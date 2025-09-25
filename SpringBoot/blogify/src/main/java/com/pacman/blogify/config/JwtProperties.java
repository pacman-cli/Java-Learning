package com.pacman.blogify.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    private String secret;
    private Long expirationMs;

    // Getters and setters
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getExpirationMs() {
        return expirationMs;
    }

    public void setExpirationMs(Long expirationMs) {
        this.expirationMs = expirationMs;
    }
}