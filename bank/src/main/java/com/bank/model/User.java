package com.bank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User Entity - Represents a bank customer/user in the system
 * Each user can have multiple accounts and must authenticate to access the system
 * 
 * Features:
 * - Username and password authentication
 * - Email address for notifications
 * - Multiple accounts per user
 * - Account creation timestamp
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Primary key - Auto-generated user ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Username - Must be unique and not blank
     * Used for login authentication
     */
    @Column(unique = true, nullable = false, length = 50)
    @NotBlank(message = "Username cannot be blank")
    private String username;

    /**
     * Password - Encrypted password for authentication
     * In production, this should be hashed using BCrypt or similar
     */
    @Column(nullable = false, length = 255)
    @NotBlank(message = "Password cannot be blank")
    private String password;

    /**
     * Full name of the user
     */
    @Column(nullable = false, length = 100)
    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    /**
     * Email address - Must be unique and valid format
     * Used for notifications and account recovery
     */
    @Column(unique = true, nullable = false, length = 100)
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    private String email;

    /**
     * Phone number - Contact information
     */
    @Column(length = 20)
    private String phoneNumber;

    /**
     * Address - User's physical address
     */
    @Column(length = 255)
    private String address;

    /**
     * Timestamp when user account was created
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when user account was last updated
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * One-to-many relationship with accounts
     * A user can have multiple bank accounts
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Account> accounts = new ArrayList<>();

    /**
     * Default constructor - Required by JPA
     */
    public User() {
    }

    /**
     * Constructor to create a new user
     * @param username Unique username for login
     * @param password Password (should be hashed in production)
     * @param fullName User's full name
     * @param email User's email address
     */
    public User(String username, String password, String fullName, String email) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Pre-persist callback - Sets timestamps before saving to database
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Pre-update callback - Updates the updatedAt timestamp before any update
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    /**
     * Add an account to this user
     * @param account The account to add
     */
    public void addAccount(Account account) {
        accounts.add(account);
        account.setUser(this);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

