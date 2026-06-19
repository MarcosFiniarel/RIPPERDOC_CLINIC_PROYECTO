package com.css.cyber_sale_service.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cyber Clinic Network API - Sale Service")
                        .version("1.0.0")
                        .description("Endpoints de orquestación central para procesar compras, verificar fondos y disparar órdenes de cirugías")
                        .contact(new Contact()
                                .name("NetRunners Team")
                                .email("netrunnersteam@cyberclinic.nc"))
                        .license(new License()
                                .name("MIT License - Copyright (c) 2026 Marcos")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
