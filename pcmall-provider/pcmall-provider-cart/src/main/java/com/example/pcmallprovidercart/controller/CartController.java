package com.example.pcmallprovidercart.controller;

import com.example.pcmallcommon.model.Cart;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallprovidercart.service.ICartService;
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
@RequestMapping("/cart")
@Tag(name = "cart参数")
public class CartController {
    @Autowired
    private ICartService cartService;

    public CartController() {
        log.debug("创建Controller对象：{}", this);
    }

    @PostMapping("/searchAllCart")
    @Operation(summary = "查询购物车信息")
    @Parameters({
            @Parameter(name = "uid", description = "用户UID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "currentPage", description = "当前页数", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "页面大小", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<List<Cart>> searchAllCart(
            String uid,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResponseResult.ok(cartService.searchAllCart(uid, currentPage, pageSize));
    }

    @PostMapping("/searchSelectCart")
    @Operation(summary = "查询已选中购物车信息")
    @Parameters({
            @Parameter(name = "uid", description = "用户UID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "isSearchGoods", description = "是否搜索商品", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<List<Cart>> searchSelectCart(
            String uid,
            @RequestParam(defaultValue = "false") Boolean isSearchGoods) {
        return ResponseResult.ok(cartService.searchSelectCart(uid, isSearchGoods));
    }

    @PostMapping("/getTotalCount")
    @Operation(summary = "查询用户购物车信息总个数")
    @Parameters({
            @Parameter(name = "uid", description = "用户UID", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<Long> getTotalCount(String uid) {
        return ResponseResult.ok(cartService.getTotalCount(uid));
    }

    @PostMapping("/addCart")
    @Operation(summary = "添加购物车信息")
    public ResponseResult<String> addCart(@RequestBody Cart cart) {
        cartService.addCart(cart);
        return ResponseResult.ok();
    }

    @PostMapping("/addCartCount")
    @Operation(summary = "增加购物车信息数量")
    @Parameters({
            @Parameter(name = "id", description = "购物车信息ID", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<String> addCartCount(Integer id) {
        cartService.addCartCount(id);
        return ResponseResult.ok();
    }

    @PostMapping("/subCartCount")
    @Operation(summary = "减少购物车信息数量")
    @Parameters({
            @Parameter(name = "id", description = "购物车信息ID", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<String> subCartCount(Integer id) {
        cartService.subCartCount(id);
        return ResponseResult.ok();
    }

    @PostMapping("/selectCart")
    @Operation(summary = "选择购物车信息")
    @Parameters({
            @Parameter(name = "id", description = "购物车信息ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "isSelect", description = "选中状态", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<String> selectCart(Integer id, Integer isSelect) {
        cartService.selectCart(id, isSelect);
        return ResponseResult.ok();
    }

    @PostMapping("/selectAllCart")
    @Operation(summary = "选择所有购物车信息")
    @Parameters({
            @Parameter(name = "id", description = "购物车信息ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "isSelect", description = "选中状态", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<String> selectAllCart(String uid, Integer isSelect) {
        cartService.selectAllCart(uid, isSelect);
        return ResponseResult.ok("购物车商品选中状态改变成功！");
    }

    @PostMapping("/deleteCart")
    @Operation(summary = "删除购物车信息")
    @Parameters({
            @Parameter(name = "id", description = "购物车信息ID", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<String> deleteCart(Integer id) {
        cartService.deleteCart(id);
        return ResponseResult.ok();
    }
}
