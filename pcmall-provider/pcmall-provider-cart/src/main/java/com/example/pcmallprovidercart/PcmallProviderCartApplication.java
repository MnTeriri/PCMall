package com.example.pcmallprovidercart;

import cn.hutool.extra.spring.EnableSpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableSpringUtil
public class PcmallProviderCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(PcmallProviderCartApplication.class, args);
    }

}
