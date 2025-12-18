package com.bank.service;

import com.bank.model.Account;
import com.bank.model.User;
import com.bank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Account Service - Business logic layer for account operations Handles account creation, retrieval, and balance
 * queries
 */
@Service
@Transactional(readOnly = true) // Default to read-only transactions for better performance
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * Constructor injection - Spring will automatically provide AccountRepository
     *
     * @param accountRepository The repository for account data access
     */
    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Create a new bank account with initial deposit Enhanced: Requires user, account type, and PIN
     *
     * @param accountNumber  Unique account number
     * @param holderName     Account holder's name
     * @param accountType    Type of account (SAVINGS or CHECKING)
     * @param pin            4-digit PIN for security
     * @param initialBalance Initial deposit amount (must be >= 0)
     * @param user           User who owns this account
     * @return The newly created account
     * @throws IllegalArgumentException if account number already exists, balance is negative, or PIN is invalid
     */
    @Transactional
    public Account createAccount(
            String accountNumber,
            String holderName,
            Account.AccountType accountType,
            String pin,
            BigDecimal initialBalance,
            User user) {
        // Step 1: Validate initial balance is not negative
        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }

        // Step 2: Check if account number already exists
        if (accountRepository.existsByAccountNumber(accountNumber)) {
            throw new IllegalArgumentException("Account number already exists: " + accountNumber);
        }

        // Step 3: Validate PIN (should be 4 digits)
        if (pin == null || pin.length() != 4 || !pin.matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN must be exactly 4 digits");
        }

        // Step 4: Validate holder name is not empty
        if (holderName == null || holderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Holder name cannot be empty");
        }

        // Step 5: Create and save the new account
        Account account = new Account(accountNumber, holderName, accountType, pin, initialBalance, user);
        Account savedAccount = accountRepository.save(account);

        // Step 6: Add account to user's account list
        user.addAccount(savedAccount);

        return savedAccount;
    }

    /**
     * Update account status
     *
     * @param accountNumber The account number
     * @param status        New account status
     * @return Updated account
     * @throws IllegalArgumentException if account not found
     */
    @Transactional
    public Account updateAccountStatus(String accountNumber, Account.AccountStatus status) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountNumber));
        account.setStatus(status);
        return accountRepository.save(account);
    }

    /**
     * Get accounts for a user
     *
     * @param user The user
     * @return List of accounts belonging to this user
     */
    public List<Account> getAccountsByUser(User user) {
        return accountRepository.findAll().stream()
                .filter(account -> account.getUser() != null && account.getUser().getId().equals(user.getId()))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Find an account by its account number Read-only operation - no database modifications
     *
     * @param accountNumber The account number to search for
     * @return Optional containing the account if found
     */
    public Optional<Account> getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    /**
     * Get account by ID Read-only operation
     *
     * @param id The account ID
     * @return Optional containing the account if found
     */
    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    /**
     * Get all accounts in the system Read-only operation
     *
     * @return List of all accounts
     */
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    /**
     * Get current balance of an account Read-only operation
     *
     * @param accountNumber The account number
     * @return The current balance
     * @throws IllegalArgumentException if account not found
     */
    public BigDecimal getBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountNumber));
        return account.getBalance();
    }

    /**
     * Check if an account exists Read-only operation
     *
     * @param accountNumber The account number to check
     * @return true if account exists, false otherwise
     */
    public boolean accountExists(String accountNumber) {
        return accountRepository.existsByAccountNumber(accountNumber);
    }
}
