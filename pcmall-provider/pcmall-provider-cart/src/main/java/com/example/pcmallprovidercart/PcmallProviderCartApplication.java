package com.example.pcmallprovidercart;

import cn.hutool.extra.spring.EnableSpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.example.pcmallcommon.client")
@EnableAspectJAutoProxy(exposeProxy = true)//指定是否暴露代理对象，通过AopContext可以进行访问
@EnableSpringUtil
public class PcmallProviderCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(PcmallProviderCartApplication.class, args);
    }

}
