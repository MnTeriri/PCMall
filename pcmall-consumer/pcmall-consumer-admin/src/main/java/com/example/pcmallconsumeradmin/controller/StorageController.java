package com.example.pcmallconsumeradmin.controller;

import com.example.pcmallcommon.client.StorageClient;
import com.example.pcmallcommon.model.dto.Storage;
import com.example.pcmallcommon.response.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/storage")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class StorageController {

    private final StorageClient storageClient;

    @PostMapping("/getStorageList")
    public ResponseResult<List<Storage>> getStorageList(
            Integer gid,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        return storageClient.getStorageList(gid, currentPage, pageSize);
    }

    @PostMapping("/getTotalCount")
    public ResponseResult<Long> getTotalCount(Integer gid) {
        return storageClient.getTotalCount(gid);
    }

    @PostMapping("/inboundDelivery")
    public ResponseResult<String> inboundDelivery(Storage storage) {
        return storageClient.inboundDelivery(storage);
    }

    @PostMapping("/outboundDelivery")
    public ResponseResult<String> outboundDelivery(Storage storage) {
        return storageClient.outboundDelivery(storage);
    }
}
