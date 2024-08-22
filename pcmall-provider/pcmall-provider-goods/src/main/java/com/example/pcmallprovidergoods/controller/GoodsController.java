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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        HashMap<String, Boolean> aspectRule = new HashMap<>() {{
            put("isSearchCategory", isSearchCategory);
            put("isSearchBrand", isSearchBrand);
        }};
        Goods goods = goodsService.searchGoodsById(aspectRule, id);
        return ResponseResult.ok(goods);
    }

    @PostMapping("/getGoodsList")
    @Operation(summary = "查询全部商品信息")
    @Parameters({
            @Parameter(name = "currentPage", description = "当前页数", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "页面大小", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<List<Goods>> getGoodsList(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        HashMap<String, Boolean> aspectRule = new HashMap<>() {{
            put("isSearchCategory", true);
            put("isSearchBrand", true);
        }};
        HashMap<String, Object> searchValue = new HashMap<>() {{
            put("currentPage", currentPage);
            put("pageSize", pageSize);
        }};
        List<Goods> goodsList = goodsService.searchGoodsList(aspectRule, IGoodsService.GoodsSearchType.ALL, searchValue);
        return ResponseResult.ok(goodsList);
    }

    @PostMapping("/searchGoodsList")
    @Operation(summary = "使用搜索值查询状态正常商品信息")
    @Parameters({
            @Parameter(name = "searchValue", description = "搜索值", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "currentPage", description = "当前页数", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "页面大小", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<List<Goods>> searchGoodsList(
            @RequestParam(value = "searchValue", defaultValue = "") String searchParam,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        HashMap<String, Boolean> aspectRule = new HashMap<>() {{
            put("isSearchCategory", true);
            put("isSearchBrand", true);
        }};
        HashMap<String, Object> searchValue = new HashMap<>() {{
            put("searchValue", searchParam);
            put("currentPage", currentPage);
            put("pageSize", pageSize);
        }};
        List<Goods> goodsList = goodsService.searchGoodsList(aspectRule, IGoodsService.GoodsSearchType.SEARCH, searchValue);
        return ResponseResult.ok(goodsList);
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
        HashMap<String, Boolean> aspectRule = new HashMap<>() {{
            put("isSearchCategory", true);
            put("isSearchBrand", true);
        }};
        HashMap<String, Object> searchValue = new HashMap<>() {{
            put("cid", cid);
            put("bid", bid);
            put("currentPage", currentPage);
            put("pageSize", pageSize);
        }};
        List<Goods> goodsList = goodsService.searchGoodsList(aspectRule, IGoodsService.GoodsSearchType.SEARCH_BY_CID_AND_BID, searchValue);
        return ResponseResult.ok(goodsList);
    }

    @PostMapping("/getTotalCount")
    @Operation(summary = "查询商品信息总个数")
    public ResponseResult<Long> getTotalCount() {
        return ResponseResult.ok(goodsService.getTotalCount(IGoodsService.GoodsSearchType.ALL, null));
    }

    @PostMapping("/getRecordsFiltered")
    @Operation(summary = "使用搜索值查询状态正常商品信息总个数")
    @Parameters({
            @Parameter(name = "searchValue", description = "搜索值", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<Long> getRecordsFiltered(String searchValue) {
        return ResponseResult.ok(goodsService.getTotalCount(IGoodsService.GoodsSearchType.SEARCH, new HashMap<>() {{
            put("searchValue", searchValue);
        }}));
    }

    @PostMapping("/getRecordsFilteredByCidAndBid")
    @Operation(summary = "使用分类ID和品牌ID查询状态正常商品信息总个数")
    @Parameters({
            @Parameter(name = "cid", description = "分类ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "bid", description = "品牌ID", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<Long> getRecordsFilteredByCidAndBid(Integer cid, Integer bid) {
        return ResponseResult.ok(goodsService.getTotalCount(IGoodsService.GoodsSearchType.SEARCH_BY_CID_AND_BID, new HashMap<>() {{
            put("cid", cid);
            put("bid", bid);
        }}));
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
}
