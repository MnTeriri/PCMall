package com.example.pcmallcommon.client;

import com.example.pcmallcommon.model.dto.Cart;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange("/cart")
public interface CartClient {
    @PostExchange("/searchAllCart")
    ResponseResult<List<Cart>> searchAllCart(
            @RequestParam("uid") String uid,
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize
    );

    @PostExchange("/searchSelectCart")
    ResponseResult<List<Cart>> searchSelectCart(
            @RequestParam("uid") String uid,
            @RequestParam("isSearchGoods") Boolean isSearchGoods
    );

    @PostExchange("/getTotalCount")
    ResponseResult<Long> getTotalCount(@RequestParam("uid") String uid);

    @PostExchange("/addCart")
    ResponseResult<String> addCart(@RequestBody Cart cart);

    @PostExchange("/addCartCount")
    ResponseResult<String> addCartCount(@RequestParam("id") Integer id);

    @PostExchange("/subCartCount")
    ResponseResult<String> subCartCount(@RequestParam("id") Integer id);

    @PostExchange("/selectCart")
    ResponseResult<String> selectCart(
            @RequestParam("id") Integer id,
            @RequestParam("isSelect") Integer isSelect
    );

    @PostExchange("/selectAllCart")
    ResponseResult<String> selectAllCart(
            @RequestParam("uid") String uid,
            @RequestParam("isSelect") Integer isSelect
    );

    @PostExchange("/deleteCart")
    ResponseResult<String> deleteCart(@RequestParam("id") Integer id);
}
