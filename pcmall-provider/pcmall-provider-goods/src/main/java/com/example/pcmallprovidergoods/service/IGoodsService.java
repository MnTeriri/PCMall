package com.example.pcmallprovidergoods.service;

import com.example.pcmallcommon.model.Goods;

import java.util.List;

public interface IGoodsService {
    public List<Goods> getGoodsList(Integer currentPage, Integer pageSize);

    public Long getTotalCount();

    public Integer addGoods(Goods goods);

    public Integer updateGoods(Goods goods);
}
