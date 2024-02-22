package com.example.pcmallprovidergoods.service;

import com.example.pcmallcommon.model.Brand;

import java.util.List;

public interface IBrandService {

    public List<Brand> getBrandList(Integer currentPage, Integer pageSize);

    public Long getTotalCount();

    public List<Brand> searchBrandByCid(Integer cid);

    public List<Integer> searchSelectedCategoryId(Integer bid);

    public Integer brandCategoryChange(Integer bid, Integer cid, Boolean selected);

    public Integer addBrand(Brand brand);

    public Integer updateBrand(Brand brand);

}
