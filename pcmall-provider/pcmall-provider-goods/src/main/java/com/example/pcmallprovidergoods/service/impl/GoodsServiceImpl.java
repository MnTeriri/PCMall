package com.example.pcmallprovidergoods.service.impl;

import com.example.pcmallprovidergoods.dao.IGoodsDao;
import com.example.pcmallprovidergoods.service.IGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoodsServiceImpl implements IGoodsService {
    @Autowired
    private IGoodsDao goodsDao;

    public GoodsServiceImpl() {
        log.debug("创建Service对象：GoodsServiceImpl");
    }
}
