package com.example.pcmallproviderorder.controller;

import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.Order;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallproviderorder.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Qualifier("orderServiceImpl")
    @Autowired
    private IOrderService orderService;

    @Qualifier("seataOrderServiceImpl")
    @Autowired
    private IOrderService seataOrderService;

    @Qualifier("rocketMQOrderServiceImpl")
    @Autowired
    private IOrderService rocketMQOrderService;

    @PostMapping("/searchOrderList")
    public ResponseResult<List<Order>> searchOrderList(String searchValue, String uid, Integer type, Integer currentPage, Integer pageSize) {
        return ResponseResult.ok(orderService.searchOrderList(searchValue, uid, type, currentPage, pageSize));
    }

    @PostMapping("/getRecordsFiltered")
    public ResponseResult<Long> getRecordsFiltered(String searchValue, String uid, Integer type) {
        return ResponseResult.ok(orderService.getRecordsFiltered(searchValue, uid, type));
    }

    @PostMapping("/createOrder")
    public ResponseResult<String> createOrder(String uid, Integer aid) {
        return ResponseResult.ok(orderService.createOrder(uid, aid));
        //return ResponseResult.ok(seataOrderService.createOrder(uid, aid));
        //return ResponseResult.ok(rocketMQOrderService.createOrder(uid, aid));
    }

    @PostMapping("/payOrder")
    public ResponseResult<String> payOrder(String oid) {
        orderService.payOrder(oid);
        return ResponseResult.ok("订单付款成功！");
    }

    @PostMapping("/sendOrder")
    public ResponseResult<String> sendOrder(String oid) {
        orderService.sendOrder(oid);
        return ResponseResult.ok("订单发货成功！");
    }

    @PostMapping("/finishOrder")
    public ResponseResult<String> finishOrder(String oid) {
        orderService.finishOrder(oid);
        return ResponseResult.ok("订单签收成功！");
    }

    @PostMapping("/cancelOrder")
    public ResponseResult<String> cancelOrder(String oid) {
        if (orderService.cancelOrder(oid, 4) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
        return ResponseResult.ok("订单取消成功！");
    }

    @PostMapping("/refundOrder")
    public ResponseResult<String> refundOrder(String oid) {
        orderService.refundOrder(oid);
        return ResponseResult.ok("订单退货申请成功！");
    }

    @PostMapping("/refundCommit")
    public ResponseResult<String> refundCommit(String oid) {
        if (orderService.cancelOrder(oid, 6) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
        return ResponseResult.ok("同意订单退货成功！");
    }
}
