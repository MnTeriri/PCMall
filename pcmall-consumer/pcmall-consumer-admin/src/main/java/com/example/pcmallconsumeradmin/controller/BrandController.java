package com.example.pcmallconsumeradmin.controller;

import com.example.pcmallcommon.model.Brand;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallconsumeradmin.client.BrandClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/brand")
@PreAuthorize("hasRole('ADMIN')")
public class BrandController {
    @Autowired
    private BrandClient brandClient;

    public BrandController() {
        log.debug("创建Controller对象：BrandController");
    }

    @PostMapping("/getBrandList")
    public ResponseResult<List<Brand>> getBrandList(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return brandClient.getBrandList(currentPage, pageSize);
    }

    @PostMapping("/searchBrandByCid")
    public ResponseResult<List<Brand>> searchBrandByCid(Integer cid) {
        return brandClient.searchBrandByCid(cid);
    }

    @PostMapping("/searchSelectedCategoryId")
    public ResponseResult<List<Integer>> searchSelectedCategoryId(Integer bid) {
        return brandClient.searchSelectedCategoryId(bid);
    }

    @PostMapping("/getTotalCount")
    public ResponseResult<Long> getTotalCount() {
        return brandClient.getTotalCount();
    }

    @PostMapping("/addBrand")
    public ResponseResult<String> addBrand(Brand brand) {
        return brandClient.addBrand(brand);
    }

    @PostMapping("/updateBrand")
    public ResponseResult<String> updateBrand(Brand brand) {
        return brandClient.updateBrand(brand);
    }

    @PostMapping("/deleteBrand")
    public ResponseResult<String> deleteBrand(Brand brand) {
        return brandClient.deleteBrand(brand);
    }

    @PostMapping("/recoverBrand")
    public ResponseResult<String> recoverBrand(Brand brand) {
        return brandClient.recoverBrand(brand);
    }

    @PostMapping("/brandCategoryChange")
    public ResponseResult<String> brandCategoryChange(Integer bid, Integer cid, Boolean selected) {
        return brandClient.brandCategoryChange(bid, cid, selected);
    }
}
