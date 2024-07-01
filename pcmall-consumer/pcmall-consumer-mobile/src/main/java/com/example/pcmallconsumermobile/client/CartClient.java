package com.example.pcmallconsumermobile.client;

import com.example.pcmallcommon.model.Cart;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "cartClient", value = "pcmall-provider-payment")
public interface CartClient {
    @PostMapping("/cart/searchCartList")
    ResponseResult<List<Cart>> searchCartList(
            @RequestParam("uid") String uid,
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize);

    @PostMapping("/cart/searchSelectCartList")
    ResponseResult<List<Cart>> searchSelectCartList(@RequestParam("uid") String uid);

    @PostMapping("/cart/getTotalCount")
    ResponseResult<Long> getTotalCount(@RequestParam("uid") String uid);

    @PostMapping("/cart/addCart")
    ResponseResult<String> addCart(@RequestBody Cart cart);

    @PostMapping("/cart/addCartCount")
    ResponseResult<String> addCartCount(@RequestBody Cart cart);

    @PostMapping("/cart/subCartCount")
    ResponseResult<String> subCartCount(@RequestBody Cart cart);

    @PostMapping("/cart/selectCart")
    ResponseResult<String> selectCart(@RequestBody Cart cart);

    @PostMapping("/cart/selectAllCart")
    ResponseResult<String> selectAllCart(@RequestBody Cart cart);

    @PostMapping("/cart/deleteCart")
    ResponseResult<String> deleteCart(@RequestParam("id") Integer id);
}
