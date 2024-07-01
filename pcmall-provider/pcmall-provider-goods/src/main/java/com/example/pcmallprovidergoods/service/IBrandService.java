package com.example.pcmallprovidergoods.service;

import com.example.pcmallcommon.model.Brand;

import java.util.List;

public interface IBrandService {

    List<Brand> getBrandList(Integer currentPage, Integer pageSize);

    Long getTotalCount();

    List<Brand> searchBrandByCid(Integer cid);

    List<Integer> searchSelectedCategoryId(Integer bid);

    Integer brandCategoryChange(Integer bid, Integer cid, Boolean selected);

    Integer addBrand(Brand brand);

    Integer updateBrand(Brand brand);

}
