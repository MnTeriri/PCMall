package com.example.pcmallai.controller;

import com.example.pcmallai.model.GoodsEmbedProgress;
import com.example.pcmallai.service.impl.GoodsKnowledgeServiceImpl;
import com.example.pcmallcommon.response.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/ai/goods-knowledge")
@RequiredArgsConstructor
@Tag(name = "商品动态知识库接口")
public class GoodsKnowledgeController {

    private final GoodsKnowledgeServiceImpl goodsKnowledgeService;

    @Operation(summary = "启动全量初始化（异步执行）")
    @PostMapping("/fullRefresh")
    public ResponseResult<String> fullRefresh() {
        goodsKnowledgeService.fullRefresh();
        return ResponseResult.ok(null, "动态知识库全量刷新任务已提交，通过 /status 查看进度");
    }

    @Operation(summary = "停止正在运行的初始化")
    @PostMapping("/stopRefresh")
    public ResponseResult<String> stopRefresh() {
        goodsKnowledgeService.stopRefresh();
        return ResponseResult.ok(null, "已发送停止信号，当前批次处理完成后将停止");
    }

    @Operation(summary = "查询初始化进度")
    @GetMapping("/status")
    public ResponseResult<GoodsEmbedProgress> status() {
        GoodsEmbedProgress progress = goodsKnowledgeService.getProgress();
        return ResponseResult.ok(progress);
    }
}