package com.example.pcmallproviderpayment.service.impl;

import com.example.pcmallproviderpayment.dao.IOrderDao;
import com.example.pcmallproviderpayment.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private IOrderDao orderDao;

    public OrderServiceImpl() {
        log.debug("创建Service对象：OrderServiceImpl");
    }
}
