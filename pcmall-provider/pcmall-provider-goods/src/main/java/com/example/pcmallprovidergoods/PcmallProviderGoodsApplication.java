package com.example.pcmallprovidergoods;

import cn.hutool.extra.spring.EnableSpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.example.pcmallcommon.client")
@EnableSpringUtil
public class PcmallProviderGoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PcmallProviderGoodsApplication.class, args);
    }

}
