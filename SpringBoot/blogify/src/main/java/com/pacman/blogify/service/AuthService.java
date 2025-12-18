package com.pacman.blogify.service;

import com.pacman.blogify.dto.UserLoginDto;
import com.pacman.blogify.dto.UserRegisterDto;
import com.pacman.blogify.model.User;
import com.pacman.blogify.repository.UserRepository;
import com.pacman.blogify.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public void registerUser(UserRegisterDto userRegisterDto) {
        // Check if user already exists
        if (userRepository.existsByUsername(userRegisterDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(userRegisterDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .username(userRegisterDto.getUsername())
                .email(userRegisterDto.getEmail())
                .password(passwordEncoder.encode(userRegisterDto.getPassword())) //encoded password
                .build();
        userRepository.save(user);
    }

    public String loginUser(UserLoginDto userLoginDto) {
        //Authenticate user
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword()));

        // Set authentication in SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        return jwtUtil.generateToken(userLoginDto.getUsername());
    }
}
