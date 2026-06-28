package com.cfs.cyber_finance_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CyberFinanceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CyberFinanceServiceApplication.class, args);
	}

}
