package com.example.pcmallproviderstorage.controller;

import com.example.pcmallcommon.model.Storage;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallproviderstorage.service.IStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/storage")
public class StorageController {
    @Autowired
    private IStorageService storageService;

    public StorageController() {
        log.debug("创建Controller对象：{}", this);
    }

    @PostMapping("/getStorageList")
    public ResponseResult<List<Storage>> getStorageList(Integer gid, Integer currentPage, Integer pageSize) {
        return ResponseResult.ok(storageService.getStorageList(gid, currentPage, pageSize));
    }

    @PostMapping("/getTotalCount")
    public ResponseResult<Long> getTotalCount(Integer gid) {
        return ResponseResult.ok(storageService.getTotalCount(gid));
    }

    @PostMapping("/inboundDelivery")
    public ResponseResult<String> inboundDelivery(@RequestBody Storage storage) {
        storageService.inboundDelivery(storage);
        return ResponseResult.ok();
    }

    @PostMapping("/outboundDelivery")
    public ResponseResult<String> outboundDelivery(@RequestBody Storage storage) {
        storageService.outboundDelivery(storage);
        return ResponseResult.ok();
    }
}
