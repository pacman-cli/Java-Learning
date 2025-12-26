package com.bank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Account Entity - Represents a bank account in the system
 * Enhanced with account types, status, PIN security, and transaction limits
 * 
 * Features:
 * - Account types: SAVINGS, CHECKING
 * - Account status: ACTIVE, SUSPENDED, CLOSED
 * - PIN for transaction security
 * - Interest rate for savings accounts
 * - Daily transaction limits
 * - Minimum balance requirements
 */
@Entity
@Table(name = "accounts")
public class Account {

    /**
     * Account Type Enum - Defines the type of bank account
     */
    public enum AccountType {
        SAVINGS, // Savings account - earns interest, may have minimum balance
        CHECKING // Checking account - for daily transactions, no interest
    }

    /**
     * Account Status Enum - Defines the current status of the account
     */
    public enum AccountStatus {
        ACTIVE, // Account is active and can perform transactions
        SUSPENDED, // Account is temporarily suspended (cannot perform transactions)
        CLOSED // Account is closed (permanently inactive)
    }

    /**
     * Primary key - Auto-generated account ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Account number - Must be unique and not blank
     * This is the identifier customers use to access their account
     */
    @Column(unique = true, nullable = false, length = 20)
    @NotBlank(message = "Account number cannot be blank")
    private String accountNumber;

    /**
     * Account holder's full name
     */
    @Column(nullable = false, length = 100)
    @NotBlank(message = "Account holder name cannot be blank")
    private String holderName;

    /**
     * Account type - SAVINGS or CHECKING
     * Determines interest eligibility and account features
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull(message = "Account type cannot be null")
    private AccountType accountType;

    /**
     * Account status - ACTIVE, SUSPENDED, or CLOSED
     * Controls whether account can perform transactions
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull(message = "Account status cannot be null")
    private AccountStatus status;

    /**
     * PIN (Personal Identification Number) - 4-digit security code
     * Required for sensitive operations like withdrawals and transfers
     * In production, this should be hashed
     */
    @Column(nullable = false, length = 10)
    @NotBlank(message = "PIN cannot be blank")
    private String pin;

    /**
     * Current account balance - Must be non-negative
     * Using BigDecimal for precise decimal calculations (important for money)
     */
    @Column(nullable = false, precision = 19, scale = 2)
    @NotNull(message = "Balance cannot be null")
    @DecimalMin(value = "0.0", message = "Balance cannot be negative")
    private BigDecimal balance;

    /**
     * Interest rate - Annual interest rate as percentage (e.g., 2.5 for 2.5%)
     * Only applicable for SAVINGS accounts
     */
    @Column(precision = 5, scale = 2)
    private BigDecimal interestRate;

    /**
     * Minimum balance requirement - Minimum amount that must be maintained
     * If balance falls below this, account may be charged fees or restricted
     */
    @Column(precision = 19, scale = 2)
    private BigDecimal minimumBalance;

    /**
     * Daily withdrawal limit - Maximum amount that can be withdrawn in a day
     * Resets at midnight
     */
    @Column(nullable = false, precision = 19, scale = 2)
    @NotNull(message = "Daily withdrawal limit cannot be null")
    private BigDecimal dailyWithdrawalLimit;

    /**
     * Daily transfer limit - Maximum amount that can be transferred in a day
     * Resets at midnight
     */
    @Column(nullable = false, precision = 19, scale = 2)
    @NotNull(message = "Daily transfer limit cannot be null")
    private BigDecimal dailyTransferLimit;

    /**
     * User who owns this account
     * Many accounts can belong to one user
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Timestamp when account was created
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when account was last updated
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Last date when daily limits were reset
     * Used to track and reset daily transaction limits
     */
    @Column
    private LocalDateTime lastLimitResetDate;

    /**
     * Default constructor - Required by JPA
     */
    public Account() {
        // Set default values
        this.status = AccountStatus.ACTIVE;
        this.lastLimitResetDate = LocalDateTime.now();
    }

    /**
     * Constructor to create a new account with all required fields
     * 
     * @param accountNumber  Unique account number
     * @param holderName     Account holder's name
     * @param accountType    Type of account (SAVINGS or CHECKING)
     * @param pin            4-digit PIN for security
     * @param initialBalance Initial deposit amount
     * @param user           User who owns this account
     */
    public Account(String accountNumber, String holderName, AccountType accountType,
            String pin, BigDecimal initialBalance, User user) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.accountType = accountType;
        this.pin = pin;
        this.balance = initialBalance;
        this.user = user;
        this.status = AccountStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.lastLimitResetDate = LocalDateTime.now();

        // Set default limits based on account type
        if (accountType == AccountType.SAVINGS) {
            this.dailyWithdrawalLimit = new BigDecimal("5000.00");
            this.dailyTransferLimit = new BigDecimal("10000.00");
            this.interestRate = new BigDecimal("2.5"); // 2.5% annual interest
            this.minimumBalance = new BigDecimal("100.00");
        } else {
            this.dailyWithdrawalLimit = new BigDecimal("10000.00");
            this.dailyTransferLimit = new BigDecimal("50000.00");
            this.interestRate = BigDecimal.ZERO; // No interest for checking
            this.minimumBalance = BigDecimal.ZERO;
        }
    }

    /**
     * Pre-persist callback - Sets timestamps before saving to database
     * This ensures createdAt and updatedAt are set automatically
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public BigDecimal getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }

    public void setDailyWithdrawalLimit(BigDecimal dailyWithdrawalLimit) {
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }

    public BigDecimal getDailyTransferLimit() {
        return dailyTransferLimit;
    }

    public void setDailyTransferLimit(BigDecimal dailyTransferLimit) {
        this.dailyTransferLimit = dailyTransferLimit;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getLastLimitResetDate() {
        return lastLimitResetDate;
    }

    public void setLastLimitResetDate(LocalDateTime lastLimitResetDate) {
        this.lastLimitResetDate = lastLimitResetDate;
    }

    /**
     * Check if account is active and can perform transactions
     * 
     * @return true if account is ACTIVE, false otherwise
     */
    public boolean isActive() {
        return status == AccountStatus.ACTIVE;
    }

    /**
     * Check if account is a savings account
     * 
     * @return true if account type is SAVINGS, false otherwise
     */
    public boolean isSavingsAccount() {
        return accountType == AccountType.SAVINGS;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", holderName='" + holderName + '\'' +
                ", accountType=" + accountType +
                ", status=" + status +
                ", balance=" + balance +
                ", interestRate=" + interestRate +
                ", createdAt=" + createdAt +
                '}';
    }
}
