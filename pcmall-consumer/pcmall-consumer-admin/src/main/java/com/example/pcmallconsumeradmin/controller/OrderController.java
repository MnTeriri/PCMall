package com.example.pcmallconsumeradmin.controller;

import com.example.pcmallcommon.client.OrderClient;
import com.example.pcmallcommon.model.Order;
import com.example.pcmallcommon.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
@PreAuthorize("hasRole('ADMIN')")
public class OrderController {
    @Autowired
    private OrderClient orderClient;

    public OrderController() {
        log.debug("创建Controller对象：OrderController");
    }

    @PostMapping("/getOrderList")
    public ResponseResult<List<Order>> getOrderList(
            @RequestParam(defaultValue = "") String searchValue,
            @RequestParam(defaultValue = "-1") Integer type,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return orderClient.searchOrderList(searchValue, "", type, currentPage, pageSize);
    }

    @PostMapping("/getTotalCount")
    public ResponseResult<Long> getTotalCount(
            @RequestParam(defaultValue = "") String searchValue,
            @RequestParam(defaultValue = "-1") Integer type) {
        return orderClient.getRecordsFiltered(searchValue, "", type);
    }

    @PostMapping("/sendOrder")
    public ResponseResult<String> sendOrder(String oid) {
        return orderClient.sendOrder(oid);
    }

    @PostMapping("/refundCommit")
    public ResponseResult<String> refundCommit(String oid) {
        return orderClient.refundCommit(oid);
    }
}
