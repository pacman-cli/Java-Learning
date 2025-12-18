package com.bank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction Entity - Represents a financial transaction in the banking system
 * Tracks all deposits, withdrawals, and transfers between accounts
 */
@Entity
@Table(name = "transactions")
public class Transaction {
    /**
     * Transaction types enum - Defines valid transaction operations
     */
    public enum TransactionType {
        DEPOSIT,
        WITHDRAWAL,
        TRANSFER;
    }

    /**
     * Primary key - Auto-generated transaction ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Type of transaction (DEPOSIT, WITHDRAWAL, or TRANSFER)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Transaction type cannot be null")
    private TransactionType type;

    /**
     * Amount involved in the transaction - Must be positive
     * Using BigDecimal for precise decimal calculations
     */
    @Column(nullable = false, precision = 19, scale = 2)
    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
    private BigDecimal amount;

    /**
     * Source account - The account from which money is debited
     * For DEPOSIT, this is null (money comes from external source)
     * For WITHDRAWAL and TRANSFER, this is the account losing money
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;

    /**
     * Destination account - The account to which money is credited
     * For WITHDRAWAL, this is null (money goes to external destination)
     * For DEPOSIT and TRANSFER, this is the account receiving money
     */
    // why using lazy instead of eager? ans:to improve performance by avoiding
    // unnecessary loading of data when we don't need the data immediately
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id")
    private Account toAccount;

    /**
     * Description or note about the transaction
     */
    @Column(length = 500)
    private String description;

    /**
     * Timestamp when transaction was executed
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime transactionDate;

    /**
     * Transaction status - Whether the transaction was successful
     * This helps track failed transactions for audit purposes
     */
    @Column(nullable = false)
    private boolean successful;

    /**
     * Error message if transaction failed
     */
    @Column(length = 1000)
    private String errorMessage;

    /**
     * Transaction fee - Fee charged for this transaction
     * Some transactions may have fees (e.g., transfer fees, withdrawal fees)
     */
    @Column(precision = 19, scale = 2)
    private BigDecimal fee;

    /**
     * Default constructor - Required by JPA
     */
    public Transaction() {
        this.fee = BigDecimal.ZERO; // Default to no fee
    }

    /**
     * Constructor for creating a new transaction
     * 
     * @param type        Type of transaction
     * @param amount      Transaction amount
     * @param fromAccount Source account (can be null for deposits)
     * @param toAccount   Destination account (can be null for withdrawals)
     * @param description Transaction description
     */
    public Transaction(TransactionType type, BigDecimal amount,
            Account fromAccount, Account toAccount, String description) {
        this.type = type;
        this.amount = amount;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.description = description;
        this.transactionDate = LocalDateTime.now();
        this.successful = false; // Will be set to true when transaction completes
        this.fee = BigDecimal.ZERO; // Default to no fee
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    /**
     * Get the total amount including fee (for debits)
     * 
     * @return Amount plus fee
     */
    public BigDecimal getTotalAmount() {
        return amount.add(fee != null ? fee : BigDecimal.ZERO);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", type=" + type +
                ", amount=" + amount +
                ", fromAccount=" + (fromAccount != null ? fromAccount.getAccountNumber() : "N/A") +
                ", toAccount=" + (toAccount != null ? toAccount.getAccountNumber() : "N/A") +
                ", description='" + description + '\'' +
                ", transactionDate=" + transactionDate +
                ", successful=" + successful +
                '}';
    }
}
