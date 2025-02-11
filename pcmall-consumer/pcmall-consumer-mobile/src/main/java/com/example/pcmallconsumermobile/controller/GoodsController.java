package com.example.pcmallconsumermobile.controller;

import com.example.pcmallcommon.client.GoodsClient;
import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsClient goodsClient;

    public GoodsController() {
        log.debug("创建Controller对象：{}", this);
    }

    @RequestMapping("/searchGoodsByValue")
    public ResponseResult<List<Goods>> searchGoodsByValue(
            @RequestParam(defaultValue = "") String searchValue,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return goodsClient.searchGoodsByValue(searchValue, currentPage, pageSize);
    }

    @RequestMapping("/searchGoodsByCidAndBid")
    public ResponseResult<List<Goods>> searchGoodsByCidAndBid(
            @RequestParam(defaultValue = "1") Integer cid,
            @RequestParam(defaultValue = "1") Integer bid,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return goodsClient.searchGoodsByCidAndBid(cid, bid, currentPage, pageSize);
    }

    @PostMapping("/getTotalCountByValue")
    public ResponseResult<Long> getTotalCountByValue(@RequestParam(defaultValue = "") String searchValue) {
        return goodsClient.getTotalCountByValue(searchValue);
    }

    @PostMapping("/getTotalCountByCidAndBid")
    public ResponseResult<Long> getTotalCountByCidAndBid(
            @RequestParam(defaultValue = "1") Integer cid,
            @RequestParam(defaultValue = "1") Integer bid) {
        return goodsClient.getTotalCountByCidAndBid(cid, bid);
    }
}
