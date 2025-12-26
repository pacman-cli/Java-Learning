package com.puspo.codearena.paginationexample.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(
            new Info()
                .title("Product Pagination API")
                .version("1.0.0")
                .description(
                    "REST API for managing products with advanced pagination, sorting, and filtering capabilities. " +
                        "This API demonstrates Spring Boot pagination features with comprehensive Swagger documentation."
                )
        );
    }
}
