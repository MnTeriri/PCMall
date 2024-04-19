package com.example.pcmallproviderpayment.service;

import com.example.pcmallcommon.model.Order;

import java.util.List;

public interface IOrderService {
    public List<Order> searchOrderList(String searchValue, String uid, Integer type, Integer currentPage, Integer pageSize);

    public Long getRecordsFiltered(String searchValue, String uid, Integer type);

    public String createOrder(String uid, Integer aid);

    public void payOrder(String oid);

    public void sendOrder(String oid);

    public void finishOrder(String oid);

    public void refundOrder(String oid);

    /**
     * status只有两个值（4交易取消、6退货完成）
     */
    public Integer cancelOrder(String oid, Integer status);
}
