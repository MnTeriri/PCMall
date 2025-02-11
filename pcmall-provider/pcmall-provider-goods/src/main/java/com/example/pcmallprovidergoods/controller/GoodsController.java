package com.example.pcmallprovidergoods.controller;

import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallprovidergoods.service.IGoodsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/goods")
@Tag(name = "goods参数")
public class GoodsController {
    @Autowired
    private IGoodsService goodsService;

    public GoodsController() {
        log.debug("创建Controller对象：{}", this);
    }

    @PostMapping("/searchGoodsById")
    @Operation(summary = "查询商品信息")
    @Parameters({
            @Parameter(name = "id", description = "商品ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "isSearchCategory", description = "是否搜索商品分类", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "isSearchBrand", description = "是否搜索商品品牌", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<Goods> searchGoodsById(
            Integer id,
            @RequestParam(defaultValue = "false") Boolean isSearchCategory,
            @RequestParam(defaultValue = "false") Boolean isSearchBrand) {
        return ResponseResult.ok(goodsService.searchGoodsById(id, isSearchCategory, isSearchBrand));
    }

    @PostMapping("/searchAllGoods")
    @Operation(summary = "查询全部商品信息")
    @Parameters({
            @Parameter(name = "currentPage", description = "当前页数", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "页面大小", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<List<Goods>> searchAllGoods(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResponseResult.ok(goodsService.searchAllGoods(currentPage, pageSize));
    }

    @PostMapping("/searchGoodsByValue")
    @Operation(summary = "使用搜索值查询状态正常商品信息")
    @Parameters({
            @Parameter(name = "searchValue", description = "搜索值", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "currentPage", description = "当前页数", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "页面大小", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<List<Goods>> searchGoodsByValue(
            @RequestParam(defaultValue = "") String searchValue,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResponseResult.ok(goodsService.searchGoodsByValue(searchValue, currentPage, pageSize));
    }

    @PostMapping("/searchGoodsByCidAndBid")
    @Operation(summary = "使用分类ID和品牌ID查询状态正常商品信息")
    @Parameters({
            @Parameter(name = "cid", description = "分类ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "bid", description = "品牌ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "currentPage", description = "当前页数", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "页面大小", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<List<Goods>> searchGoodsByCidAndBid(
            Integer cid, Integer bid,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResponseResult.ok(goodsService.searchGoodsByCidAndBid(cid, bid, currentPage, pageSize));
    }

    @PostMapping("/getTotalCount")
    @Operation(summary = "查询商品信息总个数")
    public ResponseResult<Long> getTotalCount() {
        return ResponseResult.ok(goodsService.getTotalCount());
    }

    @PostMapping("/getTotalCountByValue")
    @Operation(summary = "使用搜索值查询状态正常商品信息总个数")
    @Parameters({
            @Parameter(name = "searchValue", description = "搜索值", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<Long> getTotalCountByValue(String searchValue) {
        return ResponseResult.ok(goodsService.getTotalCountByValue(searchValue));
    }

    @PostMapping("/getTotalCountByCidAndBid")
    @Operation(summary = "使用分类ID和品牌ID查询状态正常商品信息总个数")
    @Parameters({
            @Parameter(name = "cid", description = "分类ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "bid", description = "品牌ID", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<Long> getTotalCountByCidAndBid(Integer cid, Integer bid) {
        return ResponseResult.ok(goodsService.getTotalCountByCidAndBid(cid, bid));
    }

    @PostMapping("/addGoods")
    @Operation(summary = "添加商品信息")
    public ResponseResult<String> addGoods(@RequestBody Goods goods) {
        goodsService.addGoods(goods);
        return ResponseResult.ok();
    }

    @PostMapping("/updateGoods")
    @Operation(summary = "更新商品信息")
    public ResponseResult<String> updateGoods(@RequestBody Goods goods) {
        goodsService.updateGoods(goods);
        return ResponseResult.ok();
    }

    @PostMapping("/deleteGoods")
    @Operation(summary = "删除商品信息")
    @Parameters({
            @Parameter(name = "id", description = "商品ID", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<String> deleteGoods(Integer id) {
        Goods goods = new Goods().setId(id).setIsDelete(1);
        goodsService.updateGoods(goods);
        return ResponseResult.ok();
    }

    @PostMapping("/recoverGoods")
    @Operation(summary = "恢复商品信息")
    @Parameters({
            @Parameter(name = "id", description = "商品ID", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<String> recoverGoods(Integer id) {
        Goods goods = new Goods().setId(id).setIsDelete(0);
        goodsService.updateGoods(goods);
        return ResponseResult.ok();
    }

    @PostMapping("/updateGoodsStatus")
    @Operation(summary = "更新商品状态")
    @Parameters({
            @Parameter(name = "id", description = "商品ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "status", description = "商品状态", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<String> updateGoodsStatus(Integer id, Integer status) {
        Goods goods = new Goods().setId(id).setStatus(status);
        goodsService.updateGoodsStatus(goods);
        return ResponseResult.ok();
    }

    @PostMapping("/addGoodsCount")
    @Operation(summary = "增加商品数量")
    @Parameters({
            @Parameter(name = "id", description = "商品ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "count", description = "数量", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<String> addGoodsCount(Integer id, Integer count) {
        goodsService.addGoodsCount(id, count);
        return ResponseResult.ok();
    }

    @PostMapping("/divGoodsCount")
    @Operation(summary = "减少商品数量")
    @Parameters({
            @Parameter(name = "id", description = "商品ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "count", description = "数量", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<String> divGoodsCount(Integer id, Integer count) {
        goodsService.divGoodsCount(id, count);
        return ResponseResult.ok();
    }
}
