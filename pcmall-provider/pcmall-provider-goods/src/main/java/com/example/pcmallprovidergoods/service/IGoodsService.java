package com.example.pcmallprovidergoods.service;

import com.example.pcmallcommon.model.Goods;

import java.util.List;

public interface IGoodsService {
    public List<Goods> getGoodsList(Integer currentPage, Integer pageSize);

    public List<Goods> searchGoodsList(Integer currentPage, Integer pageSize);

    public Long getTotalCount();//所有

    public Long searchTotalCount();//状态正常的数量

    public Goods searchGoods(Integer id);

    public void addGoods(Goods goods);

    public void updateGoods(Goods goods);

    public void updateGoodsStatus(Goods goods);
}
