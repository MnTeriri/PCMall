package com.example.pcmallprovidergoods.controller;

import com.example.pcmallcommon.model.Category;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallprovidergoods.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    public CategoryController() {
        log.debug("创建Controller对象：CategoryController");
    }

    @PostMapping("/getCategoryList")
    public ResponseResult<List<Category>> getCategoryList(Integer currentPage, Integer pageSize) {
        List<Category> categoryList = categoryService.getCategoryList(currentPage,pageSize);
        return ResponseResult.ok(categoryList);
    }

    @PostMapping("/searchCategory")
    public ResponseResult<List<Category>> searchCategory() {
        List<Category> categoryList = categoryService.searchCategory();
        return ResponseResult.ok(categoryList);
    }

    @PostMapping("/getTotalCount")
    public ResponseResult<Long> getTotalCount(){
        Long count = categoryService.getTotalCount();
        return ResponseResult.ok(count);
    }

    @PostMapping(value = "/addCategory")
    public ResponseResult<String> addCategory(@RequestBody Category category){
        if (categoryService.addCategory(category) == 1) {
            return ResponseResult.ok("添加成功");
        }
        return ResponseResult.error("添加失败");
    }

    @PostMapping("/updateCategory")
    public ResponseResult<String> updateCategory(@RequestBody Category category){
        if (categoryService.updateCategory(category) == 1) {
            return ResponseResult.ok("修改成功");
        }
        return ResponseResult.error("修改失败");
    }

    @PostMapping("/deleteCategory")
    public ResponseResult<String> deleteCategory(@RequestBody Category category){
        category.setIsDelete(1);
        if (categoryService.updateCategory(category) == 1) {
            return ResponseResult.ok("删除成功");
        }
        return ResponseResult.error("删除失败");
    }

    @PostMapping("/recoverCategory")
    public ResponseResult<String> recoverCategory(@RequestBody Category category){
        category.setIsDelete(0);
        if (categoryService.updateCategory(category) == 1) {
            return ResponseResult.ok("恢复成功");
        }
        return ResponseResult.error("恢复失败");
    }
}
