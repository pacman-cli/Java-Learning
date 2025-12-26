package com.pacman.surokkha.controller;

import com.pacman.surokkha.dto.LoginRequestDto;
import com.pacman.surokkha.dto.LoginResponseDto;
import com.pacman.surokkha.dto.RegisterRequestDto;
import com.pacman.surokkha.security.JwtUtil;
import com.pacman.surokkha.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication controller: - /api/auth/register -> register a new user - /api/auth/login    -> authenticate and
 * return a JWT
 * <p>
 * Notes: - Constructor injection is used (preferred). - We catch specific authentication exceptions (better error
 * messages). - Methods are validated with @Validated and DTOs should have validation annotations.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // Constructor injection (Spring will wire beans)
    public AuthController(AuthService authService,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Register a new user. Expects a validated RegisterRequestDto. Returns 200 OK on success, 400 Bad Request with
     * message on failure.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody RegisterRequestDto registerRequestDto) {
        try {
            // Delegate registration logic to service layer (service should throw meaningful exceptions)
            authService.registerNewUser(registerRequestDto);
            return ResponseEntity.ok("User registered successfully");
        } catch (IllegalArgumentException iae) {
            // Bad request - e.g. username/email already exists
            return ResponseEntity.badRequest().body(iae.getMessage());
        } catch (Exception e) {
            // Generic fallback (log the error in real app)
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    /**
     * Login endpoint. Accepts LoginRequestDto and returns LoginResponseDto containing the JWT on success.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequestDto loginRequestDto) {
        try {
            // Attempt authentication (throws BadCredentialsException or other AuthenticationException on failure)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUsernameOrEmail(),
                            loginRequestDto.getPassword()
                    )
            );

            // Extract username (safe handling in case principal is UserDetails or a simple string)
            Object principal = authentication.getPrincipal();
            String username;
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = authentication.getName();
            }

            // Generate token for the authenticated username
            String token = jwtUtil.generateToken(username);

            // Return token DTO (you can expand DTO with tokenType / expiresIn etc.)
            return ResponseEntity.ok(new LoginResponseDto(token));
        } catch (BadCredentialsException ex) {
            // Wrong credentials
            return ResponseEntity.status(401).body("Invalid username/password supplied");
        } catch (DisabledException ex) {
            // Account disabled
            return ResponseEntity.status(403).body("User account is disabled");
        } catch (LockedException ex) {
            // Account locked
            return ResponseEntity.status(403).body("User account is locked");
        } catch (AuthenticationException ex) {
            // Other authentication errors
            return ResponseEntity.status(401).body("Authentication failed: " + ex.getMessage());
        } catch (Exception ex) {
            // Generic fallback
            return ResponseEntity.status(500).body("An unexpected error occurred: " + ex.getMessage());
        }
    }
}
