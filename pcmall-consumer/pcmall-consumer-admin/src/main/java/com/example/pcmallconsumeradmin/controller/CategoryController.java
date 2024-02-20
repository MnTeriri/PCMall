package com.example.pcmallconsumeradmin.controller;

import com.example.pcmallcommon.model.Category;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallconsumeradmin.client.CategoryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/getNotDeleteCategoryList")
    public ResponseResult<List<Category>> getNotDeleteCategoryList(){
        return categoryClient.getNotDeleteCategoryList();
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
