package com.example.pcmallconsumermobile.controller;

import com.example.pcmallcommon.client.OrderClient;
import com.example.pcmallcommon.model.Order;
import com.example.pcmallcommon.response.ResponseResult;
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
    public ResponseResult<List<Order>> searchOrderList(String searchValue, String uid, Integer type, Integer currentPage, Integer pageSize) {
        return orderClient.searchOrderList(searchValue, uid, type, currentPage, pageSize);
    }

    @RequestMapping("/getRecordsFiltered")
    public ResponseResult<Long> getRecordsFiltered(String searchValue, String uid, Integer type) {
        return orderClient.getRecordsFiltered(searchValue, uid, type);
    }

    @RequestMapping("/createOrder")
    public ResponseResult<String> createOrder(String uid, Integer aid) {
        return orderClient.createOrder(uid, aid);
    }

    @RequestMapping("/payOrder")
    public ResponseResult<String> payOrder(String oid) {
        return orderClient.payOrder(oid);
    }

    @RequestMapping("/finishOrder")
    public ResponseResult<String> finishOrder(String oid) {
        return orderClient.finishOrder(oid);
    }

    @RequestMapping("/cancelOrder")
    public ResponseResult<String> cancelOrder(String oid) {
        return orderClient.cancelOrder(oid);
    }

    @RequestMapping("/refundOrder")
    public ResponseResult<String> refundOrder(String oid) {
        return orderClient.refundOrder(oid);
    }
}
