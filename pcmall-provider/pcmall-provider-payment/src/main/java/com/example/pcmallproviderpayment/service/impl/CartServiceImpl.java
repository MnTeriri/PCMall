package com.example.pcmallproviderpayment.service.impl;

import com.example.pcmallproviderpayment.dao.ICartDao;
import com.example.pcmallproviderpayment.service.ICartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private ICartDao cartDao;

    public CartServiceImpl() {
        log.debug("创建Service对象：CartServiceImpl");
    }
}
