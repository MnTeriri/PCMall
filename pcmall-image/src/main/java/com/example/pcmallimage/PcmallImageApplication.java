package com.example.pcmallimage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PcmallImageApplication {

	public static void main(String[] args) {
		SpringApplication.run(PcmallImageApplication.class, args);
	}

}
