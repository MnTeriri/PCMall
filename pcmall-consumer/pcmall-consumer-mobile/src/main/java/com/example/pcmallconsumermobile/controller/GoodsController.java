package com.example.pcmallconsumermobile.controller;

import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallconsumermobile.client.GoodsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsClient goodsClient;

    public GoodsController() {
        log.debug("创建Controller对象：GoodsController");
    }

    @RequestMapping("/searchGoodsList")
    public ResponseResult<Map<String, String>> searchGoodsList(
            @RequestParam(defaultValue = "") String searchValue,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return goodsClient.searchGoodsList(searchValue, currentPage, pageSize);
    }

    @RequestMapping("/searchGoodsByCidAndBid")
    public ResponseResult<Map<String, String>> searchGoodsByCidAndBid(
            @RequestParam(defaultValue = "1") Integer cid,
            @RequestParam(defaultValue = "1") Integer bid,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return goodsClient.searchGoodsByCidAndBid(cid, bid, currentPage, pageSize);
    }

//    @PostMapping("/getRecordsFiltered")
//    public ResponseResult<Long> getRecordsFiltered(@RequestParam(defaultValue = "") String searchValue) {
//        return goodsClient.getRecordsFiltered(searchValue);
//    }
//
//    @PostMapping("/getRecordsFilteredByCidAndBid")
//    public ResponseResult<Long> getRecordsFilteredByCidAndBid(
//            @RequestParam(defaultValue = "1") Integer cid,
//            @RequestParam(defaultValue = "1") Integer bid) {
//        return goodsClient.getRecordsFilteredByCidAndBid(cid, bid);
//    }
}
