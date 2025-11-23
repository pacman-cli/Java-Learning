package com.bank.repository;

import com.bank.model.Account;
import com.bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Transaction Repository - Data access layer for Transaction entities
 * Provides methods to query transaction history
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Find all transactions for a specific account
     * This includes both incoming and outgoing transactions
     * 
     * @param account The account to get transactions for
     * @return List of all transactions involving this account
     */
    @Query("SELECT t FROM Transaction t WHERE t.fromAccount = :account OR t.toAccount = :account ORDER BY t.transactionDate DESC")
    List<Transaction> findByAccount(@Param("account") Account account);

    /**
     * Find all transactions where the account is the source (outgoing)
     * This includes withdrawals and transfers where money leaves the account
     * 
     * @param account The account to get outgoing transactions for
     * @return List of transactions where this account is the source
     */
    List<Transaction> findByFromAccountOrderByTransactionDateDesc(Account account);

    /**
     * Find all transactions where the account is the destination (incoming)
     * This includes deposits and transfers where money enters the account
     * 
     * @param account The account to get incoming transactions for
     * @return List of transactions where this account is the destination
     */
    List<Transaction> findByToAccountOrderByTransactionDateDesc(Account account);

    /**
     * Find all successful transactions for an account
     * Useful for generating account statements
     * 
     * @param account The account to get successful transactions for
     * @return List of successful transactions involving this account
     */
    @Query("SELECT t FROM Transaction t WHERE (t.fromAccount = :account OR t.toAccount = :account) AND t.successful = true ORDER BY t.transactionDate DESC")
    List<Transaction> findSuccessfulTransactionsByAccount(@Param("account") Account account);
}

