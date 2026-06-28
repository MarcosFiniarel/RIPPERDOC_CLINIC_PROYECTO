package com.api.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	// 1. TU ENRUTAMIENTO DE DATOS (Se mantiene intacto y funcional)
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("cyber-ciberware-route", r -> r.path("/cyber-ciberware-service/**")
						.filters(f -> f.stripPrefix(1))
						.uri("lb://cyber-ciberware-service"))
				.route("cyber-compatibility-route", r -> r.path("/cyber-compatibility-service/**")
						.filters(f -> f.stripPrefix(1))
						.uri("lb://cyber-compatibility-service"))
				.route("cyber-finance-route", r -> r.path("/cyber-finance-service/**")
						.filters(f -> f.stripPrefix(1))
						.uri("lb://cyber-finance-service"))
				.route("cyber-maxtac-route", r -> r.path("/cyber-maxtac-service/**")
						.filters(f -> f.stripPrefix(1))
						.uri("lb://cyber-maxtac-service"))
				.route("cyber-patient-route", r -> r.path("/cyber-patient-service/**")
						.filters(f -> f.stripPrefix(1))
						.uri("lb://cyber-patient-service"))
				.route("cyber-sale-route", r -> r.path("/cyber-sale-service/**")
						.filters(f -> f.stripPrefix(1))
						.uri("lb://cyber-sale-service"))
				.route("cyber-surgery-route", r -> r.path("/cyber-surgery-service/**")
						.filters(f -> f.stripPrefix(1))
						.uri("lb://cyber-surgery-service"))
				.build();
	}
}
