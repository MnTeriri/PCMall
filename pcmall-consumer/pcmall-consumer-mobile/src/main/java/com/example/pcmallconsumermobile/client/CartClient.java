package com.example.pcmallconsumermobile.client;

import com.example.pcmallcommon.model.Cart;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "cartClient", value = "pcmall-provider-cart")
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
    ResponseResult<String> addCartCount(@RequestParam("id") Integer id);

    @PostMapping("/cart/subCartCount")
    ResponseResult<String> subCartCount(@RequestParam("id") Integer id);

    @PostMapping("/cart/selectCart")
    ResponseResult<String> selectCart(@RequestParam("id") Integer id,
                                      @RequestParam("isSelect") Integer isSelect);

    @PostMapping("/cart/selectAllCart")
    ResponseResult<String> selectAllCart(@RequestParam("uid") String uid,
                                         @RequestParam("isSelect") Integer isSelect);

    @PostMapping("/cart/deleteCart")
    ResponseResult<String> deleteCart(@RequestParam("id") Integer id);
}
