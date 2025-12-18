package com.bank.service;

import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interest Calculation Service - Calculates and applies interest to savings accounts
 * 
 * Features:
 * - Calculates interest based on account balance and interest rate
 * - Applies interest monthly to savings accounts
 * - Can be scheduled to run automatically or called manually
 * 
 * Interest Calculation Formula:
 * Monthly Interest = (Principal × Annual Interest Rate) / 12
 */
@Service
@Transactional(readOnly = true)
public class InterestCalculationService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Constructor injection
     * @param accountRepository Repository for account data access
     * @param transactionRepository Repository for transaction data access
     */
    @Autowired
    public InterestCalculationService(AccountRepository accountRepository, 
                                     TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Calculate interest for a savings account
     * Formula: Monthly Interest = (Balance × Annual Interest Rate) / 12
     * 
     * @param account The savings account
     * @return The calculated interest amount
     */
    public BigDecimal calculateInterest(Account account) {
        // Step 1: Verify account is a savings account
        if (!account.isSavingsAccount()) {
            return BigDecimal.ZERO; // No interest for checking accounts
        }

        // Step 2: Verify account is active
        if (!account.isActive()) {
            return BigDecimal.ZERO; // No interest for inactive accounts
        }

        // Step 3: Get account balance and interest rate
        BigDecimal balance = account.getBalance();
        BigDecimal interestRate = account.getInterestRate();

        // Step 4: Check if interest rate is set
        if (interestRate == null || interestRate.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO; // No interest if rate is not set or zero
        }

        // Step 5: Calculate monthly interest
        // Formula: (Balance × Annual Interest Rate) / 12
        // Interest rate is stored as percentage (e.g., 2.5 for 2.5%), so divide by 100
        BigDecimal monthlyInterest = balance
                .multiply(interestRate)
                .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP) // Convert percentage to decimal
                .divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP); // Divide by 12 for monthly

        return monthlyInterest;
    }

    /**
     * Apply interest to a savings account
     * Creates a deposit transaction for the interest amount
     * 
     * @param account The savings account
     * @return The interest transaction created
     */
    @Transactional
    public Transaction applyInterest(Account account) {
        // Step 1: Calculate interest
        BigDecimal interestAmount = calculateInterest(account);

        // Step 2: Check if interest amount is greater than zero
        if (interestAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Cannot apply interest: amount is zero or negative");
        }

        // Step 3: Create interest deposit transaction
        Transaction interestTransaction = new Transaction(
            Transaction.TransactionType.DEPOSIT,
            interestAmount,
            null, // No source account for interest
            account,
            "Monthly Interest Payment - " + interestAmount + " at " + account.getInterestRate() + "%"
        );

        // Step 4: Update account balance (add interest)
        BigDecimal newBalance = account.getBalance().add(interestAmount);
        account.setBalance(newBalance);
        accountRepository.save(account);

        // Step 5: Mark transaction as successful and save
        interestTransaction.setSuccessful(true);
        return transactionRepository.save(interestTransaction);
    }

    /**
     * Apply interest to all eligible savings accounts
     * This method processes all active savings accounts and applies interest
     * 
     * @return Number of accounts that received interest
     */
    @Transactional
    public int applyInterestToAllAccounts() {
        // Step 1: Get all accounts
        List<Account> allAccounts = accountRepository.findAll();

        // Step 2: Counter for accounts that received interest
        int count = 0;

        // Step 3: Process each account
        for (Account account : allAccounts) {
            // Step 4: Check if account is eligible for interest
            if (account.isSavingsAccount() && account.isActive()) {
                BigDecimal interest = calculateInterest(account);
                
                // Step 5: Apply interest if amount is greater than zero
                if (interest.compareTo(BigDecimal.ZERO) > 0) {
                    try {
                        applyInterest(account);
                        count++;
                    } catch (Exception e) {
                        // Log error but continue processing other accounts
                        System.err.println("Error applying interest to account " + 
                                         account.getAccountNumber() + ": " + e.getMessage());
                    }
                }
            }
        }

        // Step 6: Return count of accounts that received interest
        return count;
    }

    /**
     * Scheduled method to apply interest monthly
     * This runs automatically on the first day of each month at midnight
     * 
     * Note: To enable scheduling, add @EnableScheduling to the main application class
     */
    @Scheduled(cron = "0 0 0 1 * ?") // First day of every month at midnight
    @Transactional
    public void scheduledMonthlyInterest() {
        System.out.println("Running scheduled monthly interest calculation...");
        int count = applyInterestToAllAccounts();
        System.out.println("Interest applied to " + count + " accounts.");
    }

    /**
     * Calculate interest for a specific period (for custom interest calculations)
     * 
     * @param account The savings account
     * @param months Number of months to calculate interest for
     * @return Total interest for the period
     */
    public BigDecimal calculateInterestForPeriod(Account account, int months) {
        // Step 1: Calculate monthly interest
        BigDecimal monthlyInterest = calculateInterest(account);

        // Step 2: Multiply by number of months
        return monthlyInterest.multiply(new BigDecimal(months));
    }
}

