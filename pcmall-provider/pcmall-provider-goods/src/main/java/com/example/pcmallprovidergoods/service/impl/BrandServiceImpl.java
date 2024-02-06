package com.example.pcmallprovidergoods.service.impl;

import com.example.pcmallprovidergoods.dao.IBrandDao;
import com.example.pcmallprovidergoods.service.IBrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BrandServiceImpl implements IBrandService {
    @Autowired
    private IBrandDao brandDao;

    public BrandServiceImpl() {
        log.debug("创建Service对象：BrandServiceImpl");
    }
}
