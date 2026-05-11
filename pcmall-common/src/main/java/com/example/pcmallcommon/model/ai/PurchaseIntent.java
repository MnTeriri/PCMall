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
//购买意图
public class PurchaseIntent {
    // 用于表示用户想购买的商品类别，例如：CPU、内存、硬盘等
    private String categoryKeyword;

    // 用于表示用户更明确的商品需求，例如：Z790、4070Ti
    private String goodsKeyword;

    // 用于表示用户偏好的品牌，例如：华硕、微星等
    private List<String> brandKeywords;

    // 用于描述用户购买商品的用途，例如：办公、打游戏、视频剪辑、学生使用
    private List<String> usageScenarios;

    // 用户可接受的价格下限，例如：3000
    private BigDecimal minPrice;

    // 用户可接受的价格上限，例如：8000
    private BigDecimal maxPrice;

    // 用户明确要求商品必须具备的属性，例如：RTX4060、16GB内存、高刷新率
    private List<String> requiredFeatures;

    // 用户明确不希望商品包含的属性，例如：二手、集成显卡、MacOS
    private List<String> excludedFeatures;

}
