package com.example.pcmallprovidergoods.service;

import com.example.pcmallcommon.model.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getCategoryList(Integer currentPage, Integer pageSize);

    Long getTotalCount();

    List<Category> searchCategoryList();

    Integer addCategory(Category category);

    Integer updateCategory(Category category);
}
