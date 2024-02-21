package com.example.pcmallprovidergoods.controller;

import com.example.pcmallcommon.model.Brand;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallprovidergoods.service.IBrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    private IBrandService brandService;

    public BrandController() {
        log.debug("创建Controller对象：BrandController");
    }

    @PostMapping("/getBrandList")
    public ResponseResult<List<Brand>> getBrandList(Integer currentPage, Integer pageSize) {
        List<Brand> brandList = brandService.getBrandList(currentPage, pageSize);
        return ResponseResult.ok(brandList);
    }

    @PostMapping("/getBrandListByCid")
    public ResponseResult<List<Brand>> getBrandListByCid(Integer cid) {
        List<Brand> brandList = brandService.getBrandListByCid(cid);
        return ResponseResult.ok(brandList);
    }

    @PostMapping("/getSelectedCategoryIdList")
    public ResponseResult<List<Integer>> getSelectedCategoryIdList(Integer bid) {
        List<Integer> categoryList = brandService.getSelectedCategoryIdList(bid);
        return ResponseResult.ok(categoryList);
    }

    @PostMapping("/getTotalCount")
    public ResponseResult<Long> getTotalCount(){
        Long count = brandService.getTotalCount();
        return ResponseResult.ok(count);
    }

    @PostMapping("/addBrand")
    public ResponseResult<String> addBrand(@RequestBody Brand brand) {
        if (brandService.addBrand(brand) == 1) {
            return ResponseResult.ok("添加成功");
        }
        return ResponseResult.error("添加失败");
    }

    @PostMapping("/updateBrand")
    public ResponseResult<String> updateBrand(@RequestBody Brand brand) {
        if (brandService.updateBrand(brand) == 1) {
            return ResponseResult.ok("修改成功");
        }
        return ResponseResult.error("修改失败");
    }

    @PostMapping("/deleteBrand")
    public ResponseResult<String> deleteBrand(@RequestBody Brand brand) {
        brand.setIsDelete(1);
        if (brandService.updateBrand(brand) == 1) {
            return ResponseResult.ok("删除成功");
        }
        return ResponseResult.error("删除失败");
    }

    @PostMapping("/recoverBrand")
    public ResponseResult<String> recoverBrand(@RequestBody Brand brand) {
        brand.setIsDelete(0);
        if (brandService.updateBrand(brand) == 1) {
            return ResponseResult.ok("恢复成功");
        }
        return ResponseResult.error("恢复失败");
    }

    @PostMapping("/brandCategoryChange")
    public ResponseResult<String> brandCategoryChange(Integer bid, Integer cid, Boolean selected) {
        if (brandService.brandCategoryChange(bid, cid, selected) == 1) {
            return ResponseResult.ok("修改成功");
        }
        return ResponseResult.error("修改失败");
    }
}
