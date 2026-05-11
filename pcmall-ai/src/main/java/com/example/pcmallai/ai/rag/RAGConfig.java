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

    @Value("${rag.static.max-results}")
    private Integer staticMaxResults;
    @Value("${rag.static.min-score}")
    private Double staticMinScore;

    @Bean
    public ContentRetriever contentRetriever(
            @Qualifier("milvusStaticEmbeddingStore") EmbeddingStore<TextSegment> milvusStaticEmbeddingStore
    ) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(milvusStaticEmbeddingStore)
                .embeddingModel(ollamaEmbeddingModel)
                .maxResults(staticMaxResults) // 最多 5 个检索结果
                .minScore(staticMinScore) // 过滤掉分数小于 0.75 的结果
                .build();

    }
}
