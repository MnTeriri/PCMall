package com.example.pcmallconsumermobile.controller;

import com.example.pcmallcommon.model.Order;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallconsumermobile.client.OrderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
@PreAuthorize("hasRole('USER')")
public class OrderController {
    @Autowired
    private OrderClient orderClient;

    public OrderController() {
        log.debug("创建OrderController对象：OrderController");
    }

    @RequestMapping("/searchOrderList")
    public ResponseResult<List<Order>> searchOrderList() {
        return orderClient.searchOrderList();
    }

    @RequestMapping("/getRecordsFiltered")
    public ResponseResult<Long> getRecordsFiltered(String uid, Integer type) {
        return orderClient.getRecordsFiltered(uid, type);
    }

    @RequestMapping("/createOrder")
    public ResponseResult<String> createOrder(String uid) {
        return null;
    }

    @RequestMapping("/cancelOrder")
    public ResponseResult<String> cancelOrder(String oid) {
        return null;
    }
}
