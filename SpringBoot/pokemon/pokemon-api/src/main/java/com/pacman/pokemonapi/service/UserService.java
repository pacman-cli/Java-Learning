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

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserResponseDto signUp(UserRequestDto userRequestDto) {
        if (userRepository.findByUsername(userRequestDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        UserEntity userEntity = UserEntity.builder()
                .username(userRequestDto.getUsername())
                .password(bCryptPasswordEncoder.encode(userRequestDto.getPassword())) // ✅ bcrypt
                .role(userRequestDto.getRole() != null ? userRequestDto.getRole() : "USER")
                .build();

        UserEntity savedUser = userRepository.save(userEntity);
        return UserMapper.toResponseDto(savedUser);

        // map DTO -> Entity
        // UserEntity userEntity = UserMapper.toEntity(userRequestDto);
        //save
        // UserEntity savedUser = userRepository.save(userEntity);
        // map Entity -> ResponseDto
        // return UserMapper.toResponseDto(savedUser);
    }

    // login validation
    // public UserEntity login(String username, String password) {
    // return userRepository.findByUsername(username)
    // .filter(u -> u.getPassword().equals(password))
    // .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    // }

    public UserResponseDto login(UserRequestDto userRequestDto) {
        UserEntity userEntity = userRepository.findByUsername(userRequestDto.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        // Use bcrypt to check password
        if (!bCryptPasswordEncoder.matches(userRequestDto.getPassword(), userEntity.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return UserMapper.toResponseDto(userEntity);
    }
}
