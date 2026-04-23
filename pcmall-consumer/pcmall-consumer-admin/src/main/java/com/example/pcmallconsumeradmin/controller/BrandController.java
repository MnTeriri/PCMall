package com.example.pcmallconsumeradmin.controller;

import com.example.pcmallcommon.client.BrandClient;
import com.example.pcmallcommon.model.Brand;
import com.example.pcmallcommon.response.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class BrandController {

    private final BrandClient brandClient;

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
    public ResponseResult<String> deleteBrand(Integer id) {
        return brandClient.deleteBrand(id);
    }

    @PostMapping("/recoverBrand")
    public ResponseResult<String> recoverBrand(Integer id) {
        return brandClient.recoverBrand(id);
    }

    @PostMapping("/brandCategoryChange")
    public ResponseResult<String> brandCategoryChange(Integer bid, Integer cid, Boolean selected) {
        return brandClient.brandCategoryChange(bid, cid, selected);
    }
}
