package com.example.pcmallprovidergoods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pcmallcommon.model.Category;
import com.example.pcmallprovidergoods.dao.ICategoryDao;
import com.example.pcmallprovidergoods.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private ICategoryDao categoryDao;

    public CategoryServiceImpl() {
        log.debug("创建Service对象：CategoryServiceImpl");
    }

    @Override
    public List<Category> getCategoryList(Integer currentPage, Integer pageSize) {
        Page<Category> page = new Page<>(currentPage, pageSize);
        return categoryDao.selectPage(page, null).getRecords();
    }

    @Override
    public Long getTotalCount() {
        return categoryDao.selectCount(null);
    }

    @Override
    public List<Category> searchCategory() {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<Category>().eq("is_delete", 0);
        return categoryDao.selectList(queryWrapper);
    }

    @Override
    public Integer addCategory(Category category) {
        return categoryDao.insert(category);
    }

    @Override
    public Integer updateCategory(Category category) {
        category.setUpdateTime(LocalDateTime.now());
        return categoryDao.updateById(category);
    }
}
