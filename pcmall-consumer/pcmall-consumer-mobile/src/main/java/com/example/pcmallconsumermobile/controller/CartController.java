package com.example.pcmallconsumermobile.controller;

import com.example.pcmallcommon.client.CartClient;
import com.example.pcmallcommon.model.Cart;
import com.example.pcmallcommon.response.ResponseResult;
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
@RequestMapping("/cart")
@PreAuthorize("hasRole('USER')")
public class CartController {
    @Autowired
    private CartClient cartClient;

    public CartController() {
        log.debug("创建Controller对象：{}", this);
    }

    @PostMapping("/searchAllCart")
    public ResponseResult<List<Cart>> searchAllCart(
            String uid,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return cartClient.searchAllCart(uid, currentPage, pageSize);
    }

    @PostMapping("/searchSelectCart")
    public ResponseResult<List<Cart>> searchSelectCart(String uid) {
        return cartClient.searchSelectCart(uid, true, true, true);
    }

    @PostMapping("/getTotalCount")
    public ResponseResult<Long> getTotalCount(String uid) {
        return cartClient.getTotalCount(uid);
    }

    @PostMapping("/addCart")
    public ResponseResult<String> addCart(Cart cart) {
        return cartClient.addCart(cart);
    }

    @PostMapping("/addCartCount")
    public ResponseResult<String> addCartCount(Integer id) {
        return cartClient.addCartCount(id);
    }

    @PostMapping("/subCartCount")
    public ResponseResult<String> subCartCount(Integer id) {
        return cartClient.subCartCount(id);
    }

    @PostMapping("/selectCart")
    public ResponseResult<String> selectCart(Integer id, Integer isSelect) {
        return cartClient.selectCart(id, isSelect);
    }

    @PostMapping("/selectAllCart")
    public ResponseResult<String> selectAllCart(String uid, Integer isSelect) {
        return cartClient.selectAllCart(uid, isSelect);
    }

    @PostMapping("/deleteCart")
    public ResponseResult<String> deleteCart(Integer id) {
        return cartClient.deleteCart(id);
    }

}
