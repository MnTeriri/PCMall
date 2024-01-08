package com.example.pcmallconsumermobile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PcmallConsumerMobileApplication {

    public static void main(String[] args) {
        SpringApplication.run(PcmallConsumerMobileApplication.class, args);
    }

}
