package com.pacman.pokemonapi.controller;

import com.pacman.pokemonapi.adapter.persistence.UserEntity;
import com.pacman.pokemonapi.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserEntity signUp(@RequestBody UserEntity userEntity) {
        return userService.signUp(userEntity.getUsername(), userEntity.getPassword());
    }

    @PostMapping("/login")
    public UserEntity login(@RequestBody UserEntity userEntity) {
        return userService.login(userEntity.getUsername(), userEntity.getPassword());
    }
}
