package com.example.pcmallimage.controller;

import cn.hutool.core.util.RandomUtil;
import com.example.pcmallcommon.response.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/image")
@Tag(name = "图片接口")
public class ImageController {
    @Value("${image.save.dir}")
    private String imagePath;

    @Operation(summary = "上传图片")
    @PostMapping("/upload")
    public ResponseResult<String> upload(@RequestParam("file") MultipartFile uploadFile) throws IOException {
        String oldName = uploadFile.getOriginalFilename();//原文件名
        String fileName = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) +
                "_" +
                RandomUtil.randomNumbers(6) +
                oldName.substring(oldName.lastIndexOf("."));//生成新文件名

        log.debug("文件原名称：{}", oldName);
        log.debug("文件现名称：{}", fileName);

        uploadFile.transferTo(Path.of(imagePath, fileName).toFile());
        return ResponseResult.ok(fileName);
    }

    @Operation(summary = "获取图片（Base64）")
    @GetMapping("/getImage")
    public String getImage() {
        return "这是获取照片接口（获取Base64）";
    }

    @Operation(summary = "获取广告图片列表")
    @GetMapping("/getADImageList")
    public ResponseResult<List<String>> getADImageList(){
        return ResponseResult.ok(List.of("IMG_6717.png","IMG_6717.png","IMG_6717.png","IMG_6717.png","IMG_6717.png"));
    }
}
