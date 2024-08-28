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
import io.seata.spring.annotation.GlobalLock;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    //@Cacheable(cacheNames = "storage", key = "#gid+':'+#currentPage+':'+#pageSize", sync = true)
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

    //@CacheEvict(cacheNames = "storage", allEntries = true)
    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public void inboundDelivery(Storage storage) {
        goodsClient.addGoodsCount(storage.getGid(), storage.getCount());
        if (storageDao.insert(storage) != 1) {
            throw new SystemException(ResponseCode.ERROR);//库存信息插入失败
        }
    }

    //@CacheEvict(cacheNames = "storage", allEntries = true)
    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public void outboundDelivery(Storage storage) {
        goodsClient.divGoodsCount(storage.getGid(), storage.getCount());
        storage.setCount(-storage.getCount());
        if (storageDao.insert(storage) != 1) {
            throw new SystemException(ResponseCode.ERROR);//库存信息插入失败
        }
    }
}
