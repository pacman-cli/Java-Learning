package com.pacman.hospital.core.security.service;

import com.pacman.hospital.core.security.dto.LoginRequest;
import com.pacman.hospital.core.security.dto.LoginResponse;
import com.pacman.hospital.core.security.dto.RegisterRequest;
import com.pacman.hospital.core.security.dto.UserDto;
import com.pacman.hospital.core.security.model.Role;
import com.pacman.hospital.core.security.model.User;
import com.pacman.hospital.core.security.model.UserRole;
import com.pacman.hospital.core.security.repository.RoleRepository;
import com.pacman.hospital.core.security.repository.UserRepository;
import com.pacman.hospital.core.security.util.JwtUtils;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        System.out.println("JWT: " + jwt);

        UserPrincipal userPrincipal =
            (UserPrincipal) authentication.getPrincipal();
        User user = userRepository
            .findByUsername(userPrincipal.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

        return LoginResponse.builder()
            .token(jwt)
            .type("Bearer")
            .id(user.getId())
            .username(user.getUsername())
            .fullName(user.getFullName())
            .email(user.getEmail())
            .roles(
                user
                    .getRoles()
                    .stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet())
            )
            .expiresAt(
                jwtUtils
                    .getExpirationDateFromToken(jwt)
                    .toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDateTime()
            )
            .build();
    }

    @Transactional
    public UserDto register(RegisterRequest registerRequest) {
        // Validate username
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new IllegalArgumentException(
                "Username is already taken. Please choose a different username."
            );
        }

        // Validate email
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException(
                "Email is already in use. Please use a different email address."
            );
        }

        // Create new user's account
        User user = User.builder()
            .username(registerRequest.getUsername())
            .password(passwordEncoder.encode(registerRequest.getPassword()))
            .fullName(registerRequest.getFullName())
            .email(registerRequest.getEmail())
            .enabled(true)
            .createdAt(LocalDateTime.now())
            .build();

        // Set roles
        Set<Role> roles = new HashSet<>();
        if (
            registerRequest.getRoles() == null ||
            registerRequest.getRoles().isEmpty()
        ) {
            // Default role for new users
            Role patientRole = roleRepository
                .findByName(UserRole.PATIENT.getAuthority())
                .orElseThrow(() ->
                    new RuntimeException(
                        "System configuration error: PATIENT role is not configured. Please contact system administrator."
                    )
                );
            roles.add(patientRole);
        } else {
            registerRequest
                .getRoles()
                .forEach(roleName -> {
                    Role role = roleRepository
                        .findByName(roleName)
                        .orElseThrow(() ->
                            new RuntimeException(
                                "Invalid role: " +
                                    roleName +
                                    ". Please contact system administrator."
                            )
                        );
                    roles.add(role);
                });
        }
        user.setRoles(roles);
        System.out.println("User roles: " + user.getRoles());

        User savedUser = userRepository.save(user);

        return UserDto.builder()
            .id(savedUser.getId())
            .username(savedUser.getUsername())
            .fullName(savedUser.getFullName())
            .email(savedUser.getEmail())
            .enabled(savedUser.isEnabled())
            .createdAt(savedUser.getCreatedAt())
            .roles(
                savedUser
                    .getRoles()
                    .stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet())
            )
            .build();
    }

    public UserDto getCurrentUser() {
        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository
            .findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return UserDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .fullName(user.getFullName())
            .email(user.getEmail())
            .enabled(user.isEnabled())
            .createdAt(user.getCreatedAt())
            .roles(
                user
                    .getRoles()
                    .stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet())
            )
            .build();
    }
}
