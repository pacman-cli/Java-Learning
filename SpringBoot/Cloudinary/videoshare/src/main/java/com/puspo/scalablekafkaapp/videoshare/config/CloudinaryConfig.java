package com.puspo.scalablekafkaapp.videoshare.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Value( "${cloudinary.api-key}")
    public String apiKey;

    @Value( "${cloudinary.api-secret}")
    public String apiSecret;

    @Value( "${cloudinary.cloud-name}")
    public String cloudName;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }
}


//By default, Spring Boot limits:
//spring.servlet.multipart.max-file-size → 1MB
//spring.servlet.multipart.max-request-size → 10MB
//So when you upload a file larger than this, Tomcat rejects it with a 413 error.