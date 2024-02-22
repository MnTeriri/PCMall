package com.example.pcmallprovidergoods.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pcmallcommon.model.Brand;
import com.example.pcmallprovidergoods.dao.IBrandDao;
import com.example.pcmallprovidergoods.service.IBrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class BrandServiceImpl implements IBrandService {
    @Autowired
    private IBrandDao brandDao;

    public BrandServiceImpl() {
        log.debug("创建Service对象：BrandServiceImpl");
    }

    @Override
    public List<Brand> getBrandList(Integer currentPage, Integer pageSize) {
        Page<Brand> page = new Page<>(currentPage, pageSize);
        List<Brand> records = brandDao.selectPage(page, null).getRecords();
        for (Brand brand : records) {
            brand.setCategoryCount(brandDao.getCategoryCount(brand.getId()));
        }
        return records;
    }

    @Override
    public Long getTotalCount() {
        return brandDao.selectCount(null);
    }

    @Override
    public List<Brand> searchBrandByCid(Integer cid) {
        return brandDao.searchBrandByCid(cid);
    }

    @Override
    public List<Integer> searchSelectedCategoryId(Integer bid) {
        return brandDao.searchSelectedCategoryId(bid);
    }

    @Override
    public Integer brandCategoryChange(Integer bid, Integer cid, Boolean selected) {
        if (selected) {
            return brandDao.insertCategoryBrand(bid, cid);
        }
        return brandDao.deleteCategoryBrand(bid, cid);
    }

    @Override
    public Integer addBrand(Brand brand) {
        return brandDao.insert(brand);
    }

    @Override
    public Integer updateBrand(Brand brand) {
        brand.setUpdateTime(LocalDateTime.now());
        return brandDao.updateById(brand);
    }
}
