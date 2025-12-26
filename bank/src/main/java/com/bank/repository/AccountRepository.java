package com.bank.repository;

import com.bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Account Repository - Data access layer for Account entities
 * Extends JpaRepository to provide CRUD operations and custom queries
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Find an account by its account number
     * This is used to look up accounts when processing transactions
     * 
     * @param accountNumber The unique account number to search for
     * @return Optional containing the account if found, empty otherwise
     */
    Optional<Account> findByAccountNumber(String accountNumber);

    /**
     * Check if an account exists with the given account number
     * Useful for validation before creating new accounts
     * 
     * @param accountNumber The account number to check
     * @return true if account exists, false otherwise
     */
    boolean existsByAccountNumber(String accountNumber);
}

