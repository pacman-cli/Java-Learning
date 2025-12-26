package com.puspo.scalablekafkaapp.videoshare.service.auth;

import com.puspo.scalablekafkaapp.videoshare.model.User;
import com.puspo.scalablekafkaapp.videoshare.repository.UserRepository;
import com.puspo.scalablekafkaapp.videoshare.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public User register(String username, String email, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .role("USER")
                .build();
        return userRepository.save(user);
    }

    public String login(String usernameOrEmail, String rawPassword) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usernameOrEmail, rawPassword));

        Map<String, Object> claims = new HashMap<>();
        claims.put("scope", "USER");
        return jwtService.generateToken(usernameOrEmail, claims);
    }
}
