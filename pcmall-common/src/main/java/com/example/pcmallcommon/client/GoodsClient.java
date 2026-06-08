package com.example.pcmallcommon.client;

import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.model.dto.GoodsAiSearchRequest;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange("/goods")
public interface GoodsClient {
    @PostExchange("/searchGoodsById")
    ResponseResult<Goods> searchGoodsById(
            @RequestParam("id") Integer id,
            @RequestParam("isSearchCategory") Boolean isSearchCategory,
            @RequestParam("isSearchBrand") Boolean isSearchBrand
    );

    @PostExchange("/searchAllGoods")
    ResponseResult<List<Goods>> searchAllGoods(
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize
    );

    @PostExchange("/searchGoodsByValue")
    ResponseResult<List<Goods>> searchGoodsByValue(
            @RequestParam("searchValue") String searchValue,
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize
    );

    @PostExchange("/searchGoodsByCidAndBid")
    ResponseResult<List<Goods>> searchGoodsByCidAndBid(
            @RequestParam("cid") Integer cid,
            @RequestParam("bid") Integer bid,
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize
    );

    @PostExchange("/searchGoodsByAiIntent")
    ResponseResult<List<Goods>> searchGoodsByAiIntent(@RequestBody GoodsAiSearchRequest aiSearchRequest);

    @PostExchange("/getTotalCount")
    ResponseResult<Long> getTotalCount();

    @PostExchange("/getTotalCountByValue")
    ResponseResult<Long> getTotalCountByValue(@RequestParam("searchValue") String searchValue);

    @PostExchange("/getTotalCountByCidAndBid")
    ResponseResult<Long> getTotalCountByCidAndBid(@RequestParam("cid") Integer cid, @RequestParam("bid") Integer bid);

    @PostExchange("/addGoods")
    ResponseResult<String> addGoods(@RequestBody Goods goods);

    @PostExchange("/updateGoods")
    ResponseResult<String> updateGoods(@RequestBody Goods goods);

    @PostExchange("/deleteGoods")
    ResponseResult<String> deleteGoods(@RequestParam("id") Integer id);

    @PostExchange("/recoverGoods")
    ResponseResult<String> recoverGoods(@RequestParam("id") Integer id);

    @PostExchange("/updateGoodsStatus")
    ResponseResult<String> updateGoodsStatus(
            @RequestParam("id") Integer id,
            @RequestParam("status") Integer status
    );

    @PostExchange("/goods/addGoodsCount")
    ResponseResult<String> addGoodsCount(
            @RequestParam("id") Integer id,
            @RequestParam("count") Integer count);

    @PostExchange("/goods/divGoodsCount")
    ResponseResult<String> divGoodsCount(
            @RequestParam("id") Integer id,
            @RequestParam("count") Integer count);

}
