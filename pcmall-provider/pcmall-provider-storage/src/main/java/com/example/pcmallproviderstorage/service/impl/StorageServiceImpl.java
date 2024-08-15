package com.example.pcmallproviderstorage.service.impl;

import com.example.pcmallcommon.model.Storage;
import com.example.pcmallproviderstorage.dao.IStorageDao;
import com.example.pcmallproviderstorage.service.IStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StorageServiceImpl implements IStorageService {
    @Autowired
    private IStorageDao storageDao;

    public StorageServiceImpl() {
        log.debug("创建Service对象：{}", this);
    }

    @Override
    public List<Storage> getStorageList(Integer gid, Integer currentPage, Integer pageSize) {
        return List.of();
    }

    @Override
    public Long getTotalCount(Integer gid) {
        return 0L;
    }

    @Override
    public void inboundDelivery(Storage storage) {

    }

    @Override
    public void outboundDelivery(Storage storage) {

    }
}
