package com.pacman.surokkha.service;

import com.pacman.surokkha.dto.RegisterRequestDto;
import com.pacman.surokkha.models.User;
import com.pacman.surokkha.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerNewUser(RegisterRequestDto registerRequestDto) {
        if (userRepository.findByUsername(registerRequestDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken!");
        }
        if (userRepository.findByEmail(registerRequestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already taken!");
        }

        User user = User.builder()
                .username(registerRequestDto.getUsername())
                .email(registerRequestDto.getEmail())
                .phoneNumber(registerRequestDto.getPhoneNumber())
                .preferredLanguage(registerRequestDto.getPreferredLanguage())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .roles(registerRequestDto.getRoles())
                .build();

        userRepository.save(user);
    }
}
