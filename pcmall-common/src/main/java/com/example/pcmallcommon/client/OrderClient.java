package com.example.pcmallcommon.client;

import com.example.pcmallcommon.model.dto.Order;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange("/order")
public interface OrderClient {
    @PostExchange("/searchOrderList")
    ResponseResult<List<Order>> searchOrderList(
            @RequestParam("searchValue") String searchValue,
            @RequestParam("uid") String uid,
            @RequestParam("type") Integer type,
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize
    );

    @PostExchange("/getRecordsFiltered")
    ResponseResult<Long> getRecordsFiltered(
            @RequestParam("searchValue") String searchValue,
            @RequestParam("uid") String uid,
            @RequestParam("type") Integer type
    );

    @PostExchange("/createOrder")
    ResponseResult<String> createOrder(@RequestParam("uid") String uid, @RequestParam("aid") Integer aid);

    @PostExchange("/payOrder")
    ResponseResult<String> payOrder(@RequestParam("oid") String oid);

    @PostExchange("/sendOrder")
    ResponseResult<String> sendOrder(@RequestParam("oid") String oid);

    @PostExchange("/finishOrder")
    ResponseResult<String> finishOrder(@RequestParam("oid") String oid);

    @PostExchange("/cancelOrder")
    ResponseResult<String> cancelOrder(@RequestParam("oid") String oid);

    @PostExchange("/refundOrder")
    ResponseResult<String> refundOrder(@RequestParam("oid") String oid);

    @PostExchange("/refundCommit")
    ResponseResult<String> refundCommit(@RequestParam("oid") String oid);
}
