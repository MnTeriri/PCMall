package com.example.pcmallprovidergoods.service;

import com.example.pcmallcommon.model.Goods;

import java.util.List;
import java.util.Map;

public interface IGoodsService {
    /**
     * aspectRule：null时不增强，（isSearchCategory：是否搜索商品分类）、（isSearchBrand：是否搜索商品品牌）<br>
     */
    Goods searchGoodsById(Map<String, Boolean> aspectRule, Integer id);

    /**
     * aspectRule：null时不增强，（isSearchCategory：是否搜索商品分类）、（isSearchBrand：是否搜索商品品牌）<br>
     * searchType：搜索类型枚举<br>
     * searchValue：（searchValue(String)：搜索值）、（cid(Integer)：分类号）、（bid(Integer)：品牌号）、（currentPage(Integer)：当前页数）、（pageSize(Integer)：页面大小）
     */
    List<Goods> searchGoodsList(Map<String, Boolean> aspectRule, GoodsSearchType searchType, Map<String, Object> searchValue);

    Long getTotalCount(GoodsSearchType searchType, Map<String, Object> searchValue);

    void addGoods(Goods goods);

    void updateGoods(Goods goods);

    void updateGoodsStatus(Goods goods);

    void addGoodsCount(Integer id, Integer count);

    void divGoodsCount(Integer id, Integer count);

    /**
     * ALL：搜索全部商品信息，使用分页<br>
     * SEARCH：搜索状态正常商品，并使用searchValue模糊查询，使用分页<br>
     * SEARCH_BY_CID_AND_BID：使用cid和bid搜索状态正常商品，使用分页
     */
    enum GoodsSearchType {
        ALL, SEARCH, SEARCH_BY_CID_AND_BID
    }
}
