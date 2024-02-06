package com.example.pcmallconsumeradmin.client;

import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "pcmall-provider-goods")
public interface GoodsClient {
    @RequestMapping("/goods/getGoodsList")
    public ResponseResult<String> getGoodsList();
}
