package com.example.pcmallconsumeradmin.client;

import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "goodsClient", value = "pcmall-provider-goods")
public interface GoodsClient {
    @PostMapping("/goods/getGoodsList")
    ResponseResult<List<Goods>> getGoodsList(
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize);

    @PostMapping("/goods/searchGoodsById")
    ResponseResult<Goods> searchGoodsById(@RequestParam("id") Integer id);

    @PostMapping("/goods/getTotalCount")
    ResponseResult<Long> getTotalCount();

    @PostMapping("/goods/addGoods")
    ResponseResult<String> addGoods(@RequestBody Goods goods);

    @PostMapping("/goods/updateGoods")
    ResponseResult<String> updateGoods(@RequestBody Goods goods);

    @PostMapping("/goods/deleteGoods")
    ResponseResult<String> deleteGoods(@RequestParam("id") Integer id);

    @PostMapping("/goods/recoverGoods")
    ResponseResult<String> recoverGoods(@RequestParam("id") Integer id);

    @PostMapping("/goods/updateGoodsStatus")
    ResponseResult<String> updateGoodsStatus(
            @RequestParam("id") Integer id,
            @RequestParam("status") Integer status);
}
