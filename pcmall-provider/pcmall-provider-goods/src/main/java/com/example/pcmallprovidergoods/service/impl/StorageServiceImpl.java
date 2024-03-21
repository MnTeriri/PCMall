package com.example.pcmallprovidergoods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.model.Storage;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallprovidergoods.dao.IStorageDao;
import com.example.pcmallprovidergoods.service.IGoodsService;
import com.example.pcmallprovidergoods.service.IStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class StorageServiceImpl implements IStorageService {
    @Autowired
    private IStorageDao storageDao;
    @Autowired
    private IGoodsService goodsService;

    public StorageServiceImpl() {
        log.debug("创建Service对象：StorageServiceImpl");
    }

    @Override
    public List<Storage> getStorageList(Integer gid, Integer currentPage, Integer pageSize) {
        QueryWrapper<Storage> queryWrapper = new QueryWrapper<Storage>().eq("gid", gid);
        Page<Storage> page = new Page<>(currentPage, pageSize);
        return storageDao.selectPage(page, queryWrapper).getRecords();
    }

    @Override
    public Long getTotalCount(Integer gid) {
        QueryWrapper<Storage> queryWrapper = new QueryWrapper<Storage>().eq("gid", gid);
        return storageDao.selectCount(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void inboundDelivery(Storage storage) {
        Goods goods = goodsService.searchGoods(storage.getGid());
        if (goods == null) {
            throw new SystemException(ResponseCode.ENTITY_NOT_FOUND);
        }
        if (goods.getStatus() == 1) {
            goods.setStatus(0);//设置为正常
        }
        goods.setCount(goods.getCount() + storage.getCount());
        goodsService.updateGoods(goods);
        if (storageDao.insert(storage) != 1) {
            throw new SystemException(ResponseCode.ERROR);//库存信息插入失败
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void outboundDelivery(Storage storage) {
        Goods goods = goodsService.searchGoods(storage.getGid());
        if (goods == null) {
            throw new SystemException(ResponseCode.ENTITY_NOT_FOUND);
        }
        if (goods.getCount() < storage.getCount()) {
            throw new SystemException(ResponseCode.GOODS_NOT_ENOUGH_ERROR);//库存不足
        }
        goods.setCount(goods.getCount() - storage.getCount());
        if (goods.getCount() == 0) {
            goods.setStatus(1);//设置为缺货
        }
        goodsService.updateGoods(goods);
        storage.setCount(-storage.getCount());
        if (storageDao.insert(storage) != 1) {
            throw new SystemException(ResponseCode.ERROR);//库存信息插入失败
        }
    }
}
