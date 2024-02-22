package com.example.pcmallprovidergoods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.response.ResponseCode;
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
    public Goods searchGoodsById(Integer id) {
        return goodsDao.searchGoodsById(id);
    }

    @Override
    public Long getTotalCount() {
        return goodsDao.selectCount(null);
    }

    @Override
    public void addGoods(Goods goods) {
        if (goodsDao.insert(goods) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }

    @Override
    public void updateGoods(Goods goods) {
        goods.setUpdateTime(LocalDateTime.now());
        if (goodsDao.updateById(goods) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }

    @Override
    public void updateGoodsStatus(Goods goods) {
        goods.setUpdateTime(LocalDateTime.now());
        Goods searched = searchGoodsById(goods.getId());
        if (goods.getStatus() == 0 && searched.getCount() == 0) {
            //上架操作如果商品没货，设置为缺货
            goods.setStatus(1);
        }
        updateGoods(goods);
    }
}
