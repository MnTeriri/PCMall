package com.example.pcmallprovidergoods.service;

import com.example.pcmallcommon.model.Goods;

import java.util.List;
import java.util.Map;

public interface IGoodsService {
    /**
     * isSearchCategory：是否搜索商品分类、isSearchBrand：是否搜索商品品牌
     */
    Goods searchGoodsById(Integer id, Boolean isSearchCategory, Boolean isSearchBrand);

    /**
     * 搜索全部商品信息，使用分页
     */
    List<Goods> searchGoodsList(Integer currentPage, Integer pageSize);

    /**
     * 搜索状态正常商品，并使用searchValue模糊查询，使用分页
     */
    List<Goods> searchGoodsList(String searchValue, Integer currentPage, Integer pageSize);

    /**
     * 使用cid和bid搜索状态正常商品，使用分页
     */
    List<Goods> searchGoodsList(Integer bid, Integer cid, Integer currentPage, Integer pageSize);

    Long getTotalCount();

    Long getTotalCount(String searchValue);

    Long getTotalCount(Integer bid, Integer cid);

    void addGoods(Goods goods);

    void updateGoods(Goods goods);

    void updateGoodsStatus(Goods goods);

    void addGoodsCount(Integer id, Integer count);

    void divGoodsCount(Integer id, Integer count);
}
