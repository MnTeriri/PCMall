package com.example.pcmallai.service.impl;

import com.alibaba.nacos.common.utils.StringUtils;
import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.model.ai.PurchaseIntent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Slf4j
@Component
public class GoodsRanker {

    public List<Goods> rank(List<Goods> candidates, PurchaseIntent intent, Integer topN) {
        if (candidates == null || candidates.isEmpty()) {
            return List.of();
        }

        return candidates.stream()
                .map(goods -> new GoodsScore(goods, calculateTotalScore(goods, intent)))
                .sorted(Comparator.comparingDouble(GoodsScore::getScore).reversed())
                .limit(topN)
                .peek(item -> log.debug("商品重排: id={}, name={}, score={}",
                        item.getGoods().getId(),
                        item.getGoods().getGname(),
                        item.getScore()))
                .map(GoodsScore::getGoods)
                .toList();
    }

    private double calculateTotalScore(Goods goods, PurchaseIntent intent) {
        double keywordScore = calculateKeywordScore(goods, intent);
        double priceScore = calculatePriceScore(goods, intent);
        double featureScore = calculateFeatureScore(goods, intent);
        double stockScore = calculateStockScore(goods);

        double totalScore =
                keywordScore * 0.45 +
                        priceScore * 0.25 +
                        featureScore * 0.20 +
                        stockScore * 0.10;

        return round(totalScore);
    }

    /**
     * 文本相关性：
     * - 商品名命中权重大
     * - 描述命中权重次之
     */
    private double calculateKeywordScore(Goods goods, PurchaseIntent intent) {
        List<String> keywords = collectKeywords(intent);
        if (keywords.isEmpty()) {
            return 0.0;
        }

        String goodsName = normalize(goods.getGname());
        String description = normalize(goods.getDescription());

        double score = 0.0;
        for (String keyword : keywords) {
            String current = normalize(keyword);
            if (!StringUtils.hasText(current)) {
                continue;
            }

            if (goodsName.contains(current)) {
                score += 1.0;
            } else if (description.contains(current)) {
                score += 0.6;
            }
        }

        return Math.min(score / keywords.size(), 1.0);
    }

    /**
     * 价格分：
     * - 有明确预算区间时，区间内得分高
     * - 越接近中间值，得分越高
     */
    private double calculatePriceScore(Goods goods, PurchaseIntent intent) {
        if (goods.getPrice() == null) {
            return 0.0;
        }

        BigDecimal price = goods.getPrice();
        BigDecimal minPrice = intent.getMinPrice();
        BigDecimal maxPrice = intent.getMaxPrice();

        if (minPrice == null && maxPrice == null) {
            return 0.5;
        }

        if (minPrice != null && maxPrice != null) {
            if (price.compareTo(minPrice) < 0 || price.compareTo(maxPrice) > 0) {
                return 0.0;
            }

            BigDecimal span = maxPrice.subtract(minPrice);
            if (span.compareTo(BigDecimal.ZERO) == 0) {
                return 1.0;
            }

            BigDecimal mid = minPrice.add(maxPrice)
                    .divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
            BigDecimal diff = price.subtract(mid).abs();

            double ratio = diff.divide(span, 4, RoundingMode.HALF_UP).doubleValue();
            return Math.max(0.4, 1.0 - ratio);
        }

        if (maxPrice != null) {
            if (price.compareTo(maxPrice) <= 0) {
                return 1.0;
            }
            BigDecimal exceed = price.subtract(maxPrice);
            double ratio = exceed.divide(maxPrice, 4, RoundingMode.HALF_UP).doubleValue();
            return Math.max(0.0, 1.0 - ratio);
        }

        if (minPrice != null) {
            if (price.compareTo(minPrice) >= 0) {
                return 1.0;
            }
            BigDecimal lack = minPrice.subtract(price);
            double ratio = lack.divide(minPrice, 4, RoundingMode.HALF_UP).doubleValue();
            return Math.max(0.0, 1.0 - ratio);
        }

        return 0.0;
    }

    /**
     * 场景 / 特征匹配：
     * usageScenarios + requiredFeatures
     */
    private double calculateFeatureScore(Goods goods, PurchaseIntent intent) {
        Set<String> features = new LinkedHashSet<>();
        if (intent.getUsageScenarios() != null) {
            features.addAll(intent.getUsageScenarios());
        }
        if (intent.getRequiredFeatures() != null) {
            features.addAll(intent.getRequiredFeatures());
        }
        if (intent.getExcludedFeatures() != null) {
            features.addAll(intent.getExcludedFeatures().stream()
                    .map(item -> "NOT:" + item)
                    .toList());
        }

        if (features.isEmpty()) {
            return 0.0;
        }

        String text = normalize(goods.getGname()) + " " + normalize(goods.getDescription());

        double score = 0.0;
        int validCount = 0;

        for (String feature : features) {
            if (!StringUtils.hasText(feature)) {
                continue;
            }
            validCount++;

            if (feature.startsWith("NOT:")) {
                String excluded = normalize(feature.substring(4));
                if (StringUtils.hasText(excluded) && text.contains(excluded)) {
                    score -= 1.0;
                }
            } else {
                String required = normalize(feature);
                if (text.contains(required)) {
                    score += 1.0;
                }
            }
        }

        if (validCount == 0) {
            return 0.0;
        }

        double normalized = (score + validCount) / (2.0 * validCount);
        return Math.max(0.0, Math.min(normalized, 1.0));
    }

    /**
     * 简单业务分：
     * - 库存越多越好
     * - 有折扣稍加分
     */
    private double calculateStockScore(Goods goods) {
        double score = 0.0;

        if (goods.getCount() != null) {
            if (goods.getCount() > 20) {
                score += 0.7;
            } else if (goods.getCount() > 0) {
                score += 0.5;
            }
        }

        if (goods.getDiscount() != null && goods.getDiscount().compareTo(BigDecimal.ZERO) > 0) {
            score += 0.3;
        }

        return Math.min(score, 1.0);
    }

    private List<String> collectKeywords(PurchaseIntent intent) {
        Set<String> set = new LinkedHashSet<>();

        addIfPresent(set, intent.getCategoryKeyword());
        addIfPresent(set, intent.getGoodsKeyword());

        if (intent.getBrandKeywords() != null) {
            intent.getBrandKeywords().forEach(item -> addIfPresent(set, item));
        }
        if (intent.getUsageScenarios() != null) {
            intent.getUsageScenarios().forEach(item -> addIfPresent(set, item));
        }
        if (intent.getRequiredFeatures() != null) {
            intent.getRequiredFeatures().forEach(item -> addIfPresent(set, item));
        }

        return new ArrayList<>(set);
    }

    private void addIfPresent(Set<String> set, String value) {
        if (StringUtils.hasText(value)) {
            set.add(value.trim());
        }
    }

    private String normalize(String text) {
        return text == null ? "" : text.trim().toLowerCase(Locale.ROOT);
    }

    private Double round(double value) {
        return BigDecimal.valueOf(value)
                .setScale(4, RoundingMode.HALF_UP)
                .doubleValue();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    private static class GoodsScore {
        private Goods goods;
        private Double score;
    }
}
