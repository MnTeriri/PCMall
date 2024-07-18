package com.example.pcmallprovidergoods.client;

import com.example.pcmallcommon.model.Category;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "categoryClient", value = "pcmall-provider-category")
public interface CategoryClient {
    @PostMapping("/category/searchCategoryById")
    ResponseResult<Category> searchCategoryById(@RequestParam("id") Integer id);
}
