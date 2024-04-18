package com.example.pcmallprovidergoods.controller;

import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallprovidergoods.service.IGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private IGoodsService goodsService;

    public GoodsController() {
        log.debug("创建Controller对象：GoodsController");
    }

    @PostMapping("/getGoodsList")
    public ResponseResult<List<Goods>> getGoodsList(Integer currentPage, Integer pageSize) {
        return ResponseResult.ok(goodsService.getGoodsList(currentPage, pageSize));
    }

    @PostMapping("/searchGoodsList")
    public ResponseResult<List<Goods>> searchGoodsList(String searchValue, Integer currentPage, Integer pageSize) {
        return ResponseResult.ok(goodsService.searchGoodsList(searchValue, currentPage, pageSize));
    }

    @PostMapping("/searchGoodsByCidAndBid")
    public ResponseResult<List<Goods>> searchGoodsByCidAndBid(Integer cid, Integer bid, Integer currentPage, Integer pageSize) {
        return ResponseResult.ok(goodsService.searchGoodsByCidAndBid(cid, bid, currentPage, pageSize));
    }

    @PostMapping("/searchGoodsById")
    public ResponseResult<Goods> searchGoodsById(Integer id) {
        return ResponseResult.ok(goodsService.searchGoods(id));
    }

    @PostMapping("/getTotalCount")
    public ResponseResult<Long> getTotalCount() {
        return ResponseResult.ok(goodsService.getTotalCount());
    }

    @PostMapping("/getRecordsFiltered")
    public ResponseResult<Long> getRecordsFiltered(String searchValue) {
        return ResponseResult.ok(goodsService.getRecordsFiltered(searchValue));
    }

    @PostMapping("/getRecordsFilteredByCidAndBid")
    public ResponseResult<Long> getRecordsFilteredByCidAndBid(Integer cid, Integer bid) {
        return ResponseResult.ok(goodsService.getRecordsFilteredByCidAndBid(cid, bid));
    }

    @PostMapping("/addGoods")
    public ResponseResult<String> addGoods(@RequestBody Goods goods) {
        goodsService.addGoods(goods);
        return ResponseResult.ok("添加成功");
    }

    @PostMapping("/updateGoods")
    public ResponseResult<String> updateGoods(@RequestBody Goods goods) {
        goodsService.updateGoods(goods);
        return ResponseResult.ok("修改成功");
    }

    @PostMapping("/deleteGoods")
    public ResponseResult<String> deleteGoods(@RequestBody Goods goods) {
        goods.setIsDelete(1);
        goodsService.updateGoods(goods);
        return ResponseResult.ok("修改成功");
    }

    @PostMapping("/recoverGoods")
    public ResponseResult<String> recoverGoods(@RequestBody Goods goods) {
        goods.setIsDelete(0);
        goodsService.updateGoods(goods);
        return ResponseResult.ok("修改成功");
    }

    @PostMapping("/updateGoodsStatus")
    public ResponseResult<String> updateGoodsStatus(@RequestBody Goods goods) {
        goodsService.updateGoodsStatus(goods);
        return ResponseResult.ok("修改成功");
    }
}
