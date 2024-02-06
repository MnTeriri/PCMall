package com.example.pcmallconsumeradmin.controller;

import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallconsumeradmin.client.GoodsClient;
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
    private GoodsClient goodsClient;
    @RequestMapping("/getGoodsList")
    public ResponseResult<String> getGoodsList(){
        System.out.println(goodsClient.getGoodsList());
        return ResponseResult.ok("alksdhjaksljd");
    }
}
