package com.pacman.uberprojectauthservice.controllers;

import com.pacman.uberprojectauthservice.dto.PassengerDto;
import com.pacman.uberprojectauthservice.dto.PassengerSignupRequestDto;
import com.pacman.uberprojectauthservice.services.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // sign up creation
    @PostMapping("/signup/passenger")
    public ResponseEntity<PassengerDto> signUp(@RequestBody PassengerSignupRequestDto passengerSignupRequestDto) {
        PassengerDto response = authService.toSignedInPassengerDto(passengerSignupRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // sign in creation
    @GetMapping("/signing/passenger")
    public ResponseEntity<?> signIn() {
        return new ResponseEntity<>(10, HttpStatus.CREATED);
    }
}
