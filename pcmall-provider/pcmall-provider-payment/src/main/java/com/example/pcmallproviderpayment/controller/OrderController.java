package com.example.pcmallproviderpayment.controller;

import com.example.pcmallcommon.model.Order;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallproviderpayment.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    public OrderController() {
        log.debug("创建Controller对象：OrderController");
    }

    @RequestMapping("/searchOrderList")
    public ResponseResult<List<Order>> searchOrderList(String searchValue, String uid, Integer type, Integer currentPage, Integer pageSize) {
        return ResponseResult.ok(orderService.searchOrderList(searchValue, uid, type, currentPage, pageSize));
    }

    @RequestMapping("/getRecordsFiltered")
    public ResponseResult<Long> getRecordsFiltered(String searchValue, String uid, Integer type) {
        return ResponseResult.ok(orderService.getRecordsFiltered(searchValue, uid, type));
    }

    @RequestMapping("/createOrder")
    public ResponseResult<String> createOrder(String uid, Integer aid) {
        orderService.createOrder(uid, aid);
        return ResponseResult.ok("创建订单成功！");
    }
}
