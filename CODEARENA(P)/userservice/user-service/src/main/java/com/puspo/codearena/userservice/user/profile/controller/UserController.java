package com.puspo.codearena.userservice.user.profile.controller;

import com.puspo.codearena.userservice.user.profile.dto.CreateUserRequest;
import com.puspo.codearena.userservice.user.profile.dto.UpdateUserRequest;
import com.puspo.codearena.userservice.user.profile.dto.UserDto;
import com.puspo.codearena.userservice.user.profile.dto.UserStatsDto;
import com.puspo.codearena.userservice.user.profile.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    /**
     * Create a new user POST /api/users
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("REST request to create user: {}", request.getUsername());
        UserDto createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * Get user by ID GET /api/users/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID userId) {
        log.info("REST request to get user by ID: {}", userId);
        UserDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    /**
     * Get user by username GET /api/users/username/{username}
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        log.info("REST request to get user by username: {}", username);
        UserDto user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    /**
     * Get user by email GET /api/users/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        log.info("REST request to get user by email: {}", email);
        UserDto user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    /**
     * Update user profile PATCH /api/users/{userId}
     */
    @PatchMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserRequest request) {
        log.info("REST request to update user: {}", userId);
        UserDto updatedUser = userService.updateUser(userId, request);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Delete user (soft delete) DELETE /api/users/{userId}
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        log.info("REST request to delete user: {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all active users GET /api/users
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllActiveUsers() {
        log.info("REST request to get all active users");
        List<UserDto> users = userService.getAllActiveUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Search users by username GET /api/users/search?q={searchTerm}
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam("q") String searchTerm) {
        log.info("REST request to search users: {}", searchTerm);
        List<UserDto> users = userService.searchUsersByUsername(searchTerm);
        return ResponseEntity.ok(users);
    }

    /**
     * Get user statistics GET /api/users/{userId}/stats
     */
    @GetMapping("/{userId}/stats")
    public ResponseEntity<UserStatsDto> getUserStats(@PathVariable UUID userId) {
        log.info("REST request to get stats for user: {}", userId);
        UserStatsDto stats = userService.getUserStats(userId);
        return ResponseEntity.ok(stats);
    }

    /**
     * Get top users by problems solved GET /api/users/leaderboard?limit={limit}
     */
    @GetMapping("/leaderboard")
    public ResponseEntity<List<UserDto>> getTopUsers(
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        log.info("REST request to get top {} users", limit);
        List<UserDto> topUsers = userService.getTopUsers(limit);
        return ResponseEntity.ok(topUsers);
    }

    /**
     * Update user statistics (internal endpoint - will be secured later) PATCH /api/users/{userId}/stats
     */
    @PatchMapping("/{userId}/stats")
    public ResponseEntity<Void> updateUserStats(
            @PathVariable UUID userId,
            @RequestParam(required = false) Integer problemsSolved,
            @RequestParam(required = false) Integer totalSubmissions
    ) {
        log.info("REST request to update stats for user: {}", userId);
        userService.updateUserStats(userId, problemsSolved, totalSubmissions);
        return ResponseEntity.ok().build();
    }

    /**
     * Health check endpoint GET /api/users/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("User Service is running!");
    }
}
