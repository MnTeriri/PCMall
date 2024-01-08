package com.example.pcmallproviderorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PcmallProviderOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(PcmallProviderOrderApplication.class, args);
    }

}
