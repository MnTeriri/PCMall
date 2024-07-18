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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/searchCartList")
    @Operation(summary = "查询购物车信息")
//    @Parameters({
//            @Parameter(name = "uid", description = "用户UID", required = true, in = ParameterIn.QUERY)
//    })
    public ResponseResult<List<Cart>> searchCartList(
            String uid,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Map<String, Boolean> aspectRule = new HashMap<>() {{
            put("isSearchGoods", true);
        }};
        Map<String, Object> searchValue = new HashMap<>() {{
            put("uid", uid);
            put("currentPage", currentPage);
            put("pageSize", pageSize);
        }};
        return ResponseResult.ok(cartService.searchCartList(aspectRule, ICartService.CartSearchType.ALL, searchValue));
    }

    @PostMapping("/searchSelectCartList")
    @Operation(summary = "查询已选中购物车信息")
    public ResponseResult<List<Cart>> searchSelectCartList(String uid) {
        Map<String, Boolean> aspectRule = new HashMap<>() {{
            put("isSearchGoods", true);
        }};
        Map<String, Object> searchValue = new HashMap<>() {{
            put("uid", uid);
        }};
        return ResponseResult.ok(cartService.searchCartList(aspectRule, ICartService.CartSearchType.SELECT, searchValue));
    }
//
//    @PostMapping("/getTotalCount")
//    public ResponseResult<Long> getTotalCount(String uid) {
//        return ResponseResult.ok(cartService.getTotalCount(uid));
//    }
//
//    @PostMapping("/addCart")
//    public ResponseResult<String> addCart(@RequestBody Cart cart) {
//        cartService.addCart(cart);
//        return ResponseResult.ok("添加成功");
//    }
//
//    @PostMapping("/addCartCount")
//    public ResponseResult<String> addCartCount(@RequestBody Cart cart) {
//        cartService.addCartCount(cart);
//        return ResponseResult.ok("购物车商品数量增加成功！");
//    }
//
//    @PostMapping("/subCartCount")
//    public ResponseResult<String> subCartCount(@RequestBody Cart cart) {
//        cartService.subCartCount(cart);
//        return ResponseResult.ok("购物车商品数量减少成功！");
//    }
//
//    @PostMapping("/selectCart")
//    public ResponseResult<String> selectCart(@RequestBody Cart cart) {
//        cartService.selectCart(cart);
//        return ResponseResult.ok("购物车商品选中状态改变成功！");
//    }
//
//    @PostMapping("/selectAllCart")
//    public ResponseResult<String> selectAllCart(@RequestBody Cart cart) {
//        cartService.selectAllCart(cart);
//        return ResponseResult.ok("购物车商品选中状态改变成功！");
//    }
//
//    @PostMapping("/deleteCart")
//    public ResponseResult<String> deleteCart(Integer id) {
//        cartService.deleteCart(id);
//        return ResponseResult.ok("删除成功");
//    }
}
