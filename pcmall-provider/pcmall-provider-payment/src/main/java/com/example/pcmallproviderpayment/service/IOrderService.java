package com.example.pcmallproviderpayment.service;

import com.example.pcmallcommon.model.Order;

import java.util.List;

public interface IOrderService {
    public List<Order> searchOrderList();

    public Long getRecordsFiltered(String uid, Integer type);
}
