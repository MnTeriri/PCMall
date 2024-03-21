package com.example.pcmallconsumeradmin.controller;

import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallconsumeradmin.client.GoodsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/goods")
@PreAuthorize("hasRole('ADMIN')")
public class GoodsController {
    @Autowired
    private GoodsClient goodsClient;

    public GoodsController() {
        log.debug("创建Controller对象：GoodsController");
    }

    @RequestMapping("/getGoodsList")
    public ResponseResult<List<Goods>> getGoodsList(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return goodsClient.getGoodsList(currentPage, pageSize);
    }

    @PostMapping("/searchGoodsById")
    public ResponseResult<Goods> searchGoodsById(Integer id) {
        return goodsClient.searchGoodsById(id);
    }

    @PostMapping("/getTotalCount")
    public ResponseResult<Long> getTotalCount() {
        return goodsClient.getTotalCount();
    }

    @RequestMapping("/addGoods")
    public ResponseResult<String> addGoods(Goods goods) {
        return goodsClient.addGoods(goods);
    }

    @RequestMapping("/updateGoods")
    public ResponseResult<String> updateGoods(Goods goods) {
        return goodsClient.updateGoods(goods);
    }

    @PostMapping("/deleteGoods")
    public ResponseResult<String> deleteGoods(Goods goods) {
        return goodsClient.deleteGoods(goods);
    }

    @PostMapping("/recoverGoods")
    public ResponseResult<String> recoverGoods(Goods goods) {
        return goodsClient.recoverGoods(goods);
    }

    @PostMapping("/updateGoodsStatus")
    public ResponseResult<String> updateGoodsStatus(Goods goods) {
        return goodsClient.updateGoodsStatus(goods);
    }
}
