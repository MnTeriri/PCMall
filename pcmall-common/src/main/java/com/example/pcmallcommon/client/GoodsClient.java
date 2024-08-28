package com.example.pcmallcommon.client;

import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


@FeignClient(contextId = "goodsClient", value = "pcmall-provider-goods")
public interface GoodsClient {
    @PostMapping("/goods/searchGoodsById")
    ResponseResult<Goods> searchGoodsById(
            @RequestParam("id") Integer id,
            @RequestParam("isSearchCategory") Boolean isSearchCategory,
            @RequestParam("isSearchBrand") Boolean isSearchBrand);

    @PostMapping("/goods/searchGoodsForUpdate")
    ResponseResult<Goods> searchGoodsForUpdate(@RequestParam("id") Integer id);

    @PostMapping("/goods/getGoodsList")
    ResponseResult<List<Goods>> getGoodsList(
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize);

    @PostMapping("/goods/searchGoodsList")
    ResponseResult<List<Goods>> searchGoodsList(
            @RequestParam("searchValue") String searchValue,
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize);

    @PostMapping("/goods/searchGoodsByCidAndBid")
    ResponseResult<List<Goods>> searchGoodsByCidAndBid(
            @RequestParam("cid") Integer cid,
            @RequestParam("bid") Integer bid,
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize);

    @PostMapping("/goods/getTotalCount")
    ResponseResult<Long> getTotalCount();

    @PostMapping("/goods/getRecordsFiltered")
    ResponseResult<Long> getRecordsFiltered(@RequestParam("searchValue") String searchValue);

    @PostMapping("/goods/getRecordsFilteredByCidAndBid")
    ResponseResult<Long> getRecordsFilteredByCidAndBid(@RequestParam("cid") Integer cid, @RequestParam("bid") Integer bid);

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

    @PostMapping("/goods/addGoodsCount")
    ResponseResult<String> addGoodsCount(
            @RequestParam("id") Integer id,
            @RequestParam("count") Integer count);

    @PostMapping("/goods/divGoodsCount")
    ResponseResult<String> divGoodsCount(
            @RequestParam("id") Integer id,
            @RequestParam("count") Integer count);

}
