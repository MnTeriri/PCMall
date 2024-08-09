package com.example.pcmallconsumermobile.client;

import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(contextId = "goodsClient", value = "pcmall-provider-goods")
public interface GoodsClient {
    @PostMapping("/goods/searchGoodsList")
    ResponseResult<Map<String, String>> searchGoodsList(
            @RequestParam("searchValue") String searchValue,
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize);

    @PostMapping("/goods/searchGoodsByCidAndBid")
    ResponseResult<Map<String, String>> searchGoodsByCidAndBid(
            @RequestParam("cid") Integer cid,
            @RequestParam("bid") Integer bid,
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize);

//    @PostMapping("/goods/getRecordsFiltered")
//    ResponseResult<Long> getRecordsFiltered(@RequestParam("searchValue") String searchValue);
//
//    @PostMapping("/goods/getRecordsFilteredByCidAndBid")
//    ResponseResult<Long> getRecordsFilteredByCidAndBid(@RequestParam("cid") Integer cid, @RequestParam("bid") Integer bid);
}
