package com.puspo.codearena.authservice.controller;

import com.puspo.codearena.authservice.dto.UserListResponseDto;
import com.puspo.codearena.authservice.dto.UserResponseDto;
import com.puspo.codearena.authservice.entity.User;
import com.puspo.codearena.authservice.service.UserService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDto> getCurrentUser(
        Authentication authentication
    ) {
        String username = authentication.getName();
        log.info("Getting profile for user: {}", username);
        UserResponseDto user = userService.findUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        log.info("Getting user by id: {}", id);
        UserResponseDto user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<UserResponseDto> getUserByUsername(
        @PathVariable String username
    ) {
        log.info("Getting user by username: {}", username);
        UserResponseDto user = userService.findUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserListResponseDto> getAllUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "desc") String sortDir
    ) {
        log.info(
            "Getting all users - page: {}, size: {}, sortBy: {}, sortDir: {}",
            page,
            size,
            sortBy,
            sortDir
        );
        UserListResponseDto users = userService.findAllUsers(
            page,
            size,
            sortBy,
            sortDir
        );
        return ResponseEntity.ok(users);
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<List<UserResponseDto>> getUsersByRole(
        @PathVariable User.Role role
    ) {
        log.info("Getting users by role: {}", role);
        List<UserResponseDto> users = userService.findUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<List<UserResponseDto>> getActiveUsers() {
        log.info("Getting active users");
        List<UserResponseDto> users = userService.findActiveUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/recent")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<List<UserResponseDto>> getRecentUsers(
        @RequestParam(defaultValue = "10") int limit
    ) {
        log.info("Getting recent users with limit: {}", limit);
        List<UserResponseDto> users = userService.findRecentUsers(limit);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getUserStats() {
        log.info("Getting user statistics");
        Map<String, Object> stats = Map.of(
            "totalUsers",
            userService.countUsers(),
            "adminCount",
            userService.countUsersByRole(User.Role.ADMIN),
            "moderatorCount",
            userService.countUsersByRole(User.Role.MODERATOR),
            "userCount",
            userService.countUsersByRole(User.Role.USER)
        );
        return ResponseEntity.ok(stats);
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> updateUserRole(
        @PathVariable Long id,
        @RequestBody Map<String, String> request
    ) {
        User.Role newRole = User.Role.valueOf(request.get("role"));
        log.info("Updating user {} role to {}", id, newRole);
        UserResponseDto user = userService.updateUserRole(id, newRole);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> enableUser(@PathVariable Long id) {
        log.info("Enabling user: {}", id);
        UserResponseDto user = userService.enableUser(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> disableUser(@PathVariable Long id) {
        log.info("Disabling user: {}", id);
        UserResponseDto user = userService.disableUser(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> lockUser(@PathVariable Long id) {
        log.info("Locking user: {}", id);
        UserResponseDto user = userService.lockUser(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}/unlock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> unlockUser(@PathVariable Long id) {
        log.info("Unlocking user: {}", id);
        UserResponseDto user = userService.unlockUser(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}/password")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserResponseDto> changePassword(
        @PathVariable Long id,
        @RequestBody Map<String, String> request
    ) {
        String newPassword = request.get("password");
        log.info("Changing password for user: {}", id);
        UserResponseDto user = userService.changePassword(id, newPassword);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Deleting user: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
