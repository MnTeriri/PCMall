package com.example.pcmallproviderpayment;

import cn.hutool.extra.spring.EnableSpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableSpringUtil
public class PcmallProviderPaymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(PcmallProviderPaymentApplication.class, args);
	}

}
