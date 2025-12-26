package com.bank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * Beneficiary Entity - Represents a saved transfer recipient
 * Users can save frequently used transfer destinations for quick access
 * 
 * Features:
 * - Link beneficiary to a user account
 * - Store beneficiary account details
 * - Nickname for easy identification
 * - Creation timestamp
 */
@Entity
@Table(name = "beneficiaries")
public class Beneficiary {

    /**
     * Primary key - Auto-generated beneficiary ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User who owns this beneficiary (the account holder who saved this recipient)
     * Many beneficiaries can belong to one user
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Account that this beneficiary represents
     * The account to which transfers can be made
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    /**
     * Nickname for this beneficiary - User-friendly name
     * Example: "Mom's Account", "Business Partner", etc.
     */
    @Column(nullable = false, length = 100)
    @NotBlank(message = "Nickname cannot be blank")
    private String nickname;

    /**
     * Timestamp when beneficiary was added
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when beneficiary was last updated
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Default constructor - Required by JPA
     */
    public Beneficiary() {
    }

    /**
     * Constructor to create a new beneficiary
     * @param user The user who owns this beneficiary
     * @param account The account this beneficiary represents
     * @param nickname User-friendly nickname for this beneficiary
     */
    public Beneficiary(User user, Account account, String nickname) {
        this.user = user;
        this.account = account;
        this.nickname = nickname;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    @Override
    public String toString() {
        return "Beneficiary{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", accountNumber='" + (account != null ? account.getAccountNumber() : "N/A") + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

