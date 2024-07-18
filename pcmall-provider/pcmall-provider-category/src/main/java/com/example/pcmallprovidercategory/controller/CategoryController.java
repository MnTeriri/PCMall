package com.example.pcmallprovidercategory.controller;

import com.example.pcmallcommon.model.Category;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallprovidercategory.service.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
@Tag(name = "category参数")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    public CategoryController() {
        log.debug("创建Controller对象：{}", this);
    }

    @PostMapping("/searchCategoryById")
    @Operation(summary = "查询分类信息")
    @Parameters({
            @Parameter(name = "id", description = "分类ID", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<Category> searchCategoryById(Integer id) {
        return ResponseResult.ok(categoryService.searchCategoryById(id));
    }

    @PostMapping("/getCategoryList")
    @Operation(summary = "查询全部分类信息")
    @Parameters({
            @Parameter(name = "currentPage", description = "当前页数", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "页面大小", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<List<Category>> getCategoryList(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        List<Category> categoryList = categoryService.searchCategoryList(ICategoryService.CategorySearchType.ALL, new HashMap<>() {{
            put("currentPage", currentPage);
            put("pageSize", pageSize);
        }});
        return ResponseResult.ok(categoryList);
    }

    @PostMapping("/getTotalCount")
    @Operation(summary = "查询分类总个数")
    public ResponseResult<Long> getTotalCount() {
        return ResponseResult.ok(categoryService.getTotalCount());
    }

    @PostMapping("/searchCategoryList")
    @Operation(summary = "查询未删除的分类信息")
    public ResponseResult<List<Category>> searchCategoryList() {
        List<Category> categoryList = categoryService.searchCategoryList(ICategoryService.CategorySearchType.SEARCH_NOT_DELETE, null);
        return ResponseResult.ok(categoryList);
    }

    @PostMapping(value = "/addCategory")
    @Operation(summary = "添加分类")
    public ResponseResult<String> addCategory(@RequestBody Category category) {
        categoryService.addCategory(category);
        return ResponseResult.ok();
    }

    @PostMapping("/updateCategory")
    @Operation(summary = "更新分类")
    public ResponseResult<String> updateCategory(@RequestBody Category category) {
        categoryService.updateCategory(category);
        return ResponseResult.ok();
    }

    @PostMapping("/deleteCategory")
    @Operation(summary = "删除分类")
    @Parameters({
            @Parameter(name = "id", description = "分类ID", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<String> deleteCategory(Integer id) {
        Category category = new Category().setId(id).setIsDelete(1);
        categoryService.updateCategory(category);
        return ResponseResult.ok();
    }

    @PostMapping("/recoverCategory")
    @Operation(summary = "恢复分类")
    @Parameters({
            @Parameter(name = "id", description = "分类ID", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<String> recoverCategory(Integer id) {
        Category category = new Category().setId(id).setIsDelete(0);
        categoryService.updateCategory(category);
        return ResponseResult.ok();
    }
}
