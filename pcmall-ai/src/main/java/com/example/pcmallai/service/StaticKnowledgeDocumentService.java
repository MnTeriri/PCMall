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
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class StaticKnowledgeDocumentService implements ApplicationRunner {
    private static final String OLD_DIR_NAME = "old";
    private static final String NEW_DIR_NAME = "new";

    @Value("${rag.static.base-dir}")
    private String baseDir;

    @Value("${rag.static.refresh-on-startup:true}")
    private Boolean fullRefreshOnStartup;

    @Autowired
    private EmbeddingModel ollamaEmbeddingModel;

    @Autowired
    @Qualifier("milvusStaticEmbeddingStore")
    private EmbeddingStore<TextSegment> milvusStaticEmbeddingStore;

    private final AtomicBoolean refreshing = new AtomicBoolean(false);

    @Override
    public void run(@NonNull ApplicationArguments args) throws Exception {
        if (fullRefreshOnStartup) {
            log.info("执行任务，启动时加载静态知识库文件夹");
            loadDocument(getOldPath());
        }
    }

    @Scheduled(cron = "${rag.static.refresh-cron}")
    private void scheduledRefresh() {
        log.debug("执行定时刷新静态知识库任务");
        refreshDocument();
    }

    @PostConstruct
    public void init() throws IOException {
        log.debug("创建静态知识库文件夹");
        Files.createDirectories(getOldPath());
        Files.createDirectories(getNewPath());
    }

    public void refreshDocument() {
        if (!refreshing.compareAndSet(false, true)) {
            log.debug("静态知识库正在刷新，跳过本次请求");
            return;
        }

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
            refreshing.set(false);
        }
    }

    public void fullRefreshDocument(){
        //全量刷新方法，等会再实现
    }

    public void loadDocument(Path path) {
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

    public Path getOldPath() {
        return Paths.get(baseDir, OLD_DIR_NAME);
    }

    public Path getNewPath() {
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

    public void moveFiles(Path source, Path target) {
        //移动到target文件夹
        log.debug("目录：{} 下的内容移动到目录：{} 中", source, target);
        FileUtil.moveContent(source, target, true);
        //清空source文件夹
        log.debug("目录：{} 内容被清空", source);
        FileUtil.clean(source.toString());
    }
}
