package com.puspo.codearena.authservice.service;

import com.puspo.codearena.authservice.dto.AuthResponse;
import com.puspo.codearena.authservice.dto.LoginRequest;
import com.puspo.codearena.authservice.dto.RegisterRequest;
import com.puspo.codearena.authservice.entity.User;
import com.puspo.codearena.authservice.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        // Create new user
        User user = User.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .role(User.Role.USER)
            .isEnabled(true)
            .isAccountNonExpired(true)
            .isAccountNonLocked(true)
            .isCredentialsNonExpired(true)
            .build();

        user = userRepository.save(user);
        log.info(
            "User registered successfully with username: {}",
            user.getUsername()
        );

        String token = jwtService.generateToken(user);
        return AuthResponse.fromUser(user, token);
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsernameOrEmail(),
                request.getPassword()
            )
        );

        User user = (User) authentication.getPrincipal();

        // Update last login time
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        log.info(
            "User logged in successfully with username: {}",
            user.getUsername()
        );

        return AuthResponse.fromUser(user, token);
    }

    public User getUserByUsername(String username) {
        return userRepository
            .findByUsername(username)
            .orElseThrow(() ->
                new RuntimeException(
                    "User not found with username: " + username
                )
            );
    }

    public User getUserByEmail(String email) {
        return userRepository
            .findByEmail(email)
            .orElseThrow(() ->
                new RuntimeException("User not found with email: " + email)
            );
    }

    public User getUserById(Long id) {
        return userRepository
            .findById(id)
            .orElseThrow(() ->
                new RuntimeException("User not found with id: " + id)
            );
    }

    public Optional<User> getUserByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(
            usernameOrEmail,
            usernameOrEmail
        );
    }
}
