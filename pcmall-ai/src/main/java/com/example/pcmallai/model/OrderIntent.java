package com.example.pcmallai.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OrderIntent implements Serializable {
    @Serial
    private static final long serialVersionUID = 2455231864848469799L;

    private OrderAction action;
    private String oid;// 用户明确提到的订单号
    private String reason;// LLM 判断理由
    private Double confidence;// 置信度 0-1

    public enum OrderAction {
        QUERY,// 模糊查询
        QUERY_RECENT,// 查最近订单
        QUERY_PENDING_PAYMENT,//查待付款
        QUERY_PENDING_RECEIPT,//查待收货
        PAY,// 付款
        CONFIRM_RECEIPT, //确认收货
        CANCEL,// 取消
        REFUND_REQUEST,// 申请退货
        UNKNOWN// 无法识别
    }
}
