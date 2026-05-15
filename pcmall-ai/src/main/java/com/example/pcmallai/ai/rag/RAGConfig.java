package com.example.pcmallai.ai.rag;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.aggregator.DefaultContentAggregator;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.router.LanguageModelQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class RAGConfig {

    @Autowired
    private EmbeddingModel ollamaEmbeddingModel;

    @Bean
    public ContentRetriever staticContentRetriever(
            @Qualifier("milvusStaticEmbeddingStore") EmbeddingStore<TextSegment> embeddingStore,
            @Value("${rag.static.max-results}") Integer maxResults,
            @Value("${rag.static.min-score}") Double minScore
    ) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(ollamaEmbeddingModel)
                .maxResults(maxResults) // 最多 5 个检索结果
                .minScore(minScore) // 过滤掉分数小于 0.75 的结果
                .build();
    }

    @Bean
    public ContentRetriever goodsContentRetriever(
            @Qualifier("milvusGoodsEmbeddingStore") EmbeddingStore<TextSegment> embeddingStore,
            @Value("${rag.goods.max-results}") Integer maxResults,
            @Value("${rag.goods.min-score}") Double minScore
    ) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(ollamaEmbeddingModel)
                .maxResults(maxResults)
                .minScore(minScore)
                .build();
    }

    @Bean
    public RetrievalAugmentor retrievalAugmentor(
            @Qualifier("staticContentRetriever") ContentRetriever staticRetriever,
            @Qualifier("goodsContentRetriever") ContentRetriever goodsRetriever,
            @Qualifier("deepSeekChatModel") ChatModel chatModel
    ) {
        // 把每个 retriever 包装成一个可路由的目标
        Map<ContentRetriever, String> retrieverToDescription = new LinkedHashMap<>();
        retrieverToDescription.put(staticRetriever, "商品挑选规则、推荐偏好、退换货政策、售后规则、保修条款、配送说明等商场帮助文档");
        retrieverToDescription.put(goodsRetriever, "商品名称、品牌、分类、价格、配置参数、搜索关键词等商品数据");

        // LanguageModelQueryRouter 利用 LLM 决定查询的路由位置
        QueryRouter queryRouter = new LanguageModelQueryRouter(chatModel, retrieverToDescription);

        // 将每个查询路由到所有配置好的 ContentRetriever
//        QueryRouter queryRouter = new DefaultQueryRouter(staticRetriever, goodsRetriever);

        return DefaultRetrievalAugmentor.builder()
                .queryRouter(queryRouter)
                .contentAggregator(new DefaultContentAggregator())
                .build();
    }
}
