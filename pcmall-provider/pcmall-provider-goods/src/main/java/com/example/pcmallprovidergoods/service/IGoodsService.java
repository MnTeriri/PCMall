package com.example.pcmallprovidergoods.service;

import com.example.pcmallcommon.model.Goods;

import java.util.List;

public interface IGoodsService {
    public List<Goods> getGoodsList(Integer currentPage, Integer pageSize);

    public Goods searchGoodsById(Integer id);

    public Long getTotalCount();

    public void addGoods(Goods goods);

    public void updateGoods(Goods goods);

    public void updateGoodsStatus(Goods goods);
}
