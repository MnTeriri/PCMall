package com.example.pcmallconsumermobile.controller;

import com.example.pcmallcommon.model.Category;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallconsumermobile.client.CategoryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryClient categoryClient;

    public CategoryController() {
        log.debug("创建Controller对象：CategoryController");
    }

    @PostMapping("/searchCategoryList")
    public ResponseResult<List<Category>> searchCategoryList() {
        return categoryClient.searchCategoryList();
    }
}
