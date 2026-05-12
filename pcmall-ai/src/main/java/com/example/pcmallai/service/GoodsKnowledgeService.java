package com.example.pcmallai.service;

import com.example.pcmallcommon.client.GoodsClient;
import com.example.pcmallcommon.model.Goods;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GoodsKnowledgeService {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private EmbeddingModel ollamaEmbeddingModel;

    @Autowired
    @Qualifier("milvusGoodsEmbeddingStore")
    private EmbeddingStore<TextSegment> milvusGoodsEmbeddingStore;

    public void saveAll(List<Document> documents) {
        if (documents == null || documents.isEmpty()) {
            log.warn("商品文档为空，跳过写入");
            return;
        }
        log.info("开始批量写入商品向量库，数量={}", documents.size());

        List<TextSegment> segments = documents.stream()
                .map(document -> TextSegment.from(document.text(), document.metadata()))
                .toList();
        List<Embedding> embeddings = ollamaEmbeddingModel.embedAll(segments).content();

        milvusGoodsEmbeddingStore.addAll(embeddings, segments);
        log.info("商品向量库批量写入完成，成功数量={}", segments.size());
    }

    public List<Document> test() {
        List<Document> list = new ArrayList<>();
        List<Goods> goodsList = goodsClient.searchAllGoods(0, 500).getData();
        for (Goods goods : goodsList) {
            String text = buildText(goods);
            Metadata metadata = new Metadata();
            metadata.put("id", goods.getId())
                    .put("category_id", goods.getCategory().getId())
                    .put("category_name", goods.getCategory().getCname())
                    .put("brand_id", goods.getBrand().getId())
                    .put("brand_name", goods.getBrand().getBname())
                    .put("price", String.valueOf(goods.getPrice()))
                    .put("description", goods.getDescription())
                    .put("status", goods.getStatus().getName());
            Document document = Document.from(text, metadata);
            list.add(document);
        }
        return list;
    }

    private String buildText(Goods goods) {
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
}
