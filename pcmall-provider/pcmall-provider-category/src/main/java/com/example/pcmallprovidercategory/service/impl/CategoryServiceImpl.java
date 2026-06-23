package com.example.pcmallprovidercategory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.dto.Category;
import com.example.pcmallcommon.model.entity.CategoryEntity;
import com.example.pcmallcommon.model.mapper.CategoryMapper;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallprovidercategory.dao.ICategoryDao;
import com.example.pcmallprovidercategory.service.ICategoryService;
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
public class CategoryServiceImpl implements ICategoryService {

    private final ICategoryDao categoryDao;

    @Cacheable(cacheNames = "category", key = "#id", sync = true)
    @Override
    public Category searchCategoryById(Integer id) {
        CategoryEntity entity = categoryDao.selectById(id);
        return CategoryMapper.INSTANCE.toDto(entity);
    }

    @Override
    public List<Category> searchCategoryList(CategorySearchType searchType, Map<String, Object> searchValue) {
        if (searchType == CategorySearchType.ALL) {
            Integer currentPage = (Integer) searchValue.get("currentPage");
            Integer pageSize = (Integer) searchValue.get("pageSize");
            Page<CategoryEntity> page = new Page<>(currentPage, pageSize);
            List<CategoryEntity> list = categoryDao.selectPage(page, null).getRecords();
            return CategoryMapper.INSTANCE.toDtoList(list);
        } else if (searchType == CategorySearchType.SEARCH_NOT_DELETE) {
            List<CategoryEntity> list = categoryDao.selectList(new QueryWrapper<CategoryEntity>().eq("is_delete", 0));
            return CategoryMapper.INSTANCE.toDtoList(list);
        }
        return List.of();
    }

    @Override
    public Long getTotalCount() {
        return categoryDao.selectCount(null);
    }

    @Override
    public void addCategory(Category category) {
        CategoryEntity entity = CategoryMapper.INSTANCE.toEntity(category);
        if (categoryDao.insert(entity) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }

    @CacheEvict(cacheNames = "category", key = "#category.id", beforeInvocation = true)
    @Override
    public void updateCategory(Category category) {
        category.setUpdateTime(LocalDateTime.now());
        CategoryEntity entity = CategoryMapper.INSTANCE.toEntity(category);
        if (categoryDao.updateById(entity) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }
}
