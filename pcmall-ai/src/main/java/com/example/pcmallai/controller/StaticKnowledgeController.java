package com.example.pcmallai.controller;

import com.example.pcmallai.service.StaticKnowledgeService;
import com.example.pcmallcommon.response.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ai/static-knowledge")
@RequiredArgsConstructor
@Tag(name = "静态知识库接口")
public class StaticKnowledgeController {
    private final StaticKnowledgeService staticKnowledgeService;

    @Operation(summary = "上传静态知识文件")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseResult<List<String>> upload(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(defaultValue = "false") Boolean refreshNow
    ) throws IOException {
        List<String> savedFileNames = staticKnowledgeService.saveUploadedFiles(files);
        if (refreshNow) {
            staticKnowledgeService.refreshDocument();
        }
        return ResponseResult.ok(savedFileNames, "上传成功");
    }

    @Operation(summary = "增量刷新静态知识库（从暂存知识库文件夹）")
    @PostMapping("/refresh")
    public ResponseResult<String> refresh() {
        log.debug("进行增量刷新静态知识库");
        staticKnowledgeService.refreshDocument();
        return ResponseResult.ok("静态知识库增量刷新任务已提交");
    }

    @Operation(summary = "全量刷新静态知识库（从已保存知识库文件夹）")
    @PostMapping("/refreshAll")
    public ResponseResult<String> refreshAll() {
        log.debug("进行全量刷新静态知识库");
        staticKnowledgeService.fullRefreshDocument();
        return ResponseResult.ok("静态知识库全量刷新任务已提交");
    }
}
