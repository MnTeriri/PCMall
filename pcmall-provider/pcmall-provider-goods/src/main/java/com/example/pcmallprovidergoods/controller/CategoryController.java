package com.example.pcmallprovidergoods.controller;

import com.example.pcmallprovidergoods.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    public CategoryController() {
        log.debug("创建Controller对象：CategoryController");
    }
}
