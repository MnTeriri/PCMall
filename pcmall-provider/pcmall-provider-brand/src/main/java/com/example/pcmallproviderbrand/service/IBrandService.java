package com.example.pcmallproviderbrand.service;

import com.example.pcmallcommon.model.Brand;

import java.util.List;
import java.util.Map;

public interface IBrandService {
    Brand searchBrandById(Integer id);

    /**
     * searchType：搜索类型枚举<br>
     * searchValue：（cid(Integer)：分类号）、（currentPage(Integer)：当前页数）、（pageSize(Integer)：页面大小）
     */
    List<Brand> searchBrandList(BrandSearchType searchType, Map<String, Object> searchValue);

    Long getTotalCount();

    List<Integer> searchSelectedCategoryId(Integer bid);

    void brandCategoryChange(Integer bid, Integer cid, Boolean selected);

    void addBrand(Brand brand);

    void updateBrand(Brand brand);

    /**
     * ALL：搜索全部品牌信息，使用分页<br>
     * SEARCH_CID：搜索分类号为CID并且没被删除的品牌信息，不使用分页
     */
    enum BrandSearchType {
        ALL, SEARCH_CID
    }

}
