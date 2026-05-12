package com.example.pcmallai.ai.rag;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
