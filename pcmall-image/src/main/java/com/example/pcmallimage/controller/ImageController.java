package com.example.pcmallimage.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import com.example.pcmallcommon.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("/image")
public class ImageController {

    @PostMapping("/upload")
    public ResponseResult<String> upload(@RequestParam("file") MultipartFile uploadFile) throws IOException {
        String targetPath = ResourceUtils.getURL("classpath:").getPath() + "static/image/";
        String oldName = uploadFile.getOriginalFilename();//原文件名
        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_" +
                RandomUtil.randomNumbers(6) + oldName.substring(oldName.lastIndexOf("."));//生成新文件名
        log.debug("文件原名称：{}", oldName);
        log.debug("文件现名称：{}", fileName);
        uploadFile.transferTo(new File("F:/Code/IDEA/PCMall/pcmall-image/src/main/resources/static/image/" + fileName));
        FileUtil.copy("F:/Code/IDEA/PCMall/pcmall-image/src/main/resources/static/image/" + fileName,
                targetPath + fileName, true);
        return ResponseResult.ok(fileName);
    }

    @RequestMapping("/getImage")
    public String getImage() {
        return "这是获取照片接口（获取Base64）";
    }
}
