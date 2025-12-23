package com.pacman.pokemonapi.service;

import com.pacman.pokemonapi.adapter.persistence.UserEntity;
import com.pacman.pokemonapi.dto.UserRequestDto;
import com.pacman.pokemonapi.dto.UserResponseDto;
import com.pacman.pokemonapi.mapper.UserMapper;
import com.pacman.pokemonapi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // login validation
    public UserResponseDto login(UserRequestDto userRequestDto) {
        UserEntity userEntity = userRepository.findByUsername(userRequestDto.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        // Use bcrypt to check password
        if (!bCryptPasswordEncoder.matches(userRequestDto.getPassword(), userEntity.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return UserMapper.toResponseDto(userEntity);
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        // Validate input
        if (userRequestDto.getUsername() == null || userRequestDto.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (userRequestDto.getPassword() == null || userRequestDto.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        // Check if user already exists
        if (userRepository.findByUsername(userRequestDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Create new user entity
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userRequestDto.getUsername());
        userEntity.setPassword(bCryptPasswordEncoder.encode(userRequestDto.getPassword())); // Encrypt password
        userEntity.setRole(userRequestDto.getRole() != null ? userRequestDto.getRole() : "USER"); // Default role

        // Save user
        UserEntity savedUser = userRepository.save(userEntity);

        // Convert to DTO and return
        return UserMapper.toResponseDto(savedUser);
    }
}
