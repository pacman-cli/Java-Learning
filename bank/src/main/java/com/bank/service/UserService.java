package com.bank.service;

import com.bank.model.User;
import com.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * User Service - Business logic layer for user operations
 * Handles user registration, authentication, and user management
 */
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    /**
     * Constructor injection
     * @param userRepository Repository for user data access
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Register a new user
     * Validates username and email uniqueness before creating
     * 
     * @param username Unique username for login
     * @param password Password (should be hashed in production)
     * @param fullName User's full name
     * @param email User's email address
     * @return The newly created user
     * @throws IllegalArgumentException if username or email already exists
     */
    @Transactional
    public User registerUser(String username, String password, String fullName, String email) {
        // Step 1: Validate username is not empty
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        // Step 2: Check if username already exists
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }

        // Step 3: Validate email is not empty
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        // Step 4: Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }

        // Step 5: Validate password is not empty
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        // Step 6: Validate full name is not empty
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }

        // Step 7: Create and save the new user
        // Note: In production, password should be hashed using BCryptPasswordEncoder
        User user = new User(username, password, fullName, email);
        return userRepository.save(user);
    }

    /**
     * Authenticate a user by username and password
     * 
     * @param username Username for login
     * @param password Password for authentication
     * @return Optional containing the user if authentication succeeds
     */
    public Optional<User> authenticate(String username, String password) {
        // Step 1: Find user by username
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        // Step 2: Check if user exists and password matches
        // Note: In production, use BCryptPasswordEncoder.matches() for password comparison
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                return Optional.of(user);
            }
        }
        
        // Step 3: Authentication failed
        return Optional.empty();
    }

    /**
     * Get user by username
     * 
     * @param username The username
     * @return Optional containing the user if found
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Get user by ID
     * 
     * @param id The user ID
     * @return Optional containing the user if found
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Get all users
     * 
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Update user information
     * 
     * @param user User to update
     * @return Updated user
     */
    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Check if username exists
     * 
     * @param username Username to check
     * @return true if username exists, false otherwise
     */
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Check if email exists
     * 
     * @param email Email to check
     * @return true if email exists, false otherwise
     */
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}

