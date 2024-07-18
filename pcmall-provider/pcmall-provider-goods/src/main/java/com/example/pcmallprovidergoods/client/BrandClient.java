package com.example.pcmallprovidergoods.client;

import com.example.pcmallcommon.model.Brand;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "brandClient", value = "pcmall-provider-brand")
public interface BrandClient {
    @PostMapping("/brand/searchBrandById")
    ResponseResult<Brand> searchBrandById(@RequestParam("id") Integer id);
}
