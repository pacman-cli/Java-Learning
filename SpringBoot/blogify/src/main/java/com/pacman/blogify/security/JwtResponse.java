package com.pacman.blogify.security;

import lombok.AllArgsConstructor;
import lombok.Data;

//simple wrapper for JWT response
@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
}
