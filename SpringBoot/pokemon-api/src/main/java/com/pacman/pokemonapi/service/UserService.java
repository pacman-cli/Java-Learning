package com.pacman.pokemonapi.service;

import com.pacman.pokemonapi.adapter.persistence.UserEntity;
import com.pacman.pokemonapi.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity signUp(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(password)
                .build();

        return userRepository.save(userEntity);
    }

    //login validation
    public UserEntity login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password))
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }
}
