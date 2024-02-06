package com.example.pcmallprovidergoods.controller;

import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallprovidergoods.service.IGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private IGoodsService goodsService;

    public GoodsController() {
        log.debug("创建Controller对象：GoodsController");
    }

    @RequestMapping("/getGoodsList")
    public ResponseResult<String> getGoodsList() {
        return ResponseResult.ok(null,"kalsjhsfkjahsfasf");
    }
}
