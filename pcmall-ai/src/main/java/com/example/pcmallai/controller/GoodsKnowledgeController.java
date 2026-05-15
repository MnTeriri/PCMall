package com.example.pcmallai.controller;

import com.example.pcmallai.model.GoodsEmbedProgress;
import com.example.pcmallai.service.GoodsKnowledgeService;
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

    private final GoodsKnowledgeService goodsKnowledgeService;

    @Operation(summary = "启动全量初始化（异步执行）")
    @PostMapping("/init")
    public ResponseResult<String> init() {
        log.info("收到商品向量化全量初始化请求");
        goodsKnowledgeService.startInit();
        return ResponseResult.ok(null, "初始化任务已触发，通过 /init/status 查看进度");
    }

    @Operation(summary = "查询初始化进度")
    @GetMapping("/init/status")
    public ResponseResult<GoodsEmbedProgress> status() {
        GoodsEmbedProgress progress = goodsKnowledgeService.getProgress();
        return ResponseResult.ok(progress);
    }

    @Operation(summary = "停止正在运行的初始化")
    @PostMapping("/init/stop")
    public ResponseResult<String> stop() {
        log.info("收到停止初始化请求");
        goodsKnowledgeService.stopInit();
        return ResponseResult.ok(null, "已发送停止信号，当前批次处理完成后将停止");
    }
}