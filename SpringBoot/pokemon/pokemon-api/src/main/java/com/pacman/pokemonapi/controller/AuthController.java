package com.pacman.pokemonapi.controller;

import com.pacman.pokemonapi.dto.AuthResponseDto;
import com.pacman.pokemonapi.dto.UserRequestDto;
import com.pacman.pokemonapi.dto.UserResponseDto;
import com.pacman.pokemonapi.service.UserService;
import com.pacman.pokemonapi.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserRequestDto userRequestDto) {
        try {
            if (userService.findByUsername(userRequestDto.getUsername()).isPresent()) {
                return ResponseEntity.badRequest().body(
                        AuthResponseDto.withMessage("Username already exists")
                );
            }

            //create new user with encrypted password
            UserResponseDto createdUser = userService.createUser(userRequestDto);
            return ResponseEntity.ok(AuthResponseDto.withMessage("User registered successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    AuthResponseDto.withMessage("Registration failed: " + e.getMessage())
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody UserRequestDto userRequestDto) {
        try {
            UserResponseDto userResponseDto = userService.login(userRequestDto);
            String token = jwtUtil.generateToken(userResponseDto.getUsername(), userResponseDto.getRole());
            return ResponseEntity.ok(new AuthResponseDto(token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    AuthResponseDto.withMessage("Login failed: " + e.getMessage())
            );
        }
    }
}
