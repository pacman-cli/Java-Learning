package com.puspo.codearena.moduler_monolith.modules.auth.service;

import com.puspo.codearena.moduler_monolith.modules.auth.dto.AuthenticationRequest;
import com.puspo.codearena.moduler_monolith.modules.auth.dto.AuthenticationResponse;
import com.puspo.codearena.moduler_monolith.modules.auth.dto.RegisterRequest;
import com.puspo.codearena.moduler_monolith.modules.product.entity.Role;
import com.puspo.codearena.moduler_monolith.modules.product.entity.User;
import com.puspo.codearena.moduler_monolith.modules.product.repository.UserRepository;
import com.puspo.codearena.moduler_monolith.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        // User creation with encoded password
        var user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();

        // Save to repository
        userRepository.save(user);

        // Token generation (user implements UserDetails)
        var jwt = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        // Verify credentials -> username, password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );

        // If valid, fetch user and generate token
        var user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var jwt = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }
}
