package com.example.pcmallai.ai.rag.query;

import com.example.pcmallcommon.model.dto.Goods;
import dev.langchain4j.invocation.InvocationParameters;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 购物场景下的 Query 拆分器。
 * <p>
 * 子 Query 1：用户原文，保持原始语义，用于宽泛检索。
 * 子 Query 2：用户原文 + 候选商品名锚点，利用已查出的确定商品拉近向量空间中邻近的相似商品。
 */
@Slf4j
@Component
public class ShoppingQueryTransformer implements QueryTransformer {

    @Override
    public List<Query> transform(Query query) {
        InvocationParameters parameters = query.metadata().invocationParameters();
        String userMessage = parameters.get("userMessage");// 用户原始消息
        List<Goods> goodsList = parameters.get("goodsList");// 根据购买意图搜索到的商品

        // 无候选商品时不构造第二条子 Query，退化为单 Query 检索
        Query staticQuery = Query.from(buildStaticKnowledgeQuery(userMessage), query.metadata());
        if (goodsList.isEmpty()) {
            return List.of(staticQuery);
        }

        // 构造子 Query 2：用户原文 + 候选商品名锚点
        String goodsQueryText = buildGoodsQueryText(query.text(), goodsList);
        Query goodsQuery = Query.from(goodsQueryText, query.metadata());
        return List.of(staticQuery, goodsQuery);
    }

    private String buildStaticKnowledgeQuery(String originalText) {
        return """
                【静态选购知识检索】
                %s
                
                检索目标：只查与用户当前商品类别直接相关的选购原则、参数解释、避坑建议。
                """.formatted(originalText).trim();
    }

    /**
     * 构造以商品为锚点的子 Query 文本。
     */
    private String buildGoodsQueryText(String originalText, List<Goods> goodsList) {
        StringBuilder sb = new StringBuilder(originalText);

        sb.append("用户原始问题：\n").append(originalText);
        sb.append("\n\n【候选商品资料补充检索】\n");
        sb.append("以下是 Java 查询得到的候选商品：");
        for (Goods g : goodsList) {
            sb.append("\n\n商品名称: ").append(g.getGname())
                    .append("\n商品分类: ").append(g.getCategory().getCname())
                    .append("\n商品品牌: ").append(g.getBrand().getBname())
                    .append("\n商品价格: ").append(g.getPrice())
                    .append(" 元");
        }
        return sb.toString().trim();
    }
}
