package com.example.pcmallconsumeradmin.controller;

import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallconsumeradmin.client.GoodsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/goods")
@PreAuthorize("hasRole('ADMIN')")
public class GoodsController {
    @Autowired
    private GoodsClient goodsClient;

    public GoodsController() {
        log.debug("创建Controller对象：GoodsController");
    }

    @RequestMapping("/getGoodsList")
    public ResponseResult<Goods> getGoodsList() {
        return goodsClient.getGoodsList();
    }
}
