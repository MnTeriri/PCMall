package com.example.pcmallproviderstorage.controller;

import com.example.pcmallcommon.model.Storage;
import com.example.pcmallcommon.response.ResponseResult;
import com.example.pcmallproviderstorage.service.IStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/storage")
@Tag(name = "storage参数")
@RequiredArgsConstructor
public class StorageController {

    private final IStorageService storageService;

    @PostMapping("/getStorageList")
    @Operation(summary = "查询商品全部库存信息")
    @Parameters({
            @Parameter(name = "gid", description = "商品ID", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "currentPage", description = "当前页数", required = true, in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "页面大小", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<List<Storage>> getStorageList(Integer gid, Integer currentPage, Integer pageSize) {
        return ResponseResult.ok(storageService.getStorageList(gid, currentPage, pageSize));
    }

    @PostMapping("/getTotalCount")
    @Operation(summary = "查询商品库存信息总个数")
    @Parameters({
            @Parameter(name = "gid", description = "商品ID", required = true, in = ParameterIn.QUERY)
    })
    public ResponseResult<Long> getTotalCount(Integer gid) {
        return ResponseResult.ok(storageService.getTotalCount(gid));
    }

    @PostMapping("/inboundDelivery")
    @Operation(summary = "入库操作")
    public ResponseResult<String> inboundDelivery(@RequestBody Storage storage) {
        storageService.inboundDelivery(storage);
        return ResponseResult.ok();
    }

    @PostMapping("/outboundDelivery")
    @Operation(summary = "出库操作")
    public ResponseResult<String> outboundDelivery(@RequestBody Storage storage) {
        storageService.outboundDelivery(storage);
        return ResponseResult.ok();
    }
}
