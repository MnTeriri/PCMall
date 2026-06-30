package com.example.pcmallprovidergoods.provider;

import com.example.pcmallcommon.model.ai.GoodsAiSearchRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public class GoodsSqlProvider {

    public String aiSearchSql(@Param("request") GoodsAiSearchRequest request) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT goods.* FROM goods ")
                .append("INNER JOIN category ON goods.cid = category.id ")
                .append("INNER JOIN brand ON goods.bid = brand.id ")
                .append("WHERE goods.status=0 AND goods.is_delete=0 ");

        String categoryKeyword = request.getCategoryKeyword();
        List<String> brandKeywords = request.getBrandKeywords();

        if (categoryKeyword != null && !categoryKeyword.isEmpty()) {
            sql.append("AND category.cname LIKE CONCAT('%', #{request.intent.categoryKeyword} '%')");
        }

        if (brandKeywords != null && !brandKeywords.isEmpty()) {
            sql.append("AND (");
            for (int i = 0; i < brandKeywords.size(); i++) {
                if (i > 0) {
                    sql.append(" OR ");
                }
                sql.append("brand.bname LIKE CONCAT('%', ")
                        .append("#{request.intent.brandKeywords[").append(i).append("]}")
                        .append(", '%')");
            }
            sql.append(") ");
        }

        if (request.getMinPrice() != null) {
            sql.append("AND goods.price >= #{request.intent.minPrice} ");
        }

        if (request.getMaxPrice() != null) {
            sql.append("AND goods.price <= #{request.intent.maxPrice} ");
        }

        sql.append("ORDER BY goods.update_time DESC LIMIT #{request.topK}");

        System.out.println(sql);
        return sql.toString();
    }
}
