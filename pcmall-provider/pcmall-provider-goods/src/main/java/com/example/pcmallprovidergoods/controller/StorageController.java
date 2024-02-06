package com.example.pcmallprovidergoods.controller;

import com.example.pcmallprovidergoods.service.IStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/storage")
public class StorageController {
    @Autowired
    private IStorageService storageService;

    public StorageController() {
        log.debug("创建Controller对象：StorageController");
    }
}
