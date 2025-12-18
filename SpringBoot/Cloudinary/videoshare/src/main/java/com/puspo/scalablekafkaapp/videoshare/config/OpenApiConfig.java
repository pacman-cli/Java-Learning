package com.puspo.scalablekafkaapp.videoshare.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cloudinaryApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cloudinary Demo API")
                        .description("Spring Boot + Cloudinary + MySQL demo with Swagger UI")
                        .version("1.0.0"));
    }
}

