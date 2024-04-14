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
    public ResponseResult<List<Cart>> searchCartList(
            @RequestParam("uid") String uid,
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize);

    @PostMapping("/cart/searchSelectCartList")
    public ResponseResult<List<Cart>> searchSelectCartList(@RequestParam("uid") String uid);

    @PostMapping("/cart/getTotalCount")
    public ResponseResult<Long> getTotalCount(@RequestParam("uid") String uid);

    @PostMapping("/cart/addCart")
    public ResponseResult<String> addCart(@RequestBody Cart cart);

    @PostMapping("/cart/addCartCount")
    public ResponseResult<String> addCartCount(@RequestBody Cart cart);

    @PostMapping("/cart/subCartCount")
    public ResponseResult<String> subCartCount(@RequestBody Cart cart);

    @PostMapping("/cart/selectCart")
    public ResponseResult<String> selectCart(@RequestBody Cart cart);

    @PostMapping("/cart/selectAllCart")
    public ResponseResult<String> selectAllCart(@RequestBody Cart cart);

    @PostMapping("/cart/deleteCart")
    public ResponseResult<String> deleteCart(@RequestBody Cart cart);
}
