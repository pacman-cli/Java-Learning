package com.pacman.surokkha.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDto {
    private String tokenType = "Bearer";
    private String accessToken;

    // convenient constructor for only token
    public LoginResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
