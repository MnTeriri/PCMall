package com.example.pcmallconsumermobile.client;

import com.example.pcmallcommon.model.Category;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(contextId = "categoryClient", value = "pcmall-provider-goods")
public interface CategoryClient {
    @PostMapping("/category/searchCategoryList")
    public ResponseResult<List<Category>> searchCategoryList();
}
