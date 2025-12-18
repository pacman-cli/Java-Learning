package com.puspo.codearena.userservice.user.profile.service;

import com.puspo.codearena.userservice.user.profile.dto.CreateUserRequest;
import com.puspo.codearena.userservice.user.profile.dto.UpdateUserRequest;
import com.puspo.codearena.userservice.user.profile.dto.UserDto;
import com.puspo.codearena.userservice.user.profile.dto.UserStatsDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    /**
     * Create a new user
     */
    UserDto createUser(CreateUserRequest createUserRequest);

    /**
     * Get user by ID
     */
    UserDto getUserById(UUID userId);

    /**
     * Get user by username
     */
    UserDto getUserByUsername(String username);

    /**
     * Get user by email
     */
    UserDto getUserByEmail(String email);

    /**
     * Update user profile
     */
    UserDto updateUser(UUID userId, UpdateUserRequest createUserRequest);

    /**
     * Delete user (soft delete)
     */
    void deleteUser(UUID userId);

    /**
     * Get all active users
     */
    List<UserDto> getALlActiveUsers();

    /**
     * Search users by username
     */
    List<UserDto> searchUsersByUsername(String username);

    /**
     * Get user statistics
     */
    UserStatsDto getUserStats(UUID userId);

    /**
     * Update user statistics (called by submission service)
     */
    void updateUserStats(UUID userId, Integer problemSolved, Integer totalSubmissions);

    /**
     * Get top users by problems solved
     */
    List<UserDto> getTopUsers(int limit);
}
