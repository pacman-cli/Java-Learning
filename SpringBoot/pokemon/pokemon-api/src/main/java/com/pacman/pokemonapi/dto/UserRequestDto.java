package com.pacman.pokemonapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    private String username;
    private String password;
    private String role; // optional for signup, default = ROLE_USER
}
