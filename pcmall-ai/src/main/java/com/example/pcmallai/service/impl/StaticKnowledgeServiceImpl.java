package com.example.pcmallai.service.impl;

import cn.hutool.core.io.FileUtil;
import com.example.pcmallai.model.GoodsEmbedProgress;
import com.example.pcmallai.service.IKnowledgeService;
import com.example.pcmallcommon.utils.RedisUtils;
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
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class StaticKnowledgeServiceImpl implements IKnowledgeService {
    private static final String OLD_DIR_NAME = "old";
    private static final String NEW_DIR_NAME = "new";
    private static final String PROGRESS_KEY = "static:embed:progress";

    @Value("${rag.static.base-dir}")
    private String baseDir;

    @Value("${milvus.static.collection-name}")
    private String staticCollectionName;

    @Resource(name = "ollamaEmbeddingModel")
    private EmbeddingModel embeddingModel;

    @Resource(name = "milvusServiceClient")
    private MilvusServiceClient milvusClient;

    @Resource(name = "milvusStaticEmbeddingStore")
    private EmbeddingStore<TextSegment> embeddingStore;

    /**
     * 刷新锁：保证 refreshDocument 和 fullRefreshDocument 互斥（多实例需要换成分布式锁）
     */
    private final ReentrantLock refreshLock = new ReentrantLock(true);

    private volatile boolean stopRefresh = false;

    @PostConstruct
    private void init() throws IOException {
        log.debug("创建静态知识库文件夹");
        Files.createDirectories(getOldPath());
        Files.createDirectories(getNewPath());
    }

    @Async("threadPoolTaskExecutor")
    @Override
    public void fullRefresh() {
        if (!refreshLock.tryLock()) {
            log.debug("知识库刷新正在运行中，跳过本次请求");
            return;
        }
        log.debug("获得锁，开始全量刷新");
        stopRefresh = false;

        // 查询文件总数
        Path oldPath = getOldPath();
        File[] files = FileUtil.ls(oldPath.toString());
        long totalFiles = files == null ? 0 : files.length;
        if (totalFiles == 0) {
            log.debug("全量知识库文件夹为空，无需刷新");
            return;
        }

        // 任务开始，设置进度为运行
        GoodsEmbedProgress progress = new GoodsEmbedProgress()
                .setStatus(GoodsEmbedProgress.ProgressStatus.RUNNING)
                .setStartTime(LocalDateTime.now())
                .setTotalCount(totalFiles);
        setProgress(progress);

        try {
            log.debug("开始全量刷新，共处理 {} 个文件", totalFiles);
            // 1.删除数据库内容
            clearAll();

            // 2.全量重新加载静态知识库
            loadDocument(oldPath);

            // 3.任务完成，设置进度为成功
            progress.setStatus(GoodsEmbedProgress.ProgressStatus.COMPLETED)
                    .setProcessedCount(totalFiles)
                    .setEndTime(LocalDateTime.now());
            setProgress(progress);
            log.debug("全量刷新完成，共处理 {} 个文件", totalFiles);
        } catch (Exception e) {
            progress.setStatus(GoodsEmbedProgress.ProgressStatus.FAILED)
                    .setEndTime(LocalDateTime.now())
                    .setErrorMessage(e.getMessage());
            setProgress(progress);
            log.error("全量知识库全量刷新失败", e);
        } finally {
            log.debug("任务结束，释放锁");
            refreshLock.unlock();
        }
    }

    @Async("threadPoolTaskExecutor")
    @Override
    public void incrementalRefresh() {
        if (!refreshLock.tryLock()) {
            log.debug("知识库刷新正在运行中，跳过本次请求");
            return;
        }
        log.debug("获得锁，开始增量刷新");
        stopRefresh = false;

        Path oldPath = getOldPath();
        Path newPath = getNewPath();

        // 查询文件总数
        File[] files = FileUtil.ls(newPath.toString());
        long totalFiles = files == null ? 0 : files.length;
        if (totalFiles == 0) {
            log.debug("增量知识库文件夹为空，无需刷新");
            return;
        }

        // 任务开始，设置进度为运行
        GoodsEmbedProgress progress = new GoodsEmbedProgress()
                .setStatus(GoodsEmbedProgress.ProgressStatus.RUNNING)
                .setStartTime(LocalDateTime.now())
                .setTotalCount(totalFiles);
        setProgress(progress);

        try {
            log.debug("开始增量刷新，共处理 {} 个文件", totalFiles);
            //1. 删除 new 目录中已在 old 存在的同名文件的旧向量
            removeUpdatedFileVectors(newPath, oldPath);

            //2. 从新路径加载文档到数据库
            loadDocument(newPath);

            //3. 将加载过的文档移动到已加载路径
            FileUtil.moveContent(newPath, oldPath, true);
            log.debug("目录：{} 下的内容移动到目录：{} 中", newPath, oldPath);

            // 4.任务完成，设置进度为成功
            progress.setStatus(GoodsEmbedProgress.ProgressStatus.COMPLETED)
                    .setProcessedCount(totalFiles)
                    .setEndTime(LocalDateTime.now());
            setProgress(progress);
            log.debug("增量刷新完成，共处理 {} 个文件", totalFiles);
        } catch (Exception e) {
            progress.setStatus(GoodsEmbedProgress.ProgressStatus.FAILED)
                    .setEndTime(LocalDateTime.now())
                    .setErrorMessage(e.getMessage());
            setProgress(progress);
            log.error("增量知识库全量刷新失败", e);
        } finally {
            log.debug("任务结束，释放锁");
            refreshLock.unlock();
        }
    }

    @Async("threadPoolTaskExecutor")
    @Override
    public void incrementalRefresh(Object data) {
        incrementalRefresh();
    }

    @Override
    public void stopRefresh() {
        stopRefresh = true;
        GoodsEmbedProgress progress = getProgress();
        if (progress.getStatus() == GoodsEmbedProgress.ProgressStatus.RUNNING) {
            progress.setStatus(GoodsEmbedProgress.ProgressStatus.IDLE)
                    .setEndTime(LocalDateTime.now());
            setProgress(progress);
        }
    }

    @Override
    public void clearAll() {
        DeleteParam deleteParam = DeleteParam.newBuilder()
                .withCollectionName(staticCollectionName)
                .withExpr("id != ''")
                .build();
        milvusClient.delete(deleteParam);
        log.debug("已清空静态知识库向量集合: {}", staticCollectionName);
    }

    @Override
    public void deleteItem(String key) {
        Filter filter = MetadataFilterBuilder.metadataKey("file_name").isEqualTo(key);
        embeddingStore.removeAll(filter);
        log.debug("静态文档向量已删除: fileName={}", key);
    }

    @Override
    public GoodsEmbedProgress getProgress() {
        return RedisUtils.getCacheObject(PROGRESS_KEY, GoodsEmbedProgress.class);
    }

    @Override
    public void setProgress(GoodsEmbedProgress progress) {
        RedisUtils.setCacheObject(PROGRESS_KEY, progress);
    }

    private void loadDocument(Path path) {
        log.debug("加载静态知识库，路径：{}", path);
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
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        // 加载文档
        ingestor.ingest(documents);
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

    /**
     * 删除 new 目录中已在 old 存在的同名文件的旧向量。
     */
    private void removeUpdatedFileVectors(Path newPath, Path oldPath) {
        File[] files = FileUtil.ls(newPath.toString());
        for (File file : files) {
            String fileName = file.getName();
            if (!FileUtil.exist(oldPath.resolve(fileName).toString())) {
                continue;
            }
            log.debug("存在旧文件：{}，删除对应向量", fileName);
            deleteItem(fileName);
        }
    }

    private Path getOldPath() {
        return Paths.get(baseDir, OLD_DIR_NAME);
    }

    private Path getNewPath() {
        return Paths.get(baseDir, NEW_DIR_NAME);
    }
}
