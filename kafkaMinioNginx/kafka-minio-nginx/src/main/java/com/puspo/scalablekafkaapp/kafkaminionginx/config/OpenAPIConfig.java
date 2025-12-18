package com.puspo.scalablekafkaapp.kafkaminionginx.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("File Upload API with MinIO & Kafka")
                        .description("Presigned uploads, confirmation, and Kafka event streaming")
                        .version("1.0.0"));
    }
}

