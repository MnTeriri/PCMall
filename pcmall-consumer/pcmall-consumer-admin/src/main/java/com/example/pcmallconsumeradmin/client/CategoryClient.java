package com.example.pcmallconsumeradmin.client;

import com.example.pcmallcommon.model.Category;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "categoryClient", value = "pcmall-provider-goods")
public interface CategoryClient {
    @PostMapping("/category/getCategoryList")
    ResponseResult<List<Category>> getCategoryList(
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize);

    @PostMapping("/category/searchCategory")
    ResponseResult<List<Category>> searchCategory();

    @PostMapping("/category/getTotalCount")
    ResponseResult<Long> getTotalCount();

    @PostMapping(value = "/category/addCategory")
    ResponseResult<String> addCategory(@RequestBody Category category);

    @PostMapping("/category/updateCategory")
    ResponseResult<String> updateCategory(@RequestBody Category category);

    @PostMapping("/category/deleteCategory")
    ResponseResult<String> deleteCategory(@RequestBody Category category);

    @PostMapping("/category/recoverCategory")
    ResponseResult<String> recoverCategory(@RequestBody Category category);
}
