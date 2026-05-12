package com.example.pcmallai.ai.rag;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import io.milvus.client.MilvusServiceClient;
import io.milvus.common.clientenum.ConsistencyLevelEnum;
import io.milvus.param.ConnectParam;
import io.milvus.param.IndexType;
import io.milvus.param.MetricType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MilvusConfig {

    @Value("${milvus.host}")
    private String milvusHost;
    @Value("${milvus.port}")
    private Integer milvusPort;

    @Bean
    public MilvusServiceClient milvusServiceClient() {
        ConnectParam build = ConnectParam.newBuilder()
                .withHost(milvusHost)
                .withPort(milvusPort)
                .build();
        return new MilvusServiceClient(build);
    }

    @Bean
    public EmbeddingStore<TextSegment> milvusStaticEmbeddingStore(
            MilvusServiceClient milvusServiceClient,
            @Value("${milvus.static.collection-name}") String collectionName,
            @Value("${milvus.static.dimension}") Integer dimension
    ) {
        return MilvusEmbeddingStore.builder()
                .milvusClient(milvusServiceClient)          // Use an existing Milvus client
                .collectionName(collectionName)             // Name of the collection
                .dimension(dimension)                       // Dimension of vectors
                .indexType(IndexType.FLAT)                 // Index type
                .metricType(MetricType.COSINE)             // Metric type
                .consistencyLevel(ConsistencyLevelEnum.EVENTUALLY)  // Consistency level
                .autoFlushOnInsert(true)                   // Auto flush after insert
                .idFieldName("id")                         // ID field name
                .textFieldName("text")                     // Text field name
                .metadataFieldName("metadata")             // Metadata field name
                .vectorFieldName("vector")                 // Vector field name
                .build();                                  // Build the MilvusEmbeddingStore instance
    }

    @Bean
    public EmbeddingStore<TextSegment> milvusGoodsEmbeddingStore(
            MilvusServiceClient milvusServiceClient,
            @Value("${milvus.goods.collection-name}") String collectionName,
            @Value("${milvus.goods.dimension}") Integer dimension
    ) {
        return MilvusEmbeddingStore.builder()
                .milvusClient(milvusServiceClient)
                .collectionName(collectionName)
                .dimension(dimension)
                .indexType(IndexType.FLAT)
                .metricType(MetricType.COSINE)
                .consistencyLevel(ConsistencyLevelEnum.EVENTUALLY)
                .autoFlushOnInsert(true)
                .idFieldName("id")
                .textFieldName("text")
                .metadataFieldName("metadata")
                .vectorFieldName("vector")
                .build();
    }
}
