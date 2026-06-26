package com.example.pcmallai.ai.rag;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.aggregator.DefaultContentAggregator;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.router.LanguageModelQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.filter.Filter;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
public class RAGConfig {

    @Resource(name = "ollamaEmbeddingModel")
    private EmbeddingModel embeddingModel;

    @Bean
    public ContentRetriever staticContentRetriever(
            @Qualifier("milvusStaticEmbeddingStore") EmbeddingStore<TextSegment> embeddingStore,
            @Value("${rag.static.max-results}") Integer maxResults,
            @Value("${rag.static.min-score}") Double minScore
    ) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(maxResults) // 最多 5 个检索结果
                .minScore(minScore) // 过滤掉分数小于 0.75 的结果
                .build();
    }

    @Bean
    public ContentRetriever goodsContentRetriever(
            @Qualifier("milvusGoodsEmbeddingStore") EmbeddingStore<TextSegment> embeddingStore,
            @Qualifier("shoppingQueryDynamicFilter") Function<Query, Filter> dynamicFilter,
            @Value("${rag.goods.max-results}") Integer maxResults,
            @Value("${rag.goods.min-score}") Double minScore
    ) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .dynamicFilter(dynamicFilter)
                .maxResults(maxResults)
                .minScore(minScore)
                .build();
    }

    @Bean
    public RetrievalAugmentor retrievalAugmentor(
            @Qualifier("staticContentRetriever") ContentRetriever staticRetriever,
            @Qualifier("goodsContentRetriever") ContentRetriever goodsRetriever,
            @Qualifier("shoppingQueryTransformer") QueryTransformer queryTransformer,
            @Qualifier("deepSeekChatModel") ChatModel chatModel
    ) {
        // 把每个 retriever 包装成一个可路由的目标
        Map<ContentRetriever, String> retrieverToDescription = new LinkedHashMap<>();
        retrieverToDescription.put(staticRetriever,
                """
                静态知识库，包含电脑硬件选购原则、参数解释、装机知识、搭配建议、避坑指南、商城规则等文档。
                适用场景：
                - 查询文本包含【静态选购知识检索】
                - 用户需要了解怎么选、看哪些参数、不同配置有什么区别、购买时注意什么
                - SHOPPING 流程中用于补充选购原则和推荐理由依据
                不适用：
                - 不负责查询实时商品、库存、价格
                - 不负责决定最终推荐商品列表
                """
        );

        retrieverToDescription.put(goodsRetriever,
                """
                商品向量知识库，包含商品名称、品牌、分类、价格、描述等商品文本。
                适用场景：
                - 查询文本包含【候选商品资料补充检索】
                - SHOPPING 流程中用于补充 Java 候选商品的描述、参数、卖点上下文
                - 只能作为解释推荐理由的证据
                重要限制：
                - 最终推荐商品列表必须以 Java GoodsQueryService 传入的 goodsList 为准
                - 不允许把商品向量库额外召回的商品当成最终推荐商品
                - 不负责替代 GoodsQueryService 做商品筛选
                """
        );

        // LanguageModelQueryRouter 利用 LLM 决定查询的路由位置
        QueryRouter queryRouter = new LanguageModelQueryRouter(chatModel, retrieverToDescription);

        return DefaultRetrievalAugmentor.builder()
                .queryTransformer(queryTransformer)
                .queryRouter(queryRouter)
                .contentAggregator(new DefaultContentAggregator())
                .build();
    }
}
