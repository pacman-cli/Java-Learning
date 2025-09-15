package com.pacman.pokemonapi.controller;

import com.pacman.pokemonapi.adapter.persistence.UserEntity;
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
    public ResponseEntity<UserResponseDto> signUp(@RequestBody UserRequestDto userRequestDto) {
        UserResponseDto savedUser = userService.signUp(userRequestDto);
        return ResponseEntity.ok(savedUser);
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userService.login(userRequestDto);
        String token = jwtUtil.generateToken(userResponseDto.getUsername(), userResponseDto.getRole());
        return ResponseEntity.ok(token);
    }
}
