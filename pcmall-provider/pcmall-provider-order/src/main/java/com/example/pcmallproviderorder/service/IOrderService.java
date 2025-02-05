package com.example.pcmallproviderorder.service;

import com.example.pcmallcommon.model.Order;

import java.util.List;

public interface IOrderService {
    List<Order> searchOrderList(String searchValue, String uid, Integer type, Integer currentPage, Integer pageSize);

    Long getRecordsFiltered(String searchValue, String uid, Integer type);

    String createOrder(String uid, Integer aid);

    void payOrder(String oid);

    void sendOrder(String oid);

    void finishOrder(String oid);

    void refundOrder(String oid);

    /**
     * status只有两个值（4交易取消、6退货完成）
     */
    Integer cancelOrder(String oid, Integer status);//使用分布式事务取消订单

    Integer cancelOrderByProcedure(String oid, Integer status);//使用存储过程取消订单
}
