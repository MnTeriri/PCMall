package com.example.pcmallconsumermobile;

import cn.hutool.extra.spring.EnableSpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.example.pcmallcommon.client")
@EnableSpringUtil
public class PcmallConsumerMobileApplication {

    public static void main(String[] args) {
        SpringApplication.run(PcmallConsumerMobileApplication.class, args);
    }

}
