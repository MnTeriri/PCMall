package com.example.pcmallprovidercategory.service;

import com.example.pcmallcommon.model.dto.Category;

import java.util.List;
import java.util.Map;

public interface ICategoryService {
    Category searchCategoryById(Integer id);

    /**
     * searchType：搜索类型枚举<br>
     * searchValue：（currentPage(Integer)：当前页数）、（pageSize(Integer)：页面大小）
     */
    List<Category> searchCategoryList(CategorySearchType searchType, Map<String, Object> searchValue);

    Long getTotalCount();

    void addCategory(Category category);

    void updateCategory(Category category);

    /**
     * ALL：搜索全部分类信息，使用分页<br>
     * SEARCH_NOT_DELETE：搜索未删除的分类信息，不使用分页
     */
    enum CategorySearchType {
        ALL, SEARCH_NOT_DELETE
    }
}
