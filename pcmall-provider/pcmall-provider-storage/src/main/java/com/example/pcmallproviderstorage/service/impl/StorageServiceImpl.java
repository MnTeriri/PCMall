package com.example.pcmallproviderstorage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pcmallcommon.client.GoodsClient;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.dto.Storage;
import com.example.pcmallcommon.model.entity.StorageEntity;
import com.example.pcmallcommon.model.mapper.StorageMapper;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallproviderstorage.dao.IStorageDao;
import com.example.pcmallproviderstorage.service.IStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements IStorageService {

    private final IStorageDao storageDao;

    private final GoodsClient goodsClient;

    //@Cacheable(cacheNames = "storage", key = "#gid+':'+#currentPage+':'+#pageSize", sync = true)
    @Override
    public List<Storage> getStorageList(Integer gid, Integer currentPage, Integer pageSize) {
        QueryWrapper<StorageEntity> queryWrapper = new QueryWrapper<StorageEntity>()
                .eq("gid", gid)
                .orderByDesc("id");
        Page<StorageEntity> page = new Page<>(currentPage, pageSize);
        List<StorageEntity> list = storageDao.selectPage(page, queryWrapper).getRecords();
        return StorageMapper.INSTANCE.toDtoList(list);
    }

    @Override
    public Long getTotalCount(Integer gid) {
        QueryWrapper<StorageEntity> queryWrapper = new QueryWrapper<StorageEntity>().eq("gid", gid);
        return storageDao.selectCount(queryWrapper);
    }

    //@CacheEvict(cacheNames = "storage", allEntries = true)
    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public void inboundDelivery(Storage storage) {
        goodsClient.addGoodsCount(storage.getGid(), storage.getCount());
        StorageEntity entity = StorageMapper.INSTANCE.toEntity(storage);
        if (storageDao.insert(entity) != 1) {
            throw new SystemException(ResponseCode.ERROR);//库存信息插入失败
        }
    }

    //@CacheEvict(cacheNames = "storage", allEntries = true)
    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public void outboundDelivery(Storage storage) {
        goodsClient.divGoodsCount(storage.getGid(), storage.getCount());
        storage.setCount(-storage.getCount());
        StorageEntity entity = StorageMapper.INSTANCE.toEntity(storage);
        if (storageDao.insert(entity) != 1) {
            throw new SystemException(ResponseCode.ERROR);//库存信息插入失败
        }
    }
}
