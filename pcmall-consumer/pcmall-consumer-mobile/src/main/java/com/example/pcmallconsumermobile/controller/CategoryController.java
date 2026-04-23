package com.example.pcmallconsumermobile.controller;

import com.example.pcmallcommon.client.CategoryClient;
import com.example.pcmallcommon.model.Category;
import com.example.pcmallcommon.response.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryClient categoryClient;

    @PostMapping("/searchCategoryList")
    public ResponseResult<List<Category>> searchCategoryList() {
        return categoryClient.searchCategoryList();
    }
}
