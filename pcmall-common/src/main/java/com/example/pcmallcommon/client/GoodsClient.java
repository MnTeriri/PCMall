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
    /**
     * aspectRule：null时不增强，（isSearchCategory：是否搜索商品分类）、（isSearchBrand：是否搜索商品品牌）<br>
     * id：商品ID
     */
    @PostMapping("/goods/searchGoodsById")
    ResponseResult<Goods> searchGoodsById(
            @RequestBody Map<String, Boolean> aspectRule,
            @RequestParam("id") Integer id);

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

}
