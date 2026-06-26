package com.example.pcmallai.service.goods;

import com.example.pcmallcommon.client.GoodsClient;
import com.example.pcmallcommon.model.ai.GoodsAiSearchRequest;
import com.example.pcmallcommon.model.ai.PurchaseIntent;
import com.example.pcmallcommon.model.dto.Goods;
import com.example.pcmallcommon.response.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsQueryService {

    private final GoodsClient goodsClient;
    private final GoodsRanker goodsRanker;

    public List<Goods> queryCandidateGoods(PurchaseIntent intent, Integer topK) {
        int topN = normalizeTopN(topK);
        int candidateTopK = normalizeCandidateTopK(topN);

        GoodsAiSearchRequest request = new GoodsAiSearchRequest()
                .setIntent(intent)
                .setTopK(candidateTopK);

        ResponseResult<List<Goods>> result = goodsClient.searchGoodsByAiIntent(request);
        List<Goods> candidates = (result == null ? null : result.getData());

        if (candidates == null || candidates.isEmpty()) {
            log.debug("商品粗召回为空, intent={}", intent);
            return List.of();
        }

        log.debug("商品粗召回数量: {}, topN={}", candidates.size(), topN);

        return goodsRanker.rank(candidates, intent, topN);
    }

    /**
     * 最终返回给 AI 的商品数量
     */
    private Integer normalizeTopN(Integer topK) {
        if (topK == null) {
            return 5;
        }
        return Math.min(topK, 10);
    }

    /**
     * 先多召回一些，再做二次重排
     */
    private int normalizeCandidateTopK(Integer topN) {
        return Math.min(Math.max(topN * 10, 30), 100);
    }
}
