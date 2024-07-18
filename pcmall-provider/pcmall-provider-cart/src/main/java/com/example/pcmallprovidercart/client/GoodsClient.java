package com.example.pcmallprovidercart.client;

import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


@FeignClient(contextId = "goodsClient", value = "pcmall-provider-goods")
public interface GoodsClient {
    @PostMapping("/goods/newSearchGoodsById")
    ResponseResult<Goods> searchGoodsById(
            @RequestBody Map<String, Boolean> aspectRule,
            @RequestParam("id") Integer id
    );
}
