package com.example.pcmallconsumermobile.client;

import com.example.pcmallcommon.model.Order;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "orderClient", value = "pcmall-provider-payment")
public interface OrderClient {
    @PostMapping("/order/searchOrderList")
    public ResponseResult<List<Order>> searchOrderList(
            @RequestParam("searchValue") String searchValue,
            @RequestParam("uid") String uid,
            @RequestParam("type") Integer type,
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize);

    @PostMapping("/order/getRecordsFiltered")
    public ResponseResult<Long> getRecordsFiltered(
            @RequestParam("searchValue") String searchValue,
            @RequestParam("uid") String uid,
            @RequestParam("type") Integer type);

    @PostMapping("/order/createOrder")
    public ResponseResult<String> createOrder(@RequestParam("uid") String uid, @RequestParam("aid") Integer aid);

    @RequestMapping("/order/payOrder")
    public ResponseResult<String> payOrder(@RequestParam("oid") String oid);

    @RequestMapping("/order/cancelOrder")
    public ResponseResult<String> cancelOrder(@RequestParam("oid") String oid);

    @RequestMapping("/order/refundOrder")
    public ResponseResult<String> refundOrder(@RequestParam("oid") String oid);
}
