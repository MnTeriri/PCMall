package com.example.pcmallai.ai.rag.filter;

import com.example.pcmallcommon.model.ai.PurchaseIntent;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.store.embedding.filter.Filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

@Slf4j
@Component
public class ShoppingQueryDynamicFilter implements Function<Query, Filter> {
    @Override
    public Filter apply(Query query) {
        // 动态过滤器，根据 AiService 传入 InvocationParameters 来对向量数据库的数据进行过滤
        PurchaseIntent intent = query.metadata().invocationParameters().get("purchaseIntent");
        double minPrice = intent.getMinPrice() == null ? 0 : intent.getMinPrice().doubleValue();
        double maxPrice = intent.getMaxPrice() == null ? Integer.MAX_VALUE : intent.getMaxPrice().doubleValue();
        return metadataKey("price").isBetween(minPrice, maxPrice);
    }
}
