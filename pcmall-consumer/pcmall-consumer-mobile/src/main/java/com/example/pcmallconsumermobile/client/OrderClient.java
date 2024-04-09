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
    @RequestMapping("/order/searchOrderList")
    public ResponseResult<List<Order>> searchOrderList();

    @PostMapping("/order/getRecordsFiltered")
    public ResponseResult<Long> getRecordsFiltered(
            @RequestParam("uid") String uid,
            @RequestParam("type") Integer type);
}
