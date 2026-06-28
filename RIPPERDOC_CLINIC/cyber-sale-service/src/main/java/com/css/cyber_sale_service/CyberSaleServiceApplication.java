package com.css.cyber_sale_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CyberSaleServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CyberSaleServiceApplication.class, args);
	}

}
