package com.example.pcmallconsumermobile.client;

import com.example.pcmallcommon.model.Brand;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "brandClient", value = "pcmall-provider-brand")
public interface BrandClient {
    @PostMapping("/brand/searchBrandByCid")
    ResponseResult<List<Brand>> searchBrandByCid(@RequestParam("cid") Integer cid);
}
