package com.bank.service;

import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Transaction Service - Business logic layer for all banking transactions Implements proper transactional rules and
 * ensures data consistency
 * <p>
 * Enhanced Transactional Rules: 1. No account can have negative balance 2. All transactions must be recorded in the
 * transaction history 3. Transfer operations must be atomic (both accounts updated or neither) 4. Amount must be
 * positive and greater than zero 5. Source and destination accounts must exist and be valid 6. Account must be ACTIVE
 * to perform transactions 7. PIN verification required for withdrawals and transfers 8. Daily transaction limits must
 * be respected 9. Transaction fees may apply to certain operations
 */
@Service
@Transactional(readOnly = true) // Default to read-only for better performance
public class TransactionService {

    // Inject repositories for database operations
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Constructor injection - Spring will automatically provide repositories
     *
     * @param accountRepository     Repository for account data access
     * @param transactionRepository Repository for transaction data access
     */
    @Autowired
    public TransactionService(AccountRepository accountRepository,
                              TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Deposit money into an account Enhanced: Checks account status before deposit
     *
     * @param accountNumber The account number to deposit into
     * @param amount        The amount to deposit (must be > 0)
     * @param description   Optional description of the deposit
     * @return The created transaction record
     * @throws IllegalArgumentException if account not found, inactive, or amount is invalid
     */
    @Transactional
    public Transaction deposit(String accountNumber, BigDecimal amount, String description) {
        // Step 1: Validate amount is positive
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            Transaction failedTransaction = createFailedTransaction(
                    Transaction.TransactionType.DEPOSIT, amount, null, null,
                    description, "Invalid amount: must be greater than zero");
            transactionRepository.save(failedTransaction);
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }

        // Step 2: Find and validate the account
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
                    Transaction failedTransaction = createFailedTransaction(
                            Transaction.TransactionType.DEPOSIT, amount, null, null,
                            description, "Account not found: " + accountNumber);
                    transactionRepository.save(failedTransaction);
                    return new IllegalArgumentException("Account not found: " + accountNumber);
                });

        // Step 3: Check account status (Enhanced Rule: Account must be ACTIVE)
        if (!account.isActive()) {
            Transaction failedTransaction = createFailedTransaction(
                    Transaction.TransactionType.DEPOSIT, amount, null, account,
                    description, "Account is not active. Status: " + account.getStatus());
            transactionRepository.save(failedTransaction);
            throw new IllegalArgumentException("Account is not active. Status: " + account.getStatus());
        }

        // Step 4: Create transaction record (no fee for deposits)
        Transaction transaction = new Transaction(
                Transaction.TransactionType.DEPOSIT,
                amount,
                null, // No source account for deposits
                account,
                description != null ? description : "Deposit");
        transaction.setFee(BigDecimal.ZERO); // No fee for deposits

        // Step 5: Update account balance (add amount)
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);
        accountRepository.save(account);

        // Step 6: Mark transaction as successful and save
        transaction.setSuccessful(true);
        return transactionRepository.save(transaction);
    }

    /**
     * Withdraw money from an account Enhanced: PIN verification, account status check, daily limit check, withdrawal
     * fee
     *
     * @param accountNumber The account number to withdraw from
     * @param pin           PIN for verification
     * @param amount        The amount to withdraw (must be > 0)
     * @param description   Optional description of the withdrawal
     * @return The created transaction record
     * @throws IllegalArgumentException if account not found, PIN incorrect, insufficient funds, daily limit exceeded,
     *                                  or invalid amount
     */
    @Transactional
    public Transaction withdraw(String accountNumber, String pin, BigDecimal amount, String description) {
        // Step 1: Validate amount is positive
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            Transaction failedTransaction = createFailedTransaction(
                    Transaction.TransactionType.WITHDRAWAL, amount, null, null,
                    description, "Invalid amount: must be greater than zero");
            transactionRepository.save(failedTransaction);
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }

        // Step 2: Find and validate the account
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
                    Transaction failedTransaction = createFailedTransaction(
                            Transaction.TransactionType.WITHDRAWAL, amount, null, null,
                            description, "Account not found: " + accountNumber);
                    transactionRepository.save(failedTransaction);
                    return new IllegalArgumentException("Account not found: " + accountNumber);
                });

        // Step 3: Verify PIN (Enhanced Rule: PIN required for withdrawals)
        if (!account.getPin().equals(pin)) {
            Transaction failedTransaction = createFailedTransaction(
                    Transaction.TransactionType.WITHDRAWAL, amount, account, null,
                    description, "Invalid PIN");
            transactionRepository.save(failedTransaction);
            throw new IllegalArgumentException("Invalid PIN");
        }

        // Step 4: Check account status (Enhanced Rule: Account must be ACTIVE)
        if (!account.isActive()) {
            Transaction failedTransaction = createFailedTransaction(
                    Transaction.TransactionType.WITHDRAWAL, amount, account, null,
                    description, "Account is not active. Status: " + account.getStatus());
            transactionRepository.save(failedTransaction);
            throw new IllegalArgumentException("Account is not active. Status: " + account.getStatus());
        }

        // Step 5: Check daily withdrawal limit (Enhanced Rule: Daily limits)
        resetDailyLimitsIfNeeded(account);
        BigDecimal dailyWithdrawn = getDailyWithdrawalAmount(account);
        BigDecimal remainingLimit = account.getDailyWithdrawalLimit().subtract(dailyWithdrawn);
        if (amount.compareTo(remainingLimit) > 0) {
            Transaction failedTransaction = createFailedTransaction(
                    Transaction.TransactionType.WITHDRAWAL, amount, account, null,
                    description, "Daily withdrawal limit exceeded. Remaining limit: " + remainingLimit);
            transactionRepository.save(failedTransaction);
            throw new IllegalArgumentException("Daily withdrawal limit exceeded. Remaining limit: " + remainingLimit);
        }

        // Step 6: Calculate withdrawal fee (Enhanced: Transaction fees)
        BigDecimal fee = calculateWithdrawalFee(amount);
        BigDecimal totalAmount = amount.add(fee);

        // Step 7: Check sufficient balance including fee (Enhanced Rule: No negative
        // balance)
        BigDecimal currentBalance = account.getBalance();
        if (currentBalance.compareTo(totalAmount) < 0) {
            Transaction failedTransaction = createFailedTransaction(
                    Transaction.TransactionType.WITHDRAWAL, amount, account, null,
                    description,
                    "Insufficient funds. Current balance: " + currentBalance + ", Required: " + totalAmount);
            transactionRepository.save(failedTransaction);
            throw new IllegalArgumentException(
                    "Insufficient funds. Current balance: " + currentBalance + ", Required (amount + fee): "
                            + totalAmount);
        }

        // Step 8: Create transaction record with fee
        Transaction transaction = new Transaction(
                Transaction.TransactionType.WITHDRAWAL,
                amount,
                account,
                null, // No destination account for withdrawals
                description != null ? description : "Withdrawal");
        transaction.setFee(fee);

        // Step 9: Update account balance (subtract amount + fee)
        BigDecimal newBalance = currentBalance.subtract(totalAmount);
        account.setBalance(newBalance);
        accountRepository.save(account);

        // Step 10: Mark transaction as successful and save
        transaction.setSuccessful(true);
        return transactionRepository.save(transaction);
    }

    /**
     * Transfer money from one account to another Enhanced: PIN verification, account status checks, daily limit check,
     * transfer fee Transactional Rule: Must be atomic - both accounts updated or neither
     *
     * @param fromAccountNumber Source account number
     * @param pin               PIN for verification
     * @param toAccountNumber   Destination account number
     * @param amount            Amount to transfer (must be > 0)
     * @param description       Optional description of the transfer
     * @return The created transaction record
     * @throws IllegalArgumentException if accounts not found, PIN incorrect, insufficient funds, daily limit exceeded,
     *                                  or invalid amount
     */
    @Transactional // Critical: Ensures atomicity - both accounts updated or transaction rolled
    // back
    public Transaction transfer(String fromAccountNumber, String pin, String toAccountNumber,
                                BigDecimal amount, String description) {
        // Step 1: Validate amount is positive
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            Transaction failedTransaction = createFailedTransaction(
                    Transaction.TransactionType.TRANSFER, amount, null, null,
                    description, "Invalid amount: must be greater than zero");
            transactionRepository.save(failedTransaction);
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }

        // Step 2: Validate accounts are different
        if (fromAccountNumber.equals(toAccountNumber)) {
            Transaction failedTransaction = createFailedTransaction(
                    Transaction.TransactionType.TRANSFER, amount, null, null,
                    description, "Cannot transfer to the same account");
            transactionRepository.save(failedTransaction);
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        // Step 3: Find source account
        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> {
                    Transaction failedTransaction = createFailedTransaction(
                            Transaction.TransactionType.TRANSFER, amount, null, null,
                            description, "Source account not found: " + fromAccountNumber);
                    transactionRepository.save(failedTransaction);
                    return new IllegalArgumentException("Source account not found: " + fromAccountNumber);
                });

        // Step 4: Verify PIN (Enhanced Rule: PIN required for transfers)
        if (!fromAccount.getPin().equals(pin)) {
            Transaction failedTransaction = createFailedTransaction(
                    Transaction.TransactionType.TRANSFER, amount, fromAccount, null,
                    description, "Invalid PIN");
            transactionRepository.save(failedTransaction);
            throw new IllegalArgumentException("Invalid PIN");
        }

        // Step 5: Check source account status (Enhanced Rule: Account must be ACTIVE)
        if (!fromAccount.isActive()) {
            Transaction failedTransaction = createFailedTransaction(
                    Transaction.TransactionType.TRANSFER, amount, fromAccount, null,
                    description, "Source account is not active. Status: " + fromAccount.getStatus());
            transactionRepository.save(failedTransaction);
            throw new IllegalArgumentException("Source account is not active. Status: " + fromAccount.getStatus());
        }

        // Step 6: Find destination account
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> {
                    Transaction failedTransaction = createFailedTransaction(
                            Transaction.TransactionType.TRANSFER, amount, fromAccount, null,
                            description, "Destination account not found: " + toAccountNumber);
                    transactionRepository.save(failedTransaction);
                    return new IllegalArgumentException("Destination account not found: " + toAccountNumber);
                });

        // Step 7: Check destination account status (Enhanced Rule: Account must be
        // ACTIVE)
        if (!toAccount.isActive()) {
            Transaction failedTransaction = createFailedTransaction(
                    Transaction.TransactionType.TRANSFER, amount, fromAccount, toAccount,
                    description, "Destination account is not active. Status: " + toAccount.getStatus());
            transactionRepository.save(failedTransaction);
            throw new IllegalArgumentException("Destination account is not active. Status: " + toAccount.getStatus());
        }

        // Step 8: Check daily transfer limit (Enhanced Rule: Daily limits)
        resetDailyLimitsIfNeeded(fromAccount);
        BigDecimal dailyTransferred = getDailyTransferAmount(fromAccount);
        BigDecimal remainingLimit = fromAccount.getDailyTransferLimit().subtract(dailyTransferred);
        if (amount.compareTo(remainingLimit) > 0) {
            Transaction failedTransaction = createFailedTransaction(
                    Transaction.TransactionType.TRANSFER, amount, fromAccount, toAccount,
                    description, "Daily transfer limit exceeded. Remaining limit: " + remainingLimit);
            transactionRepository.save(failedTransaction);
            throw new IllegalArgumentException("Daily transfer limit exceeded. Remaining limit: " + remainingLimit);
        }

        // Step 9: Calculate transfer fee (Enhanced: Transaction fees)
        BigDecimal fee = calculateTransferFee(amount);
        BigDecimal totalAmount = amount.add(fee);

        // Step 10: Check sufficient balance including fee (Enhanced Rule: No negative
        // balance)
        BigDecimal fromBalance = fromAccount.getBalance();
        if (fromBalance.compareTo(totalAmount) < 0) {
            Transaction failedTransaction = createFailedTransaction(
                    Transaction.TransactionType.TRANSFER, amount, fromAccount, toAccount,
                    description, "Insufficient funds in source account. Current balance: " + fromBalance
                            + ", Required: " + totalAmount);
            transactionRepository.save(failedTransaction);
            throw new IllegalArgumentException(
                    "Insufficient funds in source account. Current balance: " + fromBalance
                            + ", Required (amount + fee): " + totalAmount);
        }

        // Step 11: Create transaction record with fee
        Transaction transaction = new Transaction(
                Transaction.TransactionType.TRANSFER,
                amount,
                fromAccount,
                toAccount,
                description != null ? description : "Transfer from " + fromAccountNumber + " to " + toAccountNumber);
        transaction.setFee(fee);

        // Step 12: Update both account balances atomically
        // If any step fails, entire transaction rolls back due to @Transactional
        BigDecimal newFromBalance = fromBalance.subtract(totalAmount); // Subtract amount + fee
        fromAccount.setBalance(newFromBalance);
        accountRepository.save(fromAccount);

        BigDecimal newToBalance = toAccount.getBalance().add(amount); // Only add amount (fee stays with source)
        toAccount.setBalance(newToBalance);
        accountRepository.save(toAccount);

        // Step 13: Mark transaction as successful and save
        transaction.setSuccessful(true);
        return transactionRepository.save(transaction);
    }

    /**
     * Get transaction history for an account Read-only operation
     *
     * @param accountNumber The account number
     * @return List of all transactions for this account
     * @throws IllegalArgumentException if account not found
     */
    public List<Transaction> getTransactionHistory(String accountNumber) {
        // Find the account
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountNumber));

        // Return all transactions for this account (both incoming and outgoing)
        return transactionRepository.findByAccount(account);
    }

    /**
     * Get only successful transactions for an account Read-only operation
     *
     * @param accountNumber The account number
     * @return List of successful transactions for this account
     * @throws IllegalArgumentException if account not found
     */
    public List<Transaction> getSuccessfulTransactionHistory(String accountNumber) {
        // Find the account
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountNumber));

        // Return only successful transactions
        return transactionRepository.findSuccessfulTransactionsByAccount(account);
    }

    /**
     * Calculate withdrawal fee Fee structure: 1% of amount, minimum $1, maximum $10
     *
     * @param amount Withdrawal amount
     * @return Calculated fee
     */
    private BigDecimal calculateWithdrawalFee(BigDecimal amount) {
        // Step 1: Calculate 1% of amount
        BigDecimal fee = amount.multiply(new BigDecimal("0.01"));

        // Step 2: Apply minimum fee of $1
        if (fee.compareTo(new BigDecimal("1.00")) < 0) {
            fee = new BigDecimal("1.00");
        }

        // Step 3: Apply maximum fee of $10
        if (fee.compareTo(new BigDecimal("10.00")) > 0) {
            fee = new BigDecimal("10.00");
        }

        return fee.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Calculate transfer fee Fee structure: 0.5% of amount, minimum $0.50, maximum $5
     *
     * @param amount Transfer amount
     * @return Calculated fee
     */
    private BigDecimal calculateTransferFee(BigDecimal amount) {
        // Step 1: Calculate 0.5% of amount
        BigDecimal fee = amount.multiply(new BigDecimal("0.005"));

        // Step 2: Apply minimum fee of $0.50
        if (fee.compareTo(new BigDecimal("0.50")) < 0) {
            fee = new BigDecimal("0.50");
        }

        // Step 3: Apply maximum fee of $5
        if (fee.compareTo(new BigDecimal("5.00")) > 0) {
            fee = new BigDecimal("5.00");
        }

        return fee.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Get total amount withdrawn today for an account
     *
     * @param account The account
     * @return Total amount withdrawn today
     */
    private BigDecimal getDailyWithdrawalAmount(Account account) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        List<Transaction> todayWithdrawals = transactionRepository.findByFromAccountOrderByTransactionDateDesc(account)
                .stream()
                .filter(t -> t.getType() == Transaction.TransactionType.WITHDRAWAL &&
                        t.isSuccessful() &&
                        !t.getTransactionDate().isBefore(startOfDay) &&
                        !t.getTransactionDate().isAfter(endOfDay))
                .collect(java.util.stream.Collectors.toList());

        return todayWithdrawals.stream()
                .map(t -> t.getAmount().add(t.getFee() != null ? t.getFee() : BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Get total amount transferred today from an account
     *
     * @param account The account
     * @return Total amount transferred today
     */
    private BigDecimal getDailyTransferAmount(Account account) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        //today's transfers finding
        List<Transaction> todayTransfers = transactionRepository.findByFromAccountOrderByTransactionDateDesc(account)
                .stream()
                .filter(t -> t.getType() == Transaction.TransactionType.TRANSFER &&
                        t.isSuccessful() &&
                        !t.getTransactionDate().isBefore(startOfDay) &&
                        !t.getTransactionDate().isAfter(endOfDay))
                .collect(java.util.stream.Collectors.toList());

        return todayTransfers.stream()
                .map(t -> t.getAmount().add(t.getFee() != null ? t.getFee() : BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Reset daily limits if a new day has started Updates the lastLimitResetDate if needed
     *
     * @param account The account
     */
    private void resetDailyLimitsIfNeeded(Account account) {
        LocalDate today = LocalDate.now();
        LocalDate lastResetDate = account.getLastLimitResetDate() != null
                ? account.getLastLimitResetDate().toLocalDate()
                : null;

        // If last reset was not today, update the reset date
        if (lastResetDate == null || !lastResetDate.equals(today)) {
            account.setLastLimitResetDate(today.atStartOfDay());
            accountRepository.save(account);
        }
    }

    /**
     * Helper method to create a failed transaction record This ensures all transaction attempts are logged for audit
     * purposes
     *
     * @param type         Transaction type
     * @param amount       Transaction amount
     * @param fromAccount  Source account (can be null)
     * @param toAccount    Destination account (can be null)
     * @param description  Transaction description
     * @param errorMessage Error message explaining why transaction failed
     * @return Failed transaction record
     */
    private Transaction createFailedTransaction(Transaction.TransactionType type,
                                                BigDecimal amount,
                                                Account fromAccount,
                                                Account toAccount,
                                                String description,
                                                String errorMessage) {
        Transaction transaction = new Transaction(type, amount, fromAccount, toAccount, description);
        transaction.setSuccessful(false);
        transaction.setErrorMessage(errorMessage);
        transaction.setFee(BigDecimal.ZERO);
        return transaction;
    }
}
