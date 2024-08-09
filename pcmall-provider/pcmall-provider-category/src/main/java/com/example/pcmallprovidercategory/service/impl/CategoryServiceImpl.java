package com.example.pcmallprovidercategory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.Category;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallprovidercategory.dao.ICategoryDao;
import com.example.pcmallprovidercategory.service.ICategoryService;
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
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private ICategoryDao categoryDao;

    public CategoryServiceImpl() {
        log.debug("创建Service对象：{}", this);
    }

    @Cacheable(cacheNames = "category", key = "#id", sync = true)
    @Override
    public Category searchCategoryById(Integer id) {
        return categoryDao.selectById(id);
    }

    @Override
    public List<Category> searchCategoryList(CategorySearchType searchType, Map<String, Object> searchValue) {
        if (searchType == CategorySearchType.ALL) {
            Integer currentPage = (Integer) searchValue.get("currentPage");
            Integer pageSize = (Integer) searchValue.get("pageSize");
            Page<Category> page = new Page<>(currentPage, pageSize);
            return categoryDao.selectPage(page, null).getRecords();
        } else if (searchType == CategorySearchType.SEARCH_NOT_DELETE) {
            return categoryDao.selectList(new QueryWrapper<Category>().eq("is_delete", 0));
        }
        return List.of();
    }

    @Override
    public Long getTotalCount() {
        return categoryDao.selectCount(null);
    }

    @Override
    public void addCategory(Category category) {
        if (categoryDao.insert(category) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }

    @CacheEvict(cacheNames = "category", key = "#category.id", beforeInvocation = true)
    @Override
    public void updateCategory(Category category) {
        category.setUpdateTime(LocalDateTime.now());
        if (categoryDao.updateById(category) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }
}
