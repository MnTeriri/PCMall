package com.example.pcmallimage.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private ResourceLoader resourceLoader;

    @RequestMapping("/upload")
    public String upload() throws IOException {
        String path = ResourceUtils.getURL("classpath:").getPath() + "static/image";
//        FileUtil.copy(
//                "F:/Code/IDEA/PCMall/pcmall-image/src/main/resources/static/image/1.png",
//                path + "/1.png", true);
        System.out.println(path);

        return "这是上传接口";
    }

    @RequestMapping("/getImage")
    public String getImage() {
        return "这是获取照片接口（获取Base64）";
    }
}
