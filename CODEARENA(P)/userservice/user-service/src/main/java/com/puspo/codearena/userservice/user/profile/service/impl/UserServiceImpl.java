package com.puspo.codearena.userservice.user.profile.service.impl;

import com.puspo.codearena.userservice.user.exception.UserAlreadyExistsException;
import com.puspo.codearena.userservice.user.exception.UserNotFoundException;
import com.puspo.codearena.userservice.user.profile.dto.CreateUserRequest;
import com.puspo.codearena.userservice.user.profile.dto.UpdateUserRequest;
import com.puspo.codearena.userservice.user.profile.dto.UserDto;
import com.puspo.codearena.userservice.user.profile.dto.UserStatsDto;
import com.puspo.codearena.userservice.user.profile.entity.User;
import com.puspo.codearena.userservice.user.profile.repository.UserRepository;
import com.puspo.codearena.userservice.user.profile.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto createUser(CreateUserRequest createUserRequest) {
        log.info("Creating user with username: {}", createUserRequest.getUsername());

        //Check if user already exists
        if (userRepository.existsByUsername(createUserRequest.getUsername())) {
            throw new UserAlreadyExistsException("username", createUserRequest.getUsername());
        }
        //Check if email already exists
        if (userRepository.existsByEmail(createUserRequest.getUsername())) {
            throw new UserAlreadyExistsException("email", createUserRequest.getEmail());
        }

        //Create User
        User user = User.builder()
                .username(createUserRequest.getUsername())
                .email(createUserRequest.getEmail())
                .avatarUrl(createUserRequest.getAvatarUrl())
                .hashedPassword(createUserRequest.getPassword())
                .oauthId(createUserRequest.getOauthId())
                .oauthProvider(createUserRequest.getOauthProvider())
                .isActive(true)
                .role(User.UserRole.USER)
                .problemsSolved(0)
                .totalSubmissions(0)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());
        return UserDto.toDto(savedUser);
    }

    @Override
    public UserDto getUserById(UUID userId) {
        log.info("Getting user with ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return UserDto.toDto(user);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        log.info("Fetching user by username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return UserDto.toDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("email: " + email));

        return UserDto.toDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(UUID userId, UpdateUserRequest request) {
        log.info("Updating user with ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        //Update user
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new UserAlreadyExistsException("email", request.getEmail());
            }
            user.setEmail(request.getEmail());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }

        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }

        if (request.getGithubLink() != null) {
            user.setGithubLink(request.getGithubLink());
        }

        User updatedUser = userRepository.save(user);
        log.info("User updated successfully: {}", userId);
        return UserDto.toDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        log.info("Deleting user with ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // Soft delete
        user.setIsActive(false);
        userRepository.save(user);

        log.info("User soft-deleted successfully: {}", userId);
    }

    @Override
    public List<UserDto> getALlActiveUsers() {

        log.info("Fetching all active users");

        return userRepository.findByIsActiveTrue()
                .stream()
                .map(UserDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> searchUsersByUsername(String username) {
        log.info("Searching users by username: {}", username);

        return userRepository.searchByUsername(username)
                .stream()
                .map(UserDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserStatsDto getUserStats(UUID userId) {
        log.info("Fetching user stats for user with ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        double successRate = user.getTotalSubmissions() > 0 ?
                (double) user.getProblemsSolved() / user.getTotalSubmissions() * 100 : 0.0; //this is to calculate
        // success rate based on total submissions

        return UserStatsDto.builder()
                .problemsSolved(user.getProblemsSolved())
                .totalSubmissions(user.getTotalSubmissions())
                .ranking(user.getRanking())
                .successRate(Math.round(successRate * 100.0) / 100.0) //two decimal places
                .build();
    }

    @Override
    @Transactional
    public void updateUserStats(UUID userId, Integer problemSolved, Integer totalSubmissions) {
        log.info("Updating stats for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        //Update stats
        if (problemSolved != null) {
            user.setProblemsSolved(problemSolved);
        }

        if (totalSubmissions != null) {
            user.setTotalSubmissions(totalSubmissions);
        }

        userRepository.save(user);
        log.info("User stats updated successfully: {}", userId);
    }

    @Override
    public List<UserDto> getTopUsers(int limit) {
        log.info("Fetching top {} users", limit);

        return userRepository.findTopUsersByProblemsSolved(limit)
                .stream()
                .limit(limit)
                .map(UserDto::toDto)
                .collect(Collectors.toList());
    }
}
