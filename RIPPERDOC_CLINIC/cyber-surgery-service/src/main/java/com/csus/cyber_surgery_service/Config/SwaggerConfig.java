package com.csus.cyber_surgery_service.Config;

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
                        .title("Cyber Clinic Network API - Surgery Service")
                        .version("1.0.0")
                        .description("Endpoints automáticos y manuales para la programación, consulta y ejecución de instalaciones de cromo biológico")
                        .contact(new Contact()
                                .name("NetRunners Team")
                                .email("netrunnersteam@cyberclinic.nc"))
                        .license(new License()
                                .name("MIT License - Copyright (c) 2026 Marcos")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
