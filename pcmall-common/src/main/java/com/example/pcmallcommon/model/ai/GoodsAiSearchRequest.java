package com.example.pcmallcommon.model.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class GoodsAiSearchRequest {
    private Integer topK;//搜索商品个数
    private String categoryKeyword;
    private String goodsKeyword;
    private List<String> brandKeywords;
    private List<String> usageScenarios;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<String> requiredFeatures;
    private List<String> excludedFeatures;
}
