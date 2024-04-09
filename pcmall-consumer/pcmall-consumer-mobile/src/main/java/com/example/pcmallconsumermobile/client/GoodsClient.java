package com.example.pcmallconsumermobile.client;

import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "goodsClient", value = "pcmall-provider-goods")
public interface GoodsClient {
    @PostMapping("/goods/searchGoodsList")
    ResponseResult<List<Goods>> searchGoodsList(
            @RequestParam("searchValue") String searchValue,
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize);

    @PostMapping("/goods/getRecordsFiltered")
    public ResponseResult<Long> getRecordsFiltered(@RequestParam("searchValue") String searchValue);
}
