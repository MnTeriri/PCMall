package com.example.pcmallai.service.impl;

import com.example.pcmallai.model.GoodsEmbedProgress;
import com.example.pcmallai.service.IKnowledgeService;
import com.example.pcmallcommon.client.GoodsClient;
import com.example.pcmallcommon.model.dto.Goods;
import com.example.pcmallcommon.utils.RedisUtils;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import dev.langchain4j.store.embedding.filter.MetadataFilterBuilder;
import io.milvus.client.MilvusServiceClient;
import io.milvus.param.dml.DeleteParam;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class GoodsKnowledgeServiceImpl implements IKnowledgeService {
    private static final String PROGRESS_KEY = "goods:embed:progress";

    @Value("${milvus.goods.collection-name}")
    private String goodsCollectionName;

    @Value("${rag.goods.init.batch-size:500}")
    private Integer batchSize;// 每页拉取数量

    @Value("${rag.goods.init.embed-batch-size:100}")
    private Integer embedBatchSize;// Embedding 子批大小

    @Resource
    private GoodsClient goodsClient;

    @Resource(name = "ollamaEmbeddingModel")
    private EmbeddingModel embeddingModel;

    @Resource(name = "milvusServiceClient")
    private MilvusServiceClient milvusClient;

    @Resource(name = "milvusGoodsEmbeddingStore")
    private EmbeddingStore<TextSegment> embeddingStore;

    /**
     * 初始化锁：保证 startInit 线程安全（多实例需要换成分布式锁）
     */
    private final ReentrantLock initLock = new ReentrantLock(true);

    private volatile boolean stopInit = false;

    @Async("threadPoolTaskExecutor")
    @Override
    public void fullRefresh() {
        if (!initLock.tryLock()) {
            log.warn("获得锁失败，已有初始化在运行");
            return;
        }
        log.debug("获得锁，开始全量刷新");
        stopInit = false;

        // 任务开始，设置进度为运行
        GoodsEmbedProgress progress = new GoodsEmbedProgress()
                .setStatus(GoodsEmbedProgress.ProgressStatus.RUNNING)
                .setStartTime(LocalDateTime.now());

        try {
            // 1. 获取商品总数
            Long totalCount = goodsClient.getTotalCount().getData();
            if (totalCount == null || totalCount == 0) {
                log.info("商品表为空, 跳过初始化");
                return;
            }

            progress.setTotalCount(totalCount);
            setProgress(progress);
            int totalPages = (int) Math.ceil((double) totalCount / batchSize);
            log.debug("整个商品集合数量：{}，需要执行 {} 次拉取", totalCount, totalPages);

            // 2. 清空原有商品向量集合
            clearAll();

            // 3. 分页处理
            for (int page = 1; page <= totalPages; page++) {
                if (stopInit) {
                    log.info("收到停止信号, 当前页={}, 总页数={}", page, totalPages);
                    progress.setCurrentPage(page)
                            .setStatus(GoodsEmbedProgress.ProgressStatus.IDLE);
                    setProgress(progress);
                    return;
                }

                log.info("处理第 {} / {} 页, 已处理 {}/{}", page, totalPages, progress.getProcessedCount(), totalCount);

                List<Goods> goodsList = goodsClient.searchAllGoods(page, batchSize).getData();
                if (goodsList == null || goodsList.isEmpty()) {
                    log.warn("第 {} 页无数据, 跳过", page);
                    continue;
                }

                List<Goods> list = goodsList.stream().filter(
                        goods -> Objects.equals(goods.getIsDelete(), 0)
                                && goods.getStatus() == Goods.GoodsState.NORMAL
                ).toList();//过滤掉状态不正常的数据

                int errorCount = goodsList.size() - list.size();
                if (errorCount != 0) {
                    log.debug("出现商品状态异常数据（非正常状态），过滤数量：{}", errorCount);
                    progress.setErrorCount(progress.getErrorCount() + errorCount);
                }

                // 构建 TextSegment + 分批 Embedding + 写入
                processBatch(list);

                progress.setProcessedCount(progress.getProcessedCount() + list.size())
                        .setCurrentPage(page);
                setProgress(progress);
            }

            // 4.任务完成，设置进度为成功
            progress.setStatus(GoodsEmbedProgress.ProgressStatus.COMPLETED)
                    .setEndTime(LocalDateTime.now());
            setProgress(progress);
            log.debug("全量刷新完成，共处理 {} 条数据", progress.getProcessedCount());

        } catch (Exception e) {
            progress.setStatus(GoodsEmbedProgress.ProgressStatus.FAILED)
                    .setEndTime(LocalDateTime.now())
                    .setErrorMessage(e.getMessage());
            setProgress(progress);
            log.error("全量知识库全量刷新失败", e);
        } finally {
            log.debug("任务结束，释放锁");
            initLock.unlock();
        }
    }

    @Override
    public void incrementalRefresh() {

    }

    @Override
    public void incrementalRefresh(Object data) {
        Goods goods = (Goods) data;
        deleteItem(String.valueOf(goods.getId()));
        TextSegment segment = TextSegment.from(buildGoodsText(goods), buildGoodsMetadata(goods));
        Embedding embedding = embeddingModel.embed(segment).content();
        embeddingStore.add(embedding, segment);
        log.debug("商品向量 upsert 完成: goodsId={}, name={}", goods.getId(), goods.getGname());
    }

    @Override
    public void stopRefresh() {
        stopInit = true;
        GoodsEmbedProgress progress = getProgress();
        if (progress.getStatus() == GoodsEmbedProgress.ProgressStatus.RUNNING) {
            progress.setStatus(GoodsEmbedProgress.ProgressStatus.IDLE)
                    .setEndTime(LocalDateTime.now());
            setProgress(progress);
        }
        log.info("已停止刷新动态商品知识库");
    }

    @Override
    public void clearAll() {
        DeleteParam deleteParam = DeleteParam.newBuilder()
                .withCollectionName(goodsCollectionName)
                .withExpr("id != ''")
                .build();
        milvusClient.delete(deleteParam);
        log.debug("已清空动态商品知识库向量集合: {}", goodsCollectionName);
    }

    @Override
    public void deleteItem(String key) {
        Filter filter = MetadataFilterBuilder.metadataKey("id").isEqualTo(key);
        embeddingStore.removeAll(filter);
        log.debug("动态商品向量已删除: goodsId={}", key);
    }

    @Override
    public GoodsEmbedProgress getProgress() {
        return RedisUtils.getCacheObject(PROGRESS_KEY, GoodsEmbedProgress.class);
    }

    @Override
    public void setProgress(GoodsEmbedProgress progress) {
        RedisUtils.setCacheObject(PROGRESS_KEY, progress);
    }

    /**
     * 整页数据分 embedBatchSize 批次进行 embedding 并写入
     */
    private void processBatch(List<Goods> goodsList) {
        List<TextSegment> textSegments = goodsList.stream()
                .map(goods -> TextSegment.from(buildGoodsText(goods), buildGoodsMetadata(goods)))
                .toList();

        // 分批 embedding，避免单次 embedAll 过大
        for (int i = 0; i < textSegments.size(); i += embedBatchSize) {
            if (stopInit) {
                break;
            }
            int end = Math.min(i + embedBatchSize, textSegments.size());
            List<TextSegment> subSegments = textSegments.subList(i, end);
            List<Embedding> embeddings = embeddingModel.embedAll(subSegments).content();
            embeddingStore.addAll(embeddings, subSegments);
        }
    }

    private String buildGoodsText(Goods goods) {
        return """
                商品名称: %s
                商品分类: %s
                商品品牌: %s
                商品价格: %s元
                商品折扣: %s
                商品描述: %s
                搜索关键词: %s, %s, %s
                """.formatted(
                goods.getGname(),
                goods.getCategory().getCname(),
                goods.getBrand().getBname(),
                goods.getPrice().stripTrailingZeros().toPlainString(),
                goods.getDiscount().stripTrailingZeros().toPlainString(),
                goods.getDescription(),
                goods.getGname(),
                goods.getBrand().getBname(),
                goods.getCategory().getCname()
        );
    }

    private Metadata buildGoodsMetadata(Goods goods) {
        Metadata metadata = new Metadata();
        metadata.put("id", goods.getId())
                .put("category_id", goods.getCategory().getId())
                .put("category_name", goods.getCategory().getCname())
                .put("brand_id", goods.getBrand().getId())
                .put("brand_name", goods.getBrand().getBname())
                .put("price", goods.getPrice().doubleValue())
                .put("description", goods.getDescription())
                .put("status", goods.getStatus().getName());
        return metadata;
    }
}
