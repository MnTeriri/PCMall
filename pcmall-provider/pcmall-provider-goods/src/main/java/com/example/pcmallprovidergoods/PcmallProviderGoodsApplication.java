package com.example.pcmallprovidergoods;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PcmallProviderGoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PcmallProviderGoodsApplication.class, args);
    }

}
