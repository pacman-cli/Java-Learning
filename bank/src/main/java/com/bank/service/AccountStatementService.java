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
 * Account Statement Service - Generates account statements with various filters
 * <p>
 * Features: - Generate statements for date ranges - Filter by transaction type - Filter by amount range - Calculate
 * summary statistics - Export formatted statements
 */
@Service
@Transactional(readOnly = true)
public class AccountStatementService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Constructor injection
     *
     * @param accountRepository     Repository for account data access
     * @param transactionRepository Repository for transaction data access
     */
    @Autowired
    public AccountStatementService(AccountRepository accountRepository,
                                   TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Generate account statement for a date range
     *
     * @param accountNumber The account number
     * @param startDate     Start date (inclusive)
     * @param endDate       End date (inclusive)
     * @return List of transactions in the date range
     * @throws IllegalArgumentException if account not found
     */
    public List<Transaction> generateStatement(String accountNumber, LocalDate startDate, LocalDate endDate) {
        // Step 1: Find the account
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountNumber));

        // Step 2: Get all transactions for the account
        List<Transaction> allTransactions = transactionRepository.findByAccount(account);

        // Step 3: Filter by date range
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        return allTransactions.stream()
                .filter(t -> !t.getTransactionDate().isBefore(startDateTime) &&
                        !t.getTransactionDate().isAfter(endDateTime))
                .sorted(
                        (t1, t2) -> t2.getTransactionDate()
                                .compareTo(t1.getTransactionDate())
                ) // Most recent first
                .collect(Collectors.toList());
    }

    /**
     * Generate account statement for the current month
     *
     * @param accountNumber The account number
     * @return List of transactions for current month
     */
    public List<Transaction> generateMonthlyStatement(String accountNumber) {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());
        return generateStatement(accountNumber, startOfMonth, endOfMonth);
    }

    /**
     * Generate account statement filtered by transaction type
     *
     * @param accountNumber   The account number
     * @param transactionType The transaction type to filter by
     * @return List of transactions of the specified type
     */
    public List<Transaction> generateStatementByType(String accountNumber,
                                                     Transaction.TransactionType transactionType) {
        // Step 1: Find the account
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountNumber));

        // Step 2: Get all transactions for the account
        List<Transaction> allTransactions = transactionRepository.findByAccount(account);

        // Step 3: Filter by transaction type
        return allTransactions.stream()
                .filter(t -> t.getType() == transactionType)
                .sorted((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()))
                .collect(Collectors.toList());
    }

    /**
     * Generate account statement filtered by amount range
     *
     * @param accountNumber The account number
     * @param minAmount     Minimum amount (inclusive)
     * @param maxAmount     Maximum amount (inclusive)
     * @return List of transactions within the amount range
     */
    public List<Transaction> generateStatementByAmountRange(String accountNumber,
                                                            BigDecimal minAmount,
                                                            BigDecimal maxAmount) {
        // Step 1: Find the account
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountNumber));

        // Step 2: Get all transactions for the account
        List<Transaction> allTransactions = transactionRepository.findByAccount(account);

        // Step 3: Filter by amount range
        return allTransactions.stream()
                .filter(t -> t.getAmount().compareTo(minAmount) >= 0 &&
                        t.getAmount().compareTo(maxAmount) <= 0)
                .sorted((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()))
                .collect(Collectors.toList());
    }

    /**
     * Calculate statement summary statistics
     *
     * @param transactions List of transactions
     * @return StatementSummary containing statistics
     */
    public StatementSummary calculateSummary(List<Transaction> transactions) {
        StatementSummary summary = new StatementSummary();

        // Step 1: Filter only successful transactions
        List<Transaction> successfulTransactions = transactions.stream()
                .filter(Transaction::isSuccessful)
                .collect(Collectors.toList());

        // Step 2: Calculate totals by type
        BigDecimal totalDeposits = successfulTransactions.stream()
                .filter(t -> t.getType() == Transaction.TransactionType.DEPOSIT)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalWithdrawals = successfulTransactions.stream()
                .filter(t -> t.getType() == Transaction.TransactionType.WITHDRAWAL)
                .map(t -> t.getAmount().add(t.getFee() != null ? t.getFee() : BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalTransfersOut = successfulTransactions.stream()
                .filter(t -> t.getType() == Transaction.TransactionType.TRANSFER &&
                        t.getFromAccount() != null)
                .map(t -> t.getAmount().add(t.getFee() != null ? t.getFee() : BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalTransfersIn = successfulTransactions.stream()
                .filter(t -> t.getType() == Transaction.TransactionType.TRANSFER &&
                        t.getToAccount() != null)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalFees = successfulTransactions.stream()
                .map(t -> t.getFee() != null ? t.getFee() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Step 3: Set summary values
        summary.totalTransactions = successfulTransactions.size();
        summary.totalDeposits = totalDeposits;
        summary.totalWithdrawals = totalWithdrawals;
        summary.totalTransfersOut = totalTransfersOut;
        summary.totalTransfersIn = totalTransfersIn;
        summary.totalFees = totalFees;
        summary.netAmount = totalDeposits.add(totalTransfersIn)
                .subtract(totalWithdrawals)
                .subtract(totalTransfersOut);

        return summary;
    }

    /**
     * Inner class to hold statement summary statistics
     */
    public static class StatementSummary {
        public int totalTransactions;
        public BigDecimal totalDeposits = BigDecimal.ZERO;
        public BigDecimal totalWithdrawals = BigDecimal.ZERO;
        public BigDecimal totalTransfersOut = BigDecimal.ZERO;
        public BigDecimal totalTransfersIn = BigDecimal.ZERO;
        public BigDecimal totalFees = BigDecimal.ZERO;
        public BigDecimal netAmount = BigDecimal.ZERO;

        @Override
        public String toString() {
            return "StatementSummary{" +
                    "totalTransactions=" + totalTransactions +
                    ", totalDeposits=" + totalDeposits +
                    ", totalWithdrawals=" + totalWithdrawals +
                    ", totalTransfersOut=" + totalTransfersOut +
                    ", totalTransfersIn=" + totalTransfersIn +
                    ", totalFees=" + totalFees +
                    ", netAmount=" + netAmount +
                    '}';
        }
    }
}
