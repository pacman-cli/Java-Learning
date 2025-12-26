package com.bank.controller;

import com.bank.model.*;
import com.bank.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Enhanced Terminal Controller - Console-based user interface for the banking
 * system
 * Implements CommandLineRunner to start automatically when Spring Boot starts
 * 
 * Features:
 * - User authentication (login/register)
 * - Account management with types (SAVINGS/CHECKING)
 * - PIN-secured transactions
 * - Beneficiary management
 * - Account statements with filters
 * - Interest calculation
 * - Transaction fees and daily limits
 */
@Component
public class TerminalController implements CommandLineRunner {

    // Inject all services
    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final BeneficiaryService beneficiaryService;
    private final AccountStatementService statementService;
    private final InterestCalculationService interestService;

    // Scanner for reading user input
    private final Scanner scanner;

    // Current logged-in user
    private User currentUser;

    /**
     * Constructor injection - Spring will automatically provide all services
     */
    @Autowired
    public TerminalController(UserService userService, AccountService accountService,
            TransactionService transactionService, BeneficiaryService beneficiaryService,
            AccountStatementService statementService, InterestCalculationService interestService) {
        this.userService = userService;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.beneficiaryService = beneficiaryService;
        this.statementService = statementService;
        this.interestService = interestService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Run method - Main entry point
     */
    @Override
    public void run(String... args) {
        System.out.println("\n========================================");
        System.out.println("   Enhanced Terminal Banking System");
        System.out.println("========================================\n");

        // Step 1: User authentication
        if (!authenticateUser()) {
            System.out.println("Authentication failed. Exiting...");
            return;
        }

        // Step 2: Main menu loop
        boolean running = true;
        while (running) {
            displayMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createAccount();
                    break;
                case "2":
                    depositMoney();
                    break;
                case "3":
                    withdrawMoney();
                    break;
                case "4":
                    transferMoney();
                    break;
                case "5":
                    checkBalance();
                    break;
                case "6":
                    viewTransactionHistory();
                    break;
                case "7":
                    manageBeneficiaries();
                    break;
                case "8":
                    viewAccountStatement();
                    break;
                case "9":
                    calculateInterest();
                    break;
                case "10":
                    listMyAccounts();
                    break;
                case "11":
                    accountManagement();
                    break;
                case "12":
                    System.out.println("\nThank you for using Enhanced Terminal Banking System. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("\nInvalid choice. Please try again.\n");
            }
        }

        scanner.close();
    }

    /**
     * User authentication - Login or Register
     */
    private boolean authenticateUser() {
        System.out.println("=== Authentication ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.print("Enter choice: ");
        String choice = scanner.nextLine().trim();

        if ("1".equals(choice)) {
            return login();
        } else if ("2".equals(choice)) {
            return register();
        } else {
            System.out.println("Invalid choice.");
            return false;
        }
    }

    /**
     * User login
     */
    private boolean login() {
        System.out.println("\n--- Login ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        Optional<User> userOpt = userService.authenticate(username, password);
        if (userOpt.isPresent()) {
            currentUser = userOpt.get();
            System.out.println("\n✓ Login successful! Welcome, " + currentUser.getFullName() + "!\n");
            return true;
        } else {
            System.out.println("✗ Login failed. Invalid username or password.\n");
            return false;
        }
    }

    /**
     * User registration
     */
    private boolean register() {
        System.out.println("\n--- Register New User ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Phone (optional): ");
        String phone = scanner.nextLine().trim();
        System.out.print("Address (optional): ");
        String address = scanner.nextLine().trim();

        try {
            User user = userService.registerUser(username, password, fullName, email);
            if (!phone.isEmpty())
                user.setPhoneNumber(phone);
            if (!address.isEmpty())
                user.setAddress(address);
            userService.updateUser(user);

            currentUser = user;
            System.out.println("\n✓ Registration successful! Welcome, " + currentUser.getFullName() + "!\n");
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Registration failed: " + e.getMessage() + "\n");
            return false;
        }
    }

    /**
     * Display main menu
     */
    private void displayMainMenu() {
        System.out.println("=== Main Menu ===");
        System.out.println("1. Create Account");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Transfer Money");
        System.out.println("5. Check Balance");
        System.out.println("6. View Transaction History");
        System.out.println("7. Manage Beneficiaries");
        System.out.println("8. View Account Statement");
        System.out.println("9. Calculate Interest");
        System.out.println("10. List My Accounts");
        System.out.println("11. Account Management");
        System.out.println("12. Exit");
        System.out.print("\nEnter your choice: ");
    }

    /**
     * Create a new account
     */
    private void createAccount() {
        System.out.println("\n--- Create New Account ---");

        try {
            System.out.print("Account Number: ");
            String accountNumber = scanner.nextLine().trim();

            if (accountNumber.isEmpty()) {
                System.out.println("Error: Account number cannot be empty.\n");
                return;
            }

            if (accountService.accountExists(accountNumber)) {
                System.out.println("Error: Account number already exists.\n");
                return;
            }

            System.out.print("Account Holder Name: ");
            String holderName = scanner.nextLine().trim();

            System.out.println("Account Type:");
            System.out.println("1. SAVINGS (earns interest)");
            System.out.println("2. CHECKING (for daily transactions)");
            System.out.print("Enter choice: ");
            String typeChoice = scanner.nextLine().trim();

            Account.AccountType accountType;
            if ("1".equals(typeChoice)) {
                accountType = Account.AccountType.SAVINGS;
            } else if ("2".equals(typeChoice)) {
                accountType = Account.AccountType.CHECKING;
            } else {
                System.out.println("Error: Invalid account type.\n");
                return;
            }

            System.out.print("PIN (4 digits): ");
            String pin = scanner.nextLine().trim();

            if (pin.length() != 4 || !pin.matches("\\d{4}")) {
                System.out.println("Error: PIN must be exactly 4 digits.\n");
                return;
            }

            System.out.print("Initial Deposit: $");
            String amountStr = scanner.nextLine().trim();
            BigDecimal initialBalance;
            try {
                initialBalance = new BigDecimal(amountStr);
                if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("Error: Initial deposit cannot be negative.\n");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid amount format.\n");
                return;
            }

            Account account = accountService.createAccount(accountNumber, holderName, accountType,
                    pin, initialBalance, currentUser);

            System.out.println("\n✓ Account created successfully!");
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Type: " + account.getAccountType());
            System.out.println("Status: " + account.getStatus());
            System.out.println("Balance: $" + account.getBalance());
            if (account.isSavingsAccount()) {
                System.out.println("Interest Rate: " + account.getInterestRate() + "%");
            }
            System.out.println();

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    /**
     * Deposit money
     */
    private void depositMoney() {
        System.out.println("\n--- Deposit Money ---");

        try {
            Account account = selectAccount();
            if (account == null)
                return;

            System.out.print("Deposit Amount: $");
            BigDecimal amount = parseAmount();
            if (amount == null)
                return;

            System.out.print("Description (optional): ");
            String description = scanner.nextLine().trim();

            Transaction transaction = transactionService.deposit(account.getAccountNumber(), amount,
                    description.isEmpty() ? null : description);

            Account updated = accountService.getAccountByNumber(account.getAccountNumber()).orElse(account);

            System.out.println("\n✓ Deposit successful!");
            System.out.println("Transaction ID: " + transaction.getId());
            System.out.println("Amount: $" + transaction.getAmount());
            System.out.println("New Balance: $" + updated.getBalance());
            System.out.println();

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    /**
     * Withdraw money
     */
    private void withdrawMoney() {
        System.out.println("\n--- Withdraw Money ---");

        try {
            Account account = selectAccount();
            if (account == null)
                return;

            System.out.print("PIN: ");
            String pin = scanner.nextLine().trim();

            System.out.print("Withdrawal Amount: $");
            BigDecimal amount = parseAmount();
            if (amount == null)
                return;

            System.out.print("Description (optional): ");
            String description = scanner.nextLine().trim();

            Transaction transaction = transactionService.withdraw(account.getAccountNumber(), pin, amount,
                    description.isEmpty() ? null : description);

            Account updated = accountService.getAccountByNumber(account.getAccountNumber()).orElse(account);

            System.out.println("\n✓ Withdrawal successful!");
            System.out.println("Transaction ID: " + transaction.getId());
            System.out.println("Amount: $" + transaction.getAmount());
            System.out.println("Fee: $" + transaction.getFee());
            System.out.println("Total Deducted: $" + transaction.getTotalAmount());
            System.out.println("New Balance: $" + updated.getBalance());
            System.out.println();

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    /**
     * Transfer money
     */
    private void transferMoney() {
        System.out.println("\n--- Transfer Money ---");

        try {
            Account fromAccount = selectAccount();
            if (fromAccount == null)
                return;

            System.out.print("PIN: ");
            String pin = scanner.nextLine().trim();

            System.out.println("Transfer to:");
            System.out.println("1. Enter account number");
            System.out.println("2. Select from beneficiaries");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            String toAccountNumber;
            if ("2".equals(choice)) {
                Beneficiary beneficiary = selectBeneficiary();
                if (beneficiary == null)
                    return;
                toAccountNumber = beneficiary.getAccount().getAccountNumber();
            } else {
                System.out.print("Destination Account Number: ");
                toAccountNumber = scanner.nextLine().trim();
            }

            System.out.print("Transfer Amount: $");
            BigDecimal amount = parseAmount();
            if (amount == null)
                return;

            System.out.print("Description (optional): ");
            String description = scanner.nextLine().trim();

            Transaction transaction = transactionService.transfer(fromAccount.getAccountNumber(), pin,
                    toAccountNumber, amount,
                    description.isEmpty() ? null : description);

            Account fromUpdated = accountService.getAccountByNumber(fromAccount.getAccountNumber()).orElse(fromAccount);
            Account toUpdated = accountService.getAccountByNumber(toAccountNumber).orElse(null);

            System.out.println("\n✓ Transfer successful!");
            System.out.println("Transaction ID: " + transaction.getId());
            System.out.println("Amount: $" + transaction.getAmount());
            System.out.println("Fee: $" + transaction.getFee());
            System.out.println(
                    "From: " + fromAccount.getAccountNumber() + " (Balance: $" + fromUpdated.getBalance() + ")");
            if (toUpdated != null) {
                System.out.println("To: " + toAccountNumber + " (Balance: $" + toUpdated.getBalance() + ")");
            }
            System.out.println();

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    /**
     * Check balance
     */
    private void checkBalance() {
        System.out.println("\n--- Check Balance ---");

        try {
            Account account = selectAccount();
            if (account == null)
                return;

            System.out.println("\n--- Account Information ---");
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Holder Name: " + account.getHolderName());
            System.out.println("Type: " + account.getAccountType());
            System.out.println("Status: " + account.getStatus());
            System.out.println("Balance: $" + account.getBalance());
            if (account.isSavingsAccount()) {
                System.out.println("Interest Rate: " + account.getInterestRate() + "%");
                System.out.println("Minimum Balance: $" + account.getMinimumBalance());
            }
            System.out.println("Daily Withdrawal Limit: $" + account.getDailyWithdrawalLimit());
            System.out.println("Daily Transfer Limit: $" + account.getDailyTransferLimit());
            System.out.println();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    /**
     * View transaction history
     */
    private void viewTransactionHistory() {
        System.out.println("\n--- Transaction History ---");

        try {
            Account account = selectAccount();
            if (account == null)
                return;

            List<Transaction> transactions = transactionService.getTransactionHistory(account.getAccountNumber());

            System.out.println("\n--- Transaction History for " + account.getAccountNumber() + " ---");
            System.out.println("Total Transactions: " + transactions.size());

            if (transactions.isEmpty()) {
                System.out.println("No transactions found.\n");
            } else {
                for (Transaction t : transactions) {
                    System.out.println("----------------------------------------");
                    System.out.println("ID: " + t.getId());
                    System.out.println("Type: " + t.getType());
                    System.out.println("Amount: $" + t.getAmount());
                    if (t.getFee() != null && t.getFee().compareTo(BigDecimal.ZERO) > 0) {
                        System.out.println("Fee: $" + t.getFee());
                    }
                    System.out.println("Status: " + (t.isSuccessful() ? "SUCCESS" : "FAILED"));
                    if (!t.isSuccessful() && t.getErrorMessage() != null) {
                        System.out.println("Error: " + t.getErrorMessage());
                    }
                    System.out.println("Date: " + t.getTransactionDate());
                    System.out.println("Description: " + (t.getDescription() != null ? t.getDescription() : "N/A"));
                }
                System.out.println("----------------------------------------\n");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    /**
     * Manage beneficiaries
     */
    private void manageBeneficiaries() {
        System.out.println("\n--- Manage Beneficiaries ---");
        System.out.println("1. Add Beneficiary");
        System.out.println("2. List Beneficiaries");
        System.out.println("3. Delete Beneficiary");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                addBeneficiary();
                break;
            case "2":
                listBeneficiaries();
                break;
            case "3":
                deleteBeneficiary();
                break;
            default:
                System.out.println("Invalid choice.\n");
        }
    }

    private void addBeneficiary() {
        System.out.print("Beneficiary Account Number: ");
        String accountNumber = scanner.nextLine().trim();
        System.out.print("Nickname: ");
        String nickname = scanner.nextLine().trim();

        try {
            Beneficiary beneficiary = beneficiaryService.addBeneficiary(currentUser, accountNumber, nickname);
            System.out.println("\n✓ Beneficiary added: " + beneficiary.getNickname() + "\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    private void listBeneficiaries() {
        List<Beneficiary> beneficiaries = beneficiaryService.getBeneficiaries(currentUser);
        if (beneficiaries.isEmpty()) {
            System.out.println("No beneficiaries found.\n");
        } else {
            System.out.println("\n--- Your Beneficiaries ---");
            for (Beneficiary b : beneficiaries) {
                System.out.println("ID: " + b.getId() + " | " + b.getNickname() +
                        " | Account: " + b.getAccount().getAccountNumber() + "\n");
            }
        }
    }

    private void deleteBeneficiary() {
        listBeneficiaries();
        System.out.print("Enter Beneficiary ID to delete: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            beneficiaryService.deleteBeneficiary(id, currentUser);
            System.out.println("✓ Beneficiary deleted.\n");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    /**
     * View account statement
     */
    private void viewAccountStatement() {
        System.out.println("\n--- Account Statement ---");

        try {
            Account account = selectAccount();
            if (account == null)
                return;

            System.out.println("Statement Options:");
            System.out.println("1. Current Month");
            System.out.println("2. Date Range");
            System.out.println("3. By Transaction Type");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            List<Transaction> transactions;
            if ("1".equals(choice)) {
                transactions = statementService.generateMonthlyStatement(account.getAccountNumber());
            } else if ("2".equals(choice)) {
                System.out.print("Start Date (YYYY-MM-DD): ");
                LocalDate startDate = parseDate();
                System.out.print("End Date (YYYY-MM-DD): ");
                LocalDate endDate = parseDate();
                transactions = statementService.generateStatement(account.getAccountNumber(), startDate, endDate);
            } else if ("3".equals(choice)) {
                System.out.println("1. DEPOSIT  2. WITHDRAWAL  3. TRANSFER");
                System.out.print("Choice: ");
                String typeChoice = scanner.nextLine().trim();
                Transaction.TransactionType type = switch (typeChoice) {
                    case "1" -> Transaction.TransactionType.DEPOSIT;
                    case "2" -> Transaction.TransactionType.WITHDRAWAL;
                    case "3" -> Transaction.TransactionType.TRANSFER;
                    default -> null;
                };
                if (type == null) {
                    System.out.println("Invalid type.\n");
                    return;
                }
                transactions = statementService.generateStatementByType(account.getAccountNumber(), type);
            } else {
                System.out.println("Invalid choice.\n");
                return;
            }

            AccountStatementService.StatementSummary summary = statementService.calculateSummary(transactions);

            System.out.println("\n--- Account Statement ---");
            System.out.println("Total Transactions: " + summary.totalTransactions);
            System.out.println("Total Deposits: $" + summary.totalDeposits);
            System.out.println("Total Withdrawals: $" + summary.totalWithdrawals);
            System.out.println("Total Transfers Out: $" + summary.totalTransfersOut);
            System.out.println("Total Transfers In: $" + summary.totalTransfersIn);
            System.out.println("Total Fees: $" + summary.totalFees);
            System.out.println("Net Amount: $" + summary.netAmount);
            System.out.println();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    /**
     * Calculate interest
     */
    private void calculateInterest() {
        System.out.println("\n--- Calculate Interest ---");

        try {
            Account account = selectAccount();
            if (account == null)
                return;

            if (!account.isSavingsAccount()) {
                System.out.println("Error: Interest calculation is only available for savings accounts.\n");
                return;
            }

            System.out.println("1. Calculate monthly interest");
            System.out.println("2. Apply interest to account");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            if ("1".equals(choice)) {
                BigDecimal interest = interestService.calculateInterest(account);
                System.out.println("\nMonthly Interest: $" + interest);
                System.out.println("Annual Interest Rate: " + account.getInterestRate() + "%");
                System.out.println("Current Balance: $" + account.getBalance());
                System.out.println();
            } else if ("2".equals(choice)) {
                Transaction transaction = interestService.applyInterest(account);
                Account updated = accountService.getAccountByNumber(account.getAccountNumber()).orElse(account);
                System.out.println("\n✓ Interest applied!");
                System.out.println("Interest Amount: $" + transaction.getAmount());
                System.out.println("New Balance: $" + updated.getBalance());
                System.out.println();
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    /**
     * List user's accounts
     */
    private void listMyAccounts() {
        List<Account> accounts = accountService.getAccountsByUser(currentUser);
        if (accounts.isEmpty()) {
            System.out.println("\nNo accounts found.\n");
        } else {
            System.out.println("\n--- Your Accounts ---");
            for (Account account : accounts) {
                System.out.println("Account: " + account.getAccountNumber() +
                        " | Type: " + account.getAccountType() +
                        " | Balance: $" + account.getBalance() +
                        " | Status: " + account.getStatus());
            }
            System.out.println();
        }
    }

    /**
     * Account management
     */
    private void accountManagement() {
        System.out.println("\n--- Account Management ---");
        System.out.println("1. Suspend Account");
        System.out.println("2. Activate Account");
        System.out.println("3. Close Account");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();

        try {
            Account account = selectAccount();
            if (account == null)
                return;

            switch (choice) {
                case "1":
                    accountService.updateAccountStatus(account.getAccountNumber(), Account.AccountStatus.SUSPENDED);
                    System.out.println("✓ Account suspended.\n");
                    break;
                case "2":
                    accountService.updateAccountStatus(account.getAccountNumber(), Account.AccountStatus.ACTIVE);
                    System.out.println("✓ Account activated.\n");
                    break;
                case "3":
                    accountService.updateAccountStatus(account.getAccountNumber(), Account.AccountStatus.CLOSED);
                    System.out.println("✓ Account closed.\n");
                    break;
                default:
                    System.out.println("Invalid choice.\n");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + "\n");
        }
    }

    // Helper methods

    private Account selectAccount() {
        List<Account> accounts = accountService.getAccountsByUser(currentUser);
        if (accounts.isEmpty()) {
            System.out.println("No accounts found. Please create an account first.\n");
            return null;
        }

        System.out.println("\nSelect Account:");
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            System.out.println((i + 1) + ". " + acc.getAccountNumber() +
                    " (" + acc.getAccountType() + ") - $" + acc.getBalance());
        }
        System.out.print("Enter number: ");

        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index >= 0 && index < accounts.size()) {
                return accounts.get(index);
            }
        } catch (NumberFormatException e) {
            // Fall through
        }

        System.out.println("Invalid selection.\n");
        return null;
    }

    private Beneficiary selectBeneficiary() {
        List<Beneficiary> beneficiaries = beneficiaryService.getBeneficiaries(currentUser);
        if (beneficiaries.isEmpty()) {
            System.out.println("No beneficiaries found. Please add a beneficiary first.\n");
            return null;
        }

        System.out.println("\nSelect Beneficiary:");
        for (int i = 0; i < beneficiaries.size(); i++) {
            Beneficiary b = beneficiaries.get(i);
            System.out.println((i + 1) + ". " + b.getNickname() +
                    " - " + b.getAccount().getAccountNumber());
        }
        System.out.print("Enter number: ");

        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index >= 0 && index < beneficiaries.size()) {
                return beneficiaries.get(index);
            }
        } catch (NumberFormatException e) {
            // Fall through
        }

        System.out.println("Invalid selection.\n");
        return null;
    }

    private BigDecimal parseAmount() {
        try {
            String amountStr = scanner.nextLine().trim();
            BigDecimal amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Error: Amount must be greater than zero.\n");
                return null;
            }
            return amount;
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid amount format.\n");
            return null;
        }
    }

    private LocalDate parseDate() {
        try {
            String dateStr = scanner.nextLine().trim();
            return LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
            System.out.println("Error: Invalid date format. Use YYYY-MM-DD.\n");
            return null;
        }
    }
}
