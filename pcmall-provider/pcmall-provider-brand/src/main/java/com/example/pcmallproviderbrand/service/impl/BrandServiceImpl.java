package com.example.pcmallproviderbrand.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.Brand;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallproviderbrand.dao.IBrandDao;
import com.example.pcmallproviderbrand.service.IBrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BrandServiceImpl implements IBrandService {
    @Autowired
    private IBrandDao brandDao;

    public BrandServiceImpl() {
        log.debug("创建Service对象：{}", this);
    }

    @Cacheable(cacheNames = "brand", key = "#id", sync = true)
    @Override
    public Brand searchBrandById(Integer id) {
        return brandDao.selectById(id);
    }

    @Override
    public List<Brand> searchBrandList(BrandSearchType searchType, Map<String, Object> searchValue) {
        if (searchType == BrandSearchType.ALL) {
            Integer currentPage = (Integer) searchValue.get("currentPage");
            Integer pageSize = (Integer) searchValue.get("pageSize");
            Page<Brand> page = new Page<>(currentPage, pageSize);
            return brandDao.selectPage(page, null).getRecords();
        } else if (searchType == BrandSearchType.SEARCH_CID) {
            Integer cid = (Integer) searchValue.get("cid");
            return brandDao.searchBrandByCid(cid);
        }
        return List.of();
    }

    @Override
    public Long getTotalCount() {
        return brandDao.selectCount(null);
    }

    @Override
    public List<Integer> searchSelectedCategoryId(Integer bid) {
        return brandDao.searchSelectedCategoryId(bid);
    }

    @Override
    public void brandCategoryChange(Integer bid, Integer cid, Boolean selected) {
        if (selected) {
            if (brandDao.insertCategoryBrand(bid, cid) != 1) {
                throw new SystemException(ResponseCode.ERROR);
            }
            return;
        }
        if (brandDao.deleteCategoryBrand(bid, cid) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }

    @Override
    public void addBrand(Brand brand) {
        if (brandDao.insert(brand) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }

    @CacheEvict(cacheNames = "brand", key = "#brand.id", beforeInvocation = true)
    @Override
    public void updateBrand(Brand brand) {
        brand.setUpdateTime(LocalDateTime.now());
        if (brandDao.updateById(brand) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }
}
