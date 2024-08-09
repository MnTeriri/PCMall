package com.example.pcmallconsumeradmin.client;

import com.example.pcmallcommon.model.Category;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "categoryClient", value = "pcmall-provider-category")
public interface CategoryClient {
    @PostMapping("/category/getCategoryList")
    ResponseResult<List<Category>> getCategoryList(
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize);

    @PostMapping("/category/searchCategoryList")
    ResponseResult<List<Category>> searchCategoryList();

    @PostMapping("/category/getTotalCount")
    ResponseResult<Long> getTotalCount();

    @PostMapping(value = "/category/addCategory")
    ResponseResult<String> addCategory(@RequestBody Category category);

    @PostMapping("/category/updateCategory")
    ResponseResult<String> updateCategory(@RequestBody Category category);

    @PostMapping("/category/deleteCategory")
    ResponseResult<String> deleteCategory(@RequestParam("id") Integer id);

    @PostMapping("/category/recoverCategory")
    ResponseResult<String> recoverCategory(@RequestParam("id") Integer id);
}
