package com.example.pcmallconsumeradmin.client;

import com.example.pcmallcommon.model.Brand;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "brandClient", value = "pcmall-provider-goods")
public interface BrandClient {
    @PostMapping("/brand/getBrandList")
    public ResponseResult<List<Brand>> getBrandList(
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize);


    @PostMapping("/brand/getBrandListByCid")
    public ResponseResult<List<Brand>> getBrandListByCid(@RequestParam("cid") Integer cid);

    @PostMapping("/brand/getSelectedCategoryIdList")
    public ResponseResult<List<Integer>> getSelectedCategoryIdList(@RequestParam("bid") Integer bid);

    @PostMapping("/brand/getTotalCount")
    public ResponseResult<Long> getTotalCount();

    @PostMapping("/brand/addBrand")
    public ResponseResult<String> addBrand(@RequestBody Brand brand);

    @PostMapping("/brand/updateBrand")
    public ResponseResult<String> updateBrand(@RequestBody Brand brand);

    @PostMapping("/brand/deleteBrand")
    public ResponseResult<String> deleteBrand(@RequestBody Brand brand);

    @PostMapping("/brand/recoverBrand")
    public ResponseResult<String> recoverBrand(@RequestBody Brand brand);

    @PostMapping("/brand/brandCategoryChange")
    public ResponseResult<String> brandCategoryChange(
            @RequestParam("bid") Integer bid,
            @RequestParam("cid") Integer cid,
            @RequestParam("selected") Boolean selected);
}
