package com.example.pcmallconsumermobile.controller;

import com.example.pcmallcommon.model.Cart;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallconsumermobile.client.CartClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cart")
@PreAuthorize("hasRole('USER')")
public class CartController {
    @Autowired
    private CartClient cartClient;

    public CartController() {
        log.debug("创建Controller对象：CartController");
    }

    @PostMapping("/searchCartList")
    public ResponseResult<List<Cart>> searchCartList(
            String uid,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return cartClient.searchCartList(uid, currentPage, pageSize);
    }

    @PostMapping("/searchSelectCartList")
    public ResponseResult<List<Cart>> searchSelectCartList(String uid) {
        return cartClient.searchSelectCartList(uid);
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
    public ResponseResult<String> addCartCount(Cart cart) {
        return cartClient.addCartCount(cart);
    }

    @PostMapping("/subCartCount")
    public ResponseResult<String> subCartCount(Cart cart) {
        return cartClient.subCartCount(cart);
    }

    @PostMapping("/selectCart")
    public ResponseResult<String> selectCart(Cart cart) {
        return cartClient.selectCart(cart);
    }

    @PostMapping("/selectAllCart")
    public ResponseResult<String> selectAllCart(Cart cart) {
        return cartClient.selectAllCart(cart);
    }

    @PostMapping("/deleteCart")
    public ResponseResult<String> deleteCart(Integer id) {
        return cartClient.deleteCart(id);
    }

}
