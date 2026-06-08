package com.example.pcmallcommon.client;

import com.example.pcmallcommon.model.Brand;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange("/brand")
public interface BrandClient {
    @PostExchange("/searchBrandById")
    ResponseResult<Brand> searchBrandById(@RequestParam("id") Integer id);

    @PostExchange("/getBrandList")
    ResponseResult<List<Brand>> getBrandList(
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize
    );

    @PostExchange("/getTotalCount")
    ResponseResult<Long> getTotalCount();

    @PostExchange("/searchBrandByCid")
    ResponseResult<List<Brand>> searchBrandByCid(@RequestParam("cid") Integer cid);

    @PostExchange("/searchSelectedCategoryId")
    ResponseResult<List<Integer>> searchSelectedCategoryId(@RequestParam("bid") Integer bid);

    @PostExchange("/addBrand")
    ResponseResult<String> addBrand(@RequestBody Brand brand);

    @PostExchange("/updateBrand")
    ResponseResult<String> updateBrand(@RequestBody Brand brand);

    @PostExchange("/deleteBrand")
    ResponseResult<String> deleteBrand(@RequestParam("id") Integer id);

    @PostExchange("/recoverBrand")
    ResponseResult<String> recoverBrand(@RequestParam("id") Integer id);

    @PostExchange("/brandCategoryChange")
    ResponseResult<String> brandCategoryChange(
            @RequestParam("bid") Integer bid,
            @RequestParam("cid") Integer cid,
            @RequestParam("selected") Boolean selected
    );
}
