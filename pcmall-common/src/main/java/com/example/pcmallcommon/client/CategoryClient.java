package com.example.pcmallcommon.client;

import com.example.pcmallcommon.model.dto.Category;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange("/category")
public interface CategoryClient {
    @PostExchange("/searchCategoryById")
    ResponseResult<Category> searchCategoryById(@RequestParam("id") Integer id);

    @PostExchange("/getCategoryList")
    ResponseResult<List<Category>> getCategoryList(
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize
    );

    @PostExchange("/getTotalCount")
    ResponseResult<Long> getTotalCount();

    @PostExchange("/searchCategoryList")
    ResponseResult<List<Category>> searchCategoryList();

    @PostExchange(value = "/addCategory")
    ResponseResult<String> addCategory(@RequestBody Category category);

    @PostExchange("/updateCategory")
    ResponseResult<String> updateCategory(@RequestBody Category category);

    @PostExchange("/deleteCategory")
    ResponseResult<String> deleteCategory(@RequestParam("id") Integer id);

    @PostExchange("/recoverCategory")
    ResponseResult<String> recoverCategory(@RequestParam("id") Integer id);
}
