package com.example.pcmallai.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class QueryRoute {
    private QueryType type;// 路由结果
    private String reason;// AI判断原因
    private Double confidence;// 置信度

    public enum QueryType {
        /**
         * 商品购买、推荐、筛选
         */
        SHOPPING,

        /**
         * 商品知识咨询
         */
        KNOWLEDGE,

        /**
         * 订单相关
         */
        ORDER,

        /**
         * 售后
         */
        AFTER_SALE,

        /**
         * 普通聊天
         */
        CHAT
    }
}
