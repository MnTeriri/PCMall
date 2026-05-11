package com.example.pcmallai;

import cn.hutool.extra.spring.EnableSpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableSpringUtil
@EnableFeignClients(basePackages = "com.example.pcmallcommon.client")
@EnableScheduling
public class PcmallAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PcmallAiApplication.class, args);
    }

}
