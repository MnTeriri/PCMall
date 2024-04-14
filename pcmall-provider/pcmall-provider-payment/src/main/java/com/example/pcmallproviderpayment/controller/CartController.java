package com.example.pcmallproviderpayment.controller;

import com.example.pcmallcommon.model.Cart;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallproviderpayment.service.ICartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private ICartService cartService;

    public CartController() {
        log.debug("创建Controller对象：CartController");
    }

    @PostMapping("/searchCartList")
    public ResponseResult<List<Cart>> searchCartList(String uid, Integer currentPage, Integer pageSize) {
        return ResponseResult.ok(cartService.searchCartList(uid, currentPage, pageSize));
    }

    @PostMapping("/searchSelectCartList")
    public ResponseResult<List<Cart>> searchSelectCartList(String uid) {
        return ResponseResult.ok(cartService.searchSelectCartList(uid));
    }

    @PostMapping("/getTotalCount")
    public ResponseResult<Long> getTotalCount(String uid) {
        return ResponseResult.ok(cartService.getTotalCount(uid));
    }

    @PostMapping("/addCart")
    public ResponseResult<String> addCart(@RequestBody Cart cart) {
        cartService.addCart(cart);
        return ResponseResult.ok("添加成功");
    }

    @PostMapping("/addCartCount")
    public ResponseResult<String> addCartCount(@RequestBody Cart cart) {
        cartService.addCartCount(cart);
        return ResponseResult.ok("购物车商品数量增加成功！");
    }

    @PostMapping("/subCartCount")
    public ResponseResult<String> subCartCount(@RequestBody Cart cart) {
        cartService.subCartCount(cart);
        return ResponseResult.ok("购物车商品数量减少成功！");
    }

    @PostMapping("/selectCart")
    public ResponseResult<String> selectCart(@RequestBody Cart cart) {
        cartService.selectCart(cart);
        return ResponseResult.ok("购物车商品选中状态改变成功！");
    }

    @PostMapping("/selectAllCart")
    public ResponseResult<String> selectAllCart(@RequestBody Cart cart) {
        cartService.selectAllCart(cart);
        return ResponseResult.ok("购物车商品选中状态改变成功！");
    }

    @PostMapping("/deleteCart")
    public ResponseResult<String> deleteCart(@RequestBody Cart cart) {
        cartService.deleteCart(cart);
        return ResponseResult.ok("删除成功");
    }
}
