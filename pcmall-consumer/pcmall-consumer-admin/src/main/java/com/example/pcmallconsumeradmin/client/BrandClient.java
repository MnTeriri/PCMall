package com.example.pcmallconsumeradmin.client;

import com.example.pcmallcommon.model.Brand;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "brandClient", value = "pcmall-provider-category")
public interface BrandClient {
    @PostMapping("/brand/getBrandList")
    ResponseResult<List<Brand>> getBrandList(
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize);

    @PostMapping("/brand/searchBrandByCid")
    ResponseResult<List<Brand>> searchBrandByCid(@RequestParam("cid") Integer cid);

    @PostMapping("/brand/searchSelectedCategoryId")
    ResponseResult<List<Integer>> searchSelectedCategoryId(@RequestParam("bid") Integer bid);

    @PostMapping("/brand/getTotalCount")
    ResponseResult<Long> getTotalCount();

    @PostMapping("/brand/addBrand")
    ResponseResult<String> addBrand(@RequestBody Brand brand);

    @PostMapping("/brand/updateBrand")
    ResponseResult<String> updateBrand(@RequestBody Brand brand);

    @PostMapping("/brand/deleteBrand")
    ResponseResult<String> deleteBrand(@RequestParam("id") Integer id);

    @PostMapping("/brand/recoverBrand")
    ResponseResult<String> recoverBrand(@RequestParam("id") Integer id);

    @PostMapping("/brand/brandCategoryChange")
    ResponseResult<String> brandCategoryChange(
            @RequestParam("bid") Integer bid,
            @RequestParam("cid") Integer cid,
            @RequestParam("selected") Boolean selected);
}
