package com.example.pcmallprovidergoods.service.impl;

import com.example.pcmallcommon.model.Goods;
import com.example.pcmallprovidergoods.dao.IGoodsDao;
import com.example.pcmallprovidergoods.service.IGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class GoodsServiceImpl implements IGoodsService {
    @Autowired
    private IGoodsDao goodsDao;

    public GoodsServiceImpl() {
        log.debug("创建Service对象：GoodsServiceImpl");
    }

    @Override
    public List<Goods> getGoodsList(Integer currentPage, Integer pageSize) {
        return goodsDao.getGoodsList((currentPage - 1) * pageSize, pageSize);
    }

    @Override
    public Long getTotalCount() {
        return goodsDao.selectCount(null);
    }

    @Override
    public Integer addGoods(Goods goods) {
        return goodsDao.insert(goods);
    }

    @Override
    public Integer updateGoods(Goods goods) {
        goods.setUpdateTime(LocalDateTime.now());
        return goodsDao.updateById(goods);
    }
}
