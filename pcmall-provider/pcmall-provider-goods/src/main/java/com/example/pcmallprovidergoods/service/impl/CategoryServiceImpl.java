package com.example.pcmallprovidergoods.service.impl;

import com.example.pcmallprovidergoods.dao.ICategoryDao;
import com.example.pcmallprovidergoods.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private ICategoryDao categoryDao;

    public CategoryServiceImpl() {
        log.debug("创建Service对象：CategoryServiceImpl");
    }
}
