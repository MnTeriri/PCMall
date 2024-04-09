package com.example.pcmallproviderpayment.service.impl;

import com.example.pcmallcommon.model.Order;
import com.example.pcmallproviderpayment.dao.IOrderDao;
import com.example.pcmallproviderpayment.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private IOrderDao orderDao;

    public OrderServiceImpl() {
        log.debug("创建Service对象：OrderServiceImpl");
    }

    @Override
    public List<Order> searchOrderList() {
        return null;
    }

    @Override
    public Long getRecordsFiltered(String uid, Integer type) {
        return orderDao.getRecordsFiltered(uid, type);
    }
}
