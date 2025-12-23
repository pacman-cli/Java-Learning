package com.zedcode.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger Configuration
 * Configures API documentation with Swagger UI
 */
@Configuration
public class OpenApiConfig {

    @Value("${app.name:ZedCode Backend}")
    private String appName;

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${server.servlet.context-path:/api}")
    private String contextPath;

    @Bean
    public OpenAPI customOpenAPI() {
        // Define security scheme for JWT
        SecurityScheme securityScheme = new SecurityScheme()
                .name("bearerAuth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .description("JWT Authorization header using the Bearer scheme. Example: \"Bearer {token}\"");

        // Define security requirement
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearerAuth");

        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort + contextPath)
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.zedcode.com" + contextPath)
                                .description("Production Server")
                ))
                .schemaRequirement("bearerAuth", securityScheme)
                .addSecurityItem(securityRequirement);
    }

    /**
     * API Information
     */
    private Info apiInfo() {
        return new Info()
                .title(appName + " API Documentation")
                .version(appVersion)
                .description("RESTful API documentation for " + appName + ". " +
                        "This API provides comprehensive endpoints for managing the application. " +
                        "All endpoints except authentication require a valid JWT token.")
                .contact(new Contact()
                        .name("ZedCode Development Team")
                        .email("support@zedcode.com")
                        .url("https://zedcode.com"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }
}
