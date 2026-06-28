package com.csus.cyber_surgery_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CyberSurgeryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CyberSurgeryServiceApplication.class, args);
	}

}
