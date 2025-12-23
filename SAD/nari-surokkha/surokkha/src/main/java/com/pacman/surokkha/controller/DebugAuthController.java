package com.pacman.surokkha.controller;

import com.pacman.surokkha.dto.LoginRequestDto;
import com.pacman.surokkha.models.User;
import com.pacman.surokkha.repository.UserRepository;
import com.pacman.surokkha.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Optional;

/**
 * Temporary debug controller — do NOT ship to production. - Tests authenticationManager.authenticate directly and
 * returns helpful info. - Remove after debugging.
 */
@RestController
@RequestMapping("/api/debug")
public class DebugAuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public DebugAuthController(AuthenticationManager authenticationManager,
                               JwtUtil jwtUtil,
                               UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    // Try authentication and return details (for debugging)
    @PostMapping("/try-login")
    public ResponseEntity<?> tryLogin(@Valid @RequestBody LoginRequestDto req) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsernameOrEmail(), req.getPassword())
            );
            Object principal = auth.getPrincipal();
            String token = jwtUtil.generateToken(req.getUsernameOrEmail());
            return ResponseEntity.ok(
                    java.util.Map.of(
                            "authenticated", true,
                            "principal", principal instanceof UserDetails ? ((UserDetails) principal).getUsername() : principal.toString(),
                            "authorities", auth.getAuthorities().toString(),
                            "jwt", token
                    )
            );
        } catch (Exception ex) {
            // Return the exception class and message so we can see what's thrown
            return ResponseEntity.status(500).body(java.util.Map.of(
                    "exception", ex.getClass().getName(),
                    "message", ex.getMessage()
            ));
        }
    }

    // Inspect stored user (show hashed password) - debug only
    @GetMapping("/user/{username}")
    public ResponseEntity<?> inspectUser(@PathVariable String username) {
        Optional<com.pacman.surokkha.models.User> u = userRepository.findByUsername(username);
        if (u.isEmpty()) return ResponseEntity.status(404).body("not found");
        User user = u.get();
        return ResponseEntity.ok(java.util.Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "password_hashed", user.getPassword(),
                "roles", user.getRoles()
        ));
    }
}
