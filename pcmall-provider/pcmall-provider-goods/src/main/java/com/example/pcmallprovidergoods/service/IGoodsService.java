package com.example.pcmallprovidergoods.service;

import com.example.pcmallcommon.model.Goods;

import java.util.List;

public interface IGoodsService {
    List<Goods> getGoodsList(Integer currentPage, Integer pageSize);

    List<Goods> searchGoodsList(String searchValue, Integer currentPage, Integer pageSize);

    List<Goods> searchGoodsByCidAndBid(Integer cid, Integer bid, Integer currentPage, Integer pageSize);

    Long getTotalCount();//所有

    Long getRecordsFiltered(String searchValue);//状态正常并且筛选的数量

    Long getRecordsFilteredByCidAndBid(Integer cid, Integer bid);//状态正常并且通过cid和bid筛选

    Goods searchGoods(Integer id);

    void addGoods(Goods goods);

    void updateGoods(Goods goods);

    void updateGoodsStatus(Goods goods);
}
