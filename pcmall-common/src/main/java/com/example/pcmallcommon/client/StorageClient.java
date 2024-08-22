package com.example.pcmallcommon.client;

import com.example.pcmallcommon.model.Storage;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "storageClient", value = "pcmall-provider-storage")
public interface StorageClient {
    @PostMapping("/storage/getStorageList")
    ResponseResult<List<Storage>> getStorageList(
            @RequestParam("gid") Integer gid,
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize);

    @PostMapping("/storage/getTotalCount")
    ResponseResult<Long> getTotalCount(@RequestParam("gid") Integer gid);

    @PostMapping("/storage/inboundDelivery")
    ResponseResult<String> inboundDelivery(@RequestBody Storage storage);

    @PostMapping("/storage/outboundDelivery")
    ResponseResult<String> outboundDelivery(@RequestBody Storage storage);

}
