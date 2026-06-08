package com.example.pcmallproviderorder;

import cn.hutool.extra.spring.EnableSpringUtil;
import com.example.pcmallcommon.annotation.EnableHttpClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableHttpClients
@EnableSpringUtil
public class PcmallProviderOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(PcmallProviderOrderApplication.class, args);
	}

}
