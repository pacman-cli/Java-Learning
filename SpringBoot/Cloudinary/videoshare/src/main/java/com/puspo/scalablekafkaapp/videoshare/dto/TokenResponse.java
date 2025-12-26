package com.puspo.scalablekafkaapp.videoshare.dto;

import lombok.Data;

@Data
public class TokenResponse {
    private String token;
    private String type = "Bearer";
    private Long expiresIn;
}