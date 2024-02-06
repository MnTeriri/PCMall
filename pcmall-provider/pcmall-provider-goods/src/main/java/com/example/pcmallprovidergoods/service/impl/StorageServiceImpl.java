package com.example.pcmallprovidergoods.service.impl;

import com.example.pcmallprovidergoods.dao.IStorageDao;
import com.example.pcmallprovidergoods.service.IStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StorageServiceImpl implements IStorageService {
    @Autowired
    private IStorageDao storageDao;

    public StorageServiceImpl() {
        log.debug("创建Service对象：StorageServiceImpl");
    }
}
