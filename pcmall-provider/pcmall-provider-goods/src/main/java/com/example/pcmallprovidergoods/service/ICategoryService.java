package com.example.pcmallprovidergoods.service;

import com.example.pcmallcommon.model.Category;

import java.util.List;

public interface ICategoryService {
    public List<Category> getCategoryList(Integer currentPage, Integer pageSize);

    public Long getTotalCount();

    public Integer addCategory(Category category);

    public Integer updateCategory(Category category);
//    public List<Category> getNotDeleteCategoryList();
}
