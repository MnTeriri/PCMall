package com.example.pcmallprovidergoods.service;

import com.example.pcmallcommon.model.Goods;

import java.util.List;

public interface IGoodsService {
    public List<Goods> getGoodsList(Integer currentPage, Integer pageSize);

    public List<Goods> searchGoodsList(String searchValue, Integer currentPage, Integer pageSize);

    public List<Goods> searchGoodsByCidAndBid(Integer cid, Integer bid, Integer currentPage, Integer pageSize);

    public Long getTotalCount();//所有

    public Long getRecordsFiltered(String searchValue);//状态正常并且筛选的数量

    public Long getRecordsFilteredByCidAndBid(Integer cid, Integer bid);//状态正常并且通过cid和bid筛选

    public Goods searchGoods(Integer id);

    public void addGoods(Goods goods);

    public void updateGoods(Goods goods);

    public void updateGoodsStatus(Goods goods);
}
