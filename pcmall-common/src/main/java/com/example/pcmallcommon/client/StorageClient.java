package com.example.pcmallcommon.client;

import com.example.pcmallcommon.model.Storage;
import com.example.pcmallcommon.response.ResponseResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange("/storage")
public interface StorageClient {
    @PostExchange("/getStorageList")
    ResponseResult<List<Storage>> getStorageList(
            @RequestParam("gid") Integer gid,
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam("pageSize") Integer pageSize
    );

    @PostExchange("/getTotalCount")
    ResponseResult<Long> getTotalCount(@RequestParam("gid") Integer gid);

    @PostExchange("/inboundDelivery")
    ResponseResult<String> inboundDelivery(@RequestBody Storage storage);

    @PostExchange("/outboundDelivery")
    ResponseResult<String> outboundDelivery(@RequestBody Storage storage);

}
