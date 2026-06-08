package com.example.pcmallprovideruser;

import cn.hutool.extra.spring.EnableSpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableSpringUtil
public class PcmallProviderUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(PcmallProviderUserApplication.class, args);
    }

}
