package com.example.pcmallproviderbrand.controller;

import com.example.pcmallcommon.model.Brand;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallproviderbrand.service.IBrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/brand")
@Tag(name = "brand参数")
public class BrandController {
    @Autowired
    private IBrandService brandService;

    public BrandController() {
        log.debug("创建Controller对象：{}", this);
    }

    @PostMapping("/searchBrandById")
    @Operation(summary = "查询品牌信息")
    @Parameters({
            @Parameter(name = "id", description = "品牌ID", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<Brand> searchBrandById(Integer id) {
        return ResponseResult.ok(brandService.searchBrandById(id));
    }

    @PostMapping("/getBrandList")
    @Operation(summary = "查询全部品牌信息")
    @Parameters({
            @Parameter(name = "currentPage", description = "当前页数", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "页面大小", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<List<Brand>> getBrandList(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        List<Brand> brandList = brandService.searchBrandList(IBrandService.BrandSearchType.ALL, new HashMap<>() {{
            put("pageSize", pageSize);
            put("currentPage", currentPage);
        }});
        return ResponseResult.ok(brandList);
    }

    @PostMapping("/getTotalCount")
    @Operation(summary = "查询品牌总个数")
    public ResponseResult<Long> getTotalCount() {
        return ResponseResult.ok(brandService.getTotalCount());
    }

    @PostMapping("/searchBrandByCid")
    @Operation(summary = "查询分类号为cid并且没被删除的品牌信息")
    @Parameters({
            @Parameter(name = "cid", description = "分类ID", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<List<Brand>> searchBrandByCid(Integer cid) {
        List<Brand> brandList = brandService.searchBrandList(IBrandService.BrandSearchType.SEARCH_CID, new HashMap<>() {{
            put("cid", cid);
        }});
        return ResponseResult.ok(brandList);
    }

    @PostMapping("/searchSelectedCategoryId")
    @Operation(summary = "查询品牌所属分类的分类ID")
    @Parameters({
            @Parameter(name = "bid", description = "品牌ID", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<List<Integer>> searchSelectedCategoryId(Integer bid) {
        return ResponseResult.ok(brandService.searchSelectedCategoryId(bid));
    }

    @PostMapping("/addBrand")
    @Operation(summary = "添加品牌")
    public ResponseResult<String> addBrand(@RequestBody Brand brand) {
        brandService.addBrand(brand);
        return ResponseResult.ok();
    }

    @PostMapping("/updateBrand")
    @Operation(summary = "更新品牌")
    public ResponseResult<String> updateBrand(@RequestBody Brand brand) {
        brandService.updateBrand(brand);
        return ResponseResult.ok();
    }

    @PostMapping("/deleteBrand")
    @Operation(summary = "删除品牌")
    @Parameters({
            @Parameter(name = "id", description = "品牌ID", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<String> deleteBrand(Integer id) {
        Brand brand = new Brand().setId(id).setIsDelete(1);
        brandService.updateBrand(brand);
        return ResponseResult.ok();
    }

    @PostMapping("/recoverBrand")
    @Operation(summary = "恢复品牌")
    @Parameters({
            @Parameter(name = "id", description = "品牌ID", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<String> recoverBrand(Integer id) {
        Brand brand = new Brand().setId(id).setIsDelete(0);
        brandService.updateBrand(brand);
        return ResponseResult.ok();
    }

    @PostMapping("/brandCategoryChange")
    @Operation(summary = "品牌所属分类修改")
    @Parameters({
            @Parameter(name = "bid", description = "品牌ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "cid", description = "分类ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "selected", description = "是否选择", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<String> brandCategoryChange(Integer bid, Integer cid, Boolean selected) {
        brandService.brandCategoryChange(bid, cid, selected);
        return ResponseResult.ok();
    }
}
