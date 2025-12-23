package com.puspo.scalablekafkaapp.videoshare.controller.auth;

import com.puspo.scalablekafkaapp.videoshare.dto.LoginRequest;
import com.puspo.scalablekafkaapp.videoshare.dto.RegisterRequest;
import com.puspo.scalablekafkaapp.videoshare.dto.TokenResponse;
import com.puspo.scalablekafkaapp.videoshare.dto.UserResponse;
import com.puspo.scalablekafkaapp.videoshare.model.User;
import com.puspo.scalablekafkaapp.videoshare.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User registration and login")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        User user = authService.register(request.getUsername(), request.getEmail(), request.getPassword());

        //creating new user and assigning
        UserResponse res = new UserResponse();
        res.setId(user.getId());
        res.setUsername(user.getUsername());
        res.setEmail(user.getEmail());
        res.setRole(user.getRole());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    @Operation(summary = "Login and get JWT token")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getUsernameOrEmail(), request.getPassword());
        TokenResponse res = new TokenResponse();
        res.setToken(token);
        return ResponseEntity.ok(res);
    }

}
