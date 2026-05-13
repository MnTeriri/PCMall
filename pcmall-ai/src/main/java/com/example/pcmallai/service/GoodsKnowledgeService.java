package com.example.pcmallai.service;

import com.example.pcmallai.model.GoodsEmbedProgress;
import com.example.pcmallcommon.client.GoodsClient;
import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.utils.RedisUtils;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import io.milvus.client.MilvusServiceClient;
import io.milvus.param.dml.DeleteParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class GoodsKnowledgeService {

    private static final String PROGRESS_KEY = "goods:embed:progress";

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private EmbeddingModel ollamaEmbeddingModel;

    @Autowired
    private MilvusServiceClient milvusClient;

    @Autowired
    @Qualifier("milvusGoodsEmbeddingStore")
    private EmbeddingStore<TextSegment> goodsEmbeddingStore;

    @Value("${milvus.goods.collection-name}")
    private String goodsCollectionName;

    /**
     * 每页拉取数量，分批 size 建议在 embedding 模型一次可承受范围内
     */
    @Value("${rag.goods.init.batch-size:500}")
    private Integer batchSize;

    /**
     * Embedding 子批大小——如果一次 embedAll 500 条太慢，可拆成更小子批
     */
    @Value("${rag.goods.init.embed-batch-size:100}")
    private Integer embedBatchSize;

    private volatile boolean stopRequested = false;

    /**
     * 查询当前处理进度
     */
    public GoodsEmbedProgress getProgress() {
        GoodsEmbedProgress progress = RedisUtils.getCacheObject(PROGRESS_KEY, GoodsEmbedProgress.class);
        return progress != null ? progress : new GoodsEmbedProgress();
    }

    private void saveProgress(GoodsEmbedProgress progress) {
        RedisUtils.setCacheObject(PROGRESS_KEY, progress);
    }

    public void startInit() {
        GoodsEmbedProgress current = getProgress();
        if (current.getStatus() == GoodsEmbedProgress.ProgressStatus.RUNNING) {
            log.warn("全量初始化已在运行中, 忽略重复请求");
            return;
        }

        stopRequested = false;
        //修改初始化进度为正在运行
        GoodsEmbedProgress progress = new GoodsEmbedProgress();
        progress.setStatus(GoodsEmbedProgress.ProgressStatus.RUNNING);
        progress.setStartTime(LocalDateTime.now());
        saveProgress(progress);

        try {
            // 1. 获取商品总数
            Long totalCount = goodsClient.getTotalCount().getData();
            if (totalCount == null || totalCount == 0) {
                progress.setStatus(GoodsEmbedProgress.ProgressStatus.COMPLETED);
                progress.setEndTime(LocalDateTime.now());
                progress.setTotalCount(0L);
                progress.setProcessedCount(0L);
                saveProgress(progress);
                log.info("商品表为空, 跳过初始化");
                return;
            }

            int totalPages = (int) Math.ceil((double) totalCount / batchSize);
            progress.setTotalCount(totalCount);
            saveProgress(progress);
            log.debug("整个商品集合数量：{}，需要执行 {} 次拉取", totalCount, totalPages);

            // 2. 清空原有商品向量集合（全量重建）
            log.info("清空 Milvus 商品集合: {}", goodsCollectionName);
            clearCollection();

            // 3. 分页处理
            // 支持断点续传：如果之前跑过并中断，从上次的 currentPage 继续（但清空集合后不需要，这里保留逻辑完整性）
            for (int page = 0; page < totalPages; page++) {
                if (stopRequested) {
                    log.info("收到停止信号, 当前页={}, 总页数={}", page, totalPages);
                    progress.setCurrentPage(page);
                    progress.setStatus(GoodsEmbedProgress.ProgressStatus.IDLE);
                    saveProgress(progress);
                    return;
                }

                log.info("处理第 {} / {} 页, 已处理 {}/{}", page + 1, totalPages, progress.getProcessedCount(), totalCount);

                List<Goods> goodsList = goodsClient.searchAllGoods(page, batchSize).getData();
                if (goodsList == null || goodsList.isEmpty()) {
                    log.warn("第 {} 页无数据, 跳过", page);
                    continue;
                }

                // 构建 TextSegment + 分批 Embedding + 写入
                processBatch(goodsList);

                progress.setProcessedCount(progress.getProcessedCount() + goodsList.size());
                progress.setCurrentPage(page + 1);
                saveProgress(progress);
            }

        } catch (Exception e) {
            progress = getProgress();
            progress.setStatus(GoodsEmbedProgress.ProgressStatus.FAILED);
            progress.setEndTime(LocalDateTime.now());
            progress.setErrorMessage(e.getMessage());
            saveProgress(progress);
            log.error("商品向量化初始化失败", e);
        }

        if (!stopRequested) {
            //刷新完成
            log.debug("全量刷新完成");
            progress.setStatus(GoodsEmbedProgress.ProgressStatus.COMPLETED);
            progress.setEndTime(LocalDateTime.now());
            saveProgress(progress);
        }
    }

    /**
     * 停止正在运行的初始化（仅标记，下一批次生效）
     */
    public void stopInit() {
        stopRequested = true;
        GoodsEmbedProgress progress = getProgress();
        if (progress.getStatus() == GoodsEmbedProgress.ProgressStatus.RUNNING) {
            progress.setStatus(GoodsEmbedProgress.ProgressStatus.IDLE);
            progress.setEndTime(LocalDateTime.now());
            saveProgress(progress);
        }
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
            if (stopRequested) {
                break;
            }
            int end = Math.min(i + embedBatchSize, textSegments.size());
            List<TextSegment> subSegments = textSegments.subList(i, end);
            List<Embedding> embeddings = ollamaEmbeddingModel.embedAll(subSegments).content();
            goodsEmbeddingStore.addAll(embeddings, subSegments);
        }
    }

    private void clearCollection() {
        DeleteParam deleteParam = DeleteParam.newBuilder()
                .withCollectionName(goodsCollectionName)
                .withExpr("id != ''")
                .build();
        milvusClient.delete(deleteParam);
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
                .put("price", String.valueOf(goods.getPrice()))
                .put("description", goods.getDescription())
                .put("status", goods.getStatus().getName());
        return metadata;
    }
}
