package com.pacman.pokemonapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    private String token;
    private String message;

    // Constructor for token-only responses (like login)
    public AuthResponseDto(String token) {
        this.token = token;
    }

    // Constructor for message-only responses (like signup success/error)
    public static AuthResponseDto withMessage(String message) {
        return AuthResponseDto.builder().message(message).build();
    }
}
