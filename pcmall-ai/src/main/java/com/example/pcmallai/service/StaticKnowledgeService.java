package com.example.pcmallai.service;

import cn.hutool.core.io.FileUtil;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.MetadataFilterBuilder;
import io.milvus.client.MilvusServiceClient;
import io.milvus.param.dml.DeleteParam;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class StaticKnowledgeService {
    private static final String OLD_DIR_NAME = "old";
    private static final String NEW_DIR_NAME = "new";

    @Value("${rag.static.base-dir}")
    private String baseDir;

    @Value("${milvus.static.collection-name}")
    private String staticCollectionName;

    @Autowired
    private EmbeddingModel ollamaEmbeddingModel;

    @Autowired
    private MilvusServiceClient milvusClient;

    @Autowired
    @Qualifier("milvusStaticEmbeddingStore")
    private EmbeddingStore<TextSegment> milvusStaticEmbeddingStore;

    /**
     * 刷新锁：保证 refreshDocument 和 fullRefreshDocument 互斥（多实例需要换成分布式锁）
     */
    private final ReentrantLock refreshLock = new ReentrantLock(true);

    @PostConstruct
    private void init() throws IOException {
        log.debug("创建静态知识库文件夹");
        Files.createDirectories(getOldPath());
        Files.createDirectories(getNewPath());
    }

    @Async("threadPoolTaskExecutor")
    public void refreshDocument() {
        if (!refreshLock.tryLock()) {
            log.debug("静态知识库刷新正在运行中，跳过本次请求");
            return;
        }

        log.debug("获得锁，开始刷新");
        try {
            Path oldPath = getOldPath();
            Path newPath = getNewPath();

            File[] files = FileUtil.ls(newPath.toString());
            if (files == null || files.length == 0) {
                log.debug("增量知识库文件夹为空，无需刷新");
                return;
            }

            log.debug("进行增量静态知识库刷新，路径：{}，数量：{}", newPath, files.length);
            for (File file : files) {
                String fileName = file.getName();
                if (!FileUtil.exist(oldPath.resolve(fileName).toString())) {
                    //如果不存在，则跳过清除步骤
                    continue;
                }
                log.debug("存在文件：{}，对之前存储数据进行删除", fileName);
                Filter filter = MetadataFilterBuilder.metadataKey("file_name").isEqualTo(fileName);
                milvusStaticEmbeddingStore.removeAll(filter);
            }

            //从新路径加载文档到数据库
            loadDocument(newPath);

            //将加载过的文档移动到已加载路径
            moveFiles(newPath, oldPath);
        } finally {
            log.debug("任务结束，释放锁");
            refreshLock.unlock();
        }
    }

    @Async("threadPoolTaskExecutor")
    public void fullRefreshDocument() {
        if (!refreshLock.tryLock()) {
            log.debug("静态知识库全量刷新正在运行中，跳过本次请求");
            return;
        }

        log.debug("获得锁，开始全量刷新");
        try {
            //1.删除数据库内容
            DeleteParam deleteParam = DeleteParam.newBuilder()
                    .withCollectionName(staticCollectionName)
                    .withExpr("id != ''")
                    .build();
            milvusClient.delete(deleteParam);

            //2.重新加载静态知识库
            loadDocument(getOldPath());
        } finally {
            log.debug("任务结束，释放锁");
            refreshLock.unlock();
        }
    }

    private void loadDocument(Path path) {
        log.debug("加载静态知识库，路径：{}", path);
        // ------ RAG ------
        // 1. 加载文档
        List<Document> documents = FileSystemDocumentLoader.loadDocuments(path);
        // 2. 文档切割：将每个文档按每段进行分割，最大 1000 字符，每次重叠最多 200 个字符
        DocumentByParagraphSplitter paragraphSplitter = new DocumentByParagraphSplitter(1000, 200);
        // 3. 自定义文档加载器
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(paragraphSplitter)
                // 为了提高搜索质量，为每个 TextSegment 添加文档名称
                .textSegmentTransformer(textSegment -> TextSegment.from(
                        textSegment.metadata().getString("file_name") + "\n" + textSegment.text(),
                        textSegment.metadata()
                ))
                // 使用指定的向量模型
                .embeddingModel(ollamaEmbeddingModel)
                .embeddingStore(milvusStaticEmbeddingStore)
                .build();
        // 加载文档
        ingestor.ingest(documents);
    }

    private Path getOldPath() {
        return Paths.get(baseDir, OLD_DIR_NAME);
    }

    private Path getNewPath() {
        return Paths.get(baseDir, NEW_DIR_NAME);
    }

    public List<String> saveUploadedFiles(MultipartFile[] files) throws IOException {
        if (files == null || files.length == 0) {
            return List.of();
        }

        List<String> savedFileNames = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                continue;
            }
            String fileName = file.getOriginalFilename();
            Path target = getNewPath().resolve(fileName);
            file.transferTo(target);
            savedFileNames.add(fileName);
        }

        return savedFileNames;
    }

    private void moveFiles(Path source, Path target) {
        //移动到target文件夹
        log.debug("目录：{} 下的内容移动到目录：{} 中", source, target);
        FileUtil.moveContent(source, target, true);
    }
}
