package com.example.pcmallprovidergoods.service;

import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.model.dto.GoodsAiSearchRequest;

import java.util.List;

public interface IGoodsService {
    /**
     * isSearchCategory：是否搜索商品分类、isSearchBrand：是否搜索商品品牌
     */
    Goods searchGoodsById(Integer id, Boolean isSearchCategory, Boolean isSearchBrand);

    /**
     * 搜索全部商品信息，使用分页
     */
    List<Goods> searchAllGoods(Integer currentPage, Integer pageSize);

    /**
     * 搜索状态正常商品，并使用searchValue模糊查询，使用分页
     */
    List<Goods> searchGoodsByValue(String searchValue, Integer currentPage, Integer pageSize);

    /**
     * 使用cid和bid搜索状态正常商品，使用分页
     */
    List<Goods> searchGoodsByCidAndBid(Integer bid, Integer cid, Integer currentPage, Integer pageSize);

    List<Goods> searchGoodsByAiIntent(GoodsAiSearchRequest aiSearchRequest);

    Long getTotalCount();

    Long getTotalCountByValue(String searchValue);

    Long getTotalCountByCidAndBid(Integer bid, Integer cid);

    void addGoods(Goods goods);

    void updateGoods(Goods goods);

    void updateGoodsStatus(Goods goods);

    void deleteGoods(Integer id);

    void recoverGoods(Integer id);

    void addGoodsCount(Integer id, Integer count);

    void divGoodsCount(Integer id, Integer count);
}
