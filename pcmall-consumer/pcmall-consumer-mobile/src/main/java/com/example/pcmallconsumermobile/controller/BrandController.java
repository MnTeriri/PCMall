package com.example.pcmallconsumermobile.controller;

import com.example.pcmallcommon.client.BrandClient;
import com.example.pcmallcommon.model.Brand;
import com.example.pcmallcommon.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    private BrandClient brandClient;

    public BrandController() {
        log.debug("创建Controller对象：BrandController");
    }

     @PostMapping("/searchBrandByCid")
    public ResponseResult<List<Brand>> searchBrandByCid(Integer cid) {
        return brandClient.searchBrandByCid(cid);
    }
}
