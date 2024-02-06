package com.example.pcmallproviderpayment.controller;

import com.example.pcmallproviderpayment.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    public OrderController() {
        log.debug("创建Controller对象：OrderController");
    }
}
