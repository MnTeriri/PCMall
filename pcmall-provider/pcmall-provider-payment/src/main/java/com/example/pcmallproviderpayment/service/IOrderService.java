package com.example.pcmallproviderpayment.service;

import com.example.pcmallcommon.model.Order;

import java.util.List;

public interface IOrderService {
    public List<Order> searchOrderList(String searchValue, String uid, Integer type, Integer currentPage, Integer pageSize);

    public Long getRecordsFiltered(String searchValue, String uid, Integer type);

    public void createOrder(String uid, Integer aid);

    //status只有两个值（4交易取消、6退货完成）
    public void cancelOrder(String oid, Integer status);
}
