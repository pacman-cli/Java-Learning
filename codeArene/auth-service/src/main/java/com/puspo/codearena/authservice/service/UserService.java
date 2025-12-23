package com.puspo.codearena.authservice.service;

import com.puspo.codearena.authservice.dto.UserListResponseDto;
import com.puspo.codearena.authservice.dto.UserResponseDto;
import com.puspo.codearena.authservice.entity.User;
import com.puspo.codearena.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return UserResponseDto.fromUser(user);
    }

    public UserResponseDto findUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return UserResponseDto.fromUser(user);
    }

    public UserResponseDto findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return UserResponseDto.fromUser(user);
    }

    public UserListResponseDto findAllUsers(int page, int size, String sortBy, String sortDir) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponseDto> userDtos = userPage.getContent()
                .stream()
                .map(UserResponseDto::fromUser)
                .collect(Collectors.toList());

        return UserListResponseDto.fromPageData(
                userDtos,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages()
        );
    }

    public List<UserResponseDto> findUsersByRole(User.Role role) {
        List<User> users = userRepository.findAll()
                .stream()
                .filter(user -> user.getRole() == role)
                .collect(Collectors.toList());

        return users.stream()
                .map(UserResponseDto::fromUser)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDto updateUserRole(Long userId, User.Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        User.Role oldRole = user.getRole();
        user.setRole(newRole);
        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        log.info("Updated user {} role from {} to {}", user.getUsername(), oldRole, newRole);

        return UserResponseDto.fromUser(updatedUser);
    }

    @Transactional
    public UserResponseDto enableUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.setIsEnabled(true);
        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        log.info("Enabled user: {}", user.getUsername());

        return UserResponseDto.fromUser(updatedUser);
    }

    @Transactional
    public UserResponseDto disableUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.setIsEnabled(false);
        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        log.info("Disabled user: {}", user.getUsername());

        return UserResponseDto.fromUser(updatedUser);
    }

    @Transactional
    public UserResponseDto lockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.setIsAccountNonLocked(false);
        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        log.info("Locked user account: {}", user.getUsername());

        return UserResponseDto.fromUser(updatedUser);
    }

    @Transactional
    public UserResponseDto unlockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.setIsAccountNonLocked(true);
        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        log.info("Unlocked user account: {}", user.getUsername());

        return UserResponseDto.fromUser(updatedUser);
    }

    @Transactional
    public UserResponseDto changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        log.info("Password changed for user: {}", user.getUsername());

        return UserResponseDto.fromUser(updatedUser);
    }

    @Transactional
    public void updateLastLogin(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setLastLogin(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
            log.debug("Updated last login for user: {}", username);
        }
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        userRepository.delete(user);
        log.info("Deleted user: {}", user.getUsername());
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public long countUsers() {
        return userRepository.count();
    }

    public long countUsersByRole(User.Role role) {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRole() == role)
                .count();
    }

    public List<UserResponseDto> findActiveUsers() {
        List<User> users = userRepository.findAll()
                .stream()
                .filter(User::getIsEnabled)
                .collect(Collectors.toList());

        return users.stream()
                .map(UserResponseDto::fromUser)
                .collect(Collectors.toList());
    }

    public List<UserResponseDto> findRecentUsers(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<User> userPage = userRepository.findAll(pageable);

        return userPage.getContent()
                .stream()
                .map(UserResponseDto::fromUser)
                .collect(Collectors.toList());
    }
}
