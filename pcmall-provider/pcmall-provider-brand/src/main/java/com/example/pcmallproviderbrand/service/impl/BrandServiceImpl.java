package com.example.pcmallproviderbrand.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.dto.Brand;
import com.example.pcmallcommon.model.entity.BrandEntity;
import com.example.pcmallcommon.model.mapper.BrandMapper;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallproviderbrand.dao.IBrandDao;
import com.example.pcmallproviderbrand.service.IBrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements IBrandService {

    private final IBrandDao brandDao;

    @Cacheable(cacheNames = "brand", key = "#id", sync = true)
    @Override
    public Brand searchBrandById(Integer id) {
        return BrandMapper.INSTANCE.toDto(brandDao.selectById(id));
    }

    @Override
    public List<Brand> searchBrandList(BrandSearchType searchType, Map<String, Object> searchValue) {
        if (searchType == BrandSearchType.ALL) {
            Integer currentPage = (Integer) searchValue.get("currentPage");
            Integer pageSize = (Integer) searchValue.get("pageSize");
            Page<BrandEntity> page = new Page<>(currentPage, pageSize);
            List<BrandEntity> list = brandDao.selectPage(page, null).getRecords();
            return BrandMapper.INSTANCE.toDtoList(list);
        } else if (searchType == BrandSearchType.SEARCH_CID) {
            Integer cid = (Integer) searchValue.get("cid");
            List<BrandEntity> list = brandDao.searchBrandByCid(cid);
            return BrandMapper.INSTANCE.toDtoList(list);
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
        BrandEntity entity = BrandMapper.INSTANCE.toEntity(brand);
        if (brandDao.insert(entity) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }

    @CacheEvict(cacheNames = "brand", key = "#brand.id", beforeInvocation = true)
    @Override
    public void updateBrand(Brand brand) {
        brand.setUpdateTime(LocalDateTime.now());
        BrandEntity entity = BrandMapper.INSTANCE.toEntity(brand);
        if (brandDao.updateById(entity) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }
}
