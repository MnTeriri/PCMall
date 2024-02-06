package com.example.pcmallprovidergoods.controller;

import com.example.pcmallprovidergoods.service.IBrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    private IBrandService brandService;

    public BrandController() {
        log.debug("创建Controller对象：BrandController");
    }
}
