package com.example.pcmallconsumermobile.controller;

import com.example.pcmallcommon.client.BrandClient;
import com.example.pcmallcommon.model.Brand;
import com.example.pcmallcommon.response.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandClient brandClient;

     @PostMapping("/searchBrandByCid")
    public ResponseResult<List<Brand>> searchBrandByCid(Integer cid) {
        return brandClient.searchBrandByCid(cid);
    }
}
