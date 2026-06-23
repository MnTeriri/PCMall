package com.example.pcmallconsumeradmin.controller;

import com.example.pcmallcommon.client.GoodsClient;
import com.example.pcmallcommon.model.dto.Goods;
import com.example.pcmallcommon.response.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class GoodsController {

    private final GoodsClient goodsClient;

    @RequestMapping("/getGoodsList")
    public ResponseResult<List<Goods>> getGoodsList(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return goodsClient.searchAllGoods(currentPage, pageSize);
    }

    @PostMapping("/searchGoodsById")
    public ResponseResult<Goods> searchGoodsById(Integer id) {
        return goodsClient.searchGoodsById(id, false, false);
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
    public ResponseResult<String> deleteGoods(Integer id) {
        return goodsClient.deleteGoods(id);
    }

    @PostMapping("/recoverGoods")
    public ResponseResult<String> recoverGoods(Integer id) {
        return goodsClient.recoverGoods(id);
    }

    @PostMapping("/updateGoodsStatus")
    public ResponseResult<String> updateGoodsStatus(Integer id, Integer status) {
        return goodsClient.updateGoodsStatus(id, status);
    }
}
