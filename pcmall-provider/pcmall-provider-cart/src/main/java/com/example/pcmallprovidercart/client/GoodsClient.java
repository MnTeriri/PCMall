package com.example.pcmallprovidercart.client;

import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(contextId = "goodsClient", value = "pcmall-provider-goods")
public interface GoodsClient {
    @PostMapping("/goods/searchGoodsById")
    ResponseResult<Goods> searchGoodsById(
            @RequestParam("id") Integer id,
            @RequestParam("isSearchCategory") boolean isSearchCategory,
            @RequestParam("isSearchBrand") boolean isSearchBrand
    );
}
