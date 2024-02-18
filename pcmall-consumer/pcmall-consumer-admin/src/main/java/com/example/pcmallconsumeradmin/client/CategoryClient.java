package com.example.pcmallconsumeradmin.client;

import com.example.pcmallcommon.model.Category;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "categoryClient", value = "pcmall-provider-goods")
public interface CategoryClient {
    @PostMapping("/category/getCategoryList")
    public ResponseResult<List<Category>> getCategoryList(
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize);

    @PostMapping("/category/getTotalCount")
    public ResponseResult<Long> getTotalCount();

    @PostMapping(value = "/category/addCategory")
    public ResponseResult<String> addCategory(@RequestBody Category category);

    @PostMapping("/category/updateCategory")
    public ResponseResult<String> updateCategory(@RequestBody Category category);

    @PostMapping("/category/deleteCategory")
    public ResponseResult<String> deleteCategory(@RequestBody Category category);

    @PostMapping("/category/recoverCategory")
    public ResponseResult<String> recoverCategory(@RequestBody Category category);
}
