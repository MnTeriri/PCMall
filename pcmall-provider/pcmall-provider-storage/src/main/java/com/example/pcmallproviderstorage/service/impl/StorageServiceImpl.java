package com.example.pcmallproviderstorage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pcmallcommon.client.GoodsClient;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.model.Storage;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallproviderstorage.dao.IStorageDao;
import com.example.pcmallproviderstorage.service.IStorageService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class StorageServiceImpl implements IStorageService {
    @Autowired
    private IStorageDao storageDao;
    @Autowired
    private GoodsClient goodsClient;

    public StorageServiceImpl() {
        log.debug("创建Service对象：{}", this);
    }

    @Override
    public List<Storage> getStorageList(Integer gid, Integer currentPage, Integer pageSize) {
        QueryWrapper<Storage> queryWrapper = new QueryWrapper<Storage>()
                .eq("gid", gid)
                .orderByDesc("id");
        Page<Storage> page = new Page<>(currentPage, pageSize);
        return storageDao.selectPage(page, queryWrapper).getRecords();
    }

    @Override
    public Long getTotalCount(Integer gid) {
        QueryWrapper<Storage> queryWrapper = new QueryWrapper<Storage>().eq("gid", gid);
        return storageDao.selectCount(queryWrapper);
    }

    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public void inboundDelivery(Storage storage) {
        Goods goods = goodsClient.searchGoodsById(new HashMap<>(), storage.getGid()).getData();
        if (goods == null) {
            throw new SystemException(ResponseCode.ENTITY_NOT_FOUND);//不存在该商品
        }
        if (goods.getStatus() == 1) {
            goods.setStatus(0);//如果商品状态为缺货，设置为正常
        }
        goods.setCount(goods.getCount() + storage.getCount());
        goodsClient.updateGoods(goods);//更新商品数量
        if (storageDao.insert(storage) != 1) {
            throw new SystemException(ResponseCode.ERROR);//库存信息插入失败
        }
    }

    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public void outboundDelivery(Storage storage) {
        Goods goods = goodsClient.searchGoodsById(new HashMap<>(), storage.getGid()).getData();
        if (goods == null) {
            throw new SystemException(ResponseCode.ENTITY_NOT_FOUND);//不存在该商品
        }
        if (goods.getCount() < storage.getCount()) {
            throw new SystemException(ResponseCode.GOODS_NOT_ENOUGH_ERROR);//库存不足
        }
        goods.setCount(goods.getCount() - storage.getCount());
        if (goods.getCount() == 0) {
            goods.setStatus(1);//如果商品数量为0，设置状态为缺货
        }
        goodsClient.updateGoods(goods);//更新商品数量
        storage.setCount(-storage.getCount());
        if (storageDao.insert(storage) != 1) {
            throw new SystemException(ResponseCode.ERROR);//库存信息插入失败
        }
    }
}
