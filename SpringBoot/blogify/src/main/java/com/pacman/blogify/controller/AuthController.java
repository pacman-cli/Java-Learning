package com.pacman.blogify.controller;

import com.pacman.blogify.dto.UserLoginDto;
import com.pacman.blogify.dto.UserRegisterDto;
import com.pacman.blogify.security.JwtResponse;
import com.pacman.blogify.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        authService.registerUser(userRegisterDto);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> loginUser(@Valid @RequestBody UserLoginDto loginDto) {
        // Returns JWT token on successful login
        String jwt = authService.loginUser(loginDto);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}
