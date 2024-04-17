package com.example.pcmallconsumeradmin.controller;

import com.example.pcmallcommon.model.Category;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallconsumeradmin.client.CategoryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
@PreAuthorize("hasRole('ADMIN')")
public class CategoryController {
    @Autowired
    private CategoryClient categoryClient;

    public CategoryController() {
        log.debug("创建Controller对象：CategoryController");
    }

    @PostMapping("/getCategoryList")
    public ResponseResult<List<Category>> getCategoryList(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return categoryClient.getCategoryList(currentPage, pageSize);
    }

    @PostMapping("/searchCategoryList")
    public ResponseResult<List<Category>> searchCategoryList(){
        return categoryClient.searchCategoryList();
    }

    @PostMapping("/getTotalCount")
    public ResponseResult<Long> getTotalCount(){
        return categoryClient.getTotalCount();
    }

    @PostMapping("/addCategory")
    public ResponseResult<String> addCategory(Category category){
        return categoryClient.addCategory(category);
    }

    @PostMapping("/updateCategory")
    public ResponseResult<String> updateCategory(Category category) {
        return categoryClient.updateCategory(category);
    }

    @PostMapping("/deleteCategory")
    public ResponseResult<String> deleteCategory(Category category){
        return categoryClient.deleteCategory(category);
    }

    @PostMapping("/recoverCategory")
    public ResponseResult<String> recoverCategory(Category category){
        return categoryClient.recoverCategory(category);
    }
}
