package com.example.pcmallconsumeradmin.client;

import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "goodsClient", value = "pcmall-provider-goods")
public interface GoodsClient {
    @RequestMapping("/goods/getGoodsList")
    public ResponseResult<List<Goods>> getGoodsList(
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize);

    @PostMapping("/goods/getTotalCount")
    public ResponseResult<Long> getTotalCount();

    @RequestMapping("/goods/addGoods")
    public ResponseResult<String> addGoods(@RequestBody Goods goods);

    @RequestMapping("/goods/updateGoods")
    public ResponseResult<String> updateGoods(@RequestBody Goods goods);
}
