package bankingapp;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;

/**
 * Main menu interface for the Banking Application.
 * 
 * @author Martin Rivera
 * @author Wandile Hannah
 * @author Jerome Hsing
 */
public class Menu {
    private Scanner scanner;
    private BankAccount currentAccount;
    private BankAccountDatabase accountDatabase;
    private AccountHolder currentAccountHolder;
    
    // Inner class to store transaction statistics
    private static class TransactionStatistics {
        double totalDeposits = 0;
        double totalWithdrawals = 0;
        double totalTransfers = 0;
        int depositCount = 0;
        int withdrawalCount = 0;
        int transferCount = 0;
        int failedCount = 0;
        Transaction largestDeposit = null;
        Transaction largestWithdrawal = null;
    }
    
    /**
     * Constructs a new Menu with initialized components.
     */
    public Menu() {
        scanner = new Scanner(System.in);
        currentAccount = new BankAccount(500.0); // Default account with $500
        accountDatabase = new BankAccountDatabase();
        currentAccountHolder = new AccountHolder();
        
        // Add default account to database
        accountDatabase.addBankAccount(currentAccount);
    }
    
    /**
     * Main entry point for the Banking Application.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("Welcome to the Banking Application!");
        System.out.println("----------------------------------");
        
        Menu menu = new Menu();
        menu.showMainMenu();
    }
    
    /**
     * Displays the main menu and handles user input.
     */
    public void showMainMenu() {
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Create a new account");
            System.out.println("2. Select account");
            System.out.println("3. Check Balance");
            System.out.println("4. Deposit Funds");
            System.out.println("5. Withdraw Funds");
            System.out.println("6. View Transaction History");
            System.out.println("7. Transaction Management");
            System.out.println("8. View Account Limits");
            System.out.println("9. View Personal Information");
            System.out.println("10. Change Password");
            System.out.println("11. Manage Recurring Payments");
            System.out.println("12. Exit");
            System.out.print("Enter your choice (1-12): ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    selectAccount();
                    break;
                case 3:
                    checkBalance();
                    break;
                case 4:
                    deposit();
                    break;
                case 5:
                    withdraw();
                    break;
                case 6:
                    if (accountExists()) {
                        viewTransactionHistory(currentAccount);
                    }
                    break;
                case 7:
                    if (accountExists()) {
                        showTransactionMenu();
                    }
                    break;
                case 8:
                    if (accountExists()) {
                        viewLimits();
                    }
                    break;
                case 9:
                    viewPersonalInfo();
                    break;
                case 10:
                    changePassword();
                    break;
                case 11:
                    manageRecurringPayments();
                    break;
                case 12:
                    exit = true;
                    System.out.println("Thank you for using the Banking Application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        
        scanner.close();
    }
    
    /**
     * Checks if an account exists and is selected.
     * 
     * @return true if an account exists, false otherwise
     */
    private boolean accountExists() {
        if (currentAccount == null) {
            System.out.println("No account exists. Please create an account first.");
            return false;
        }
        return true;
    }
    
    /**
     * Creates a new bank account.
     */
    private void createAccount() {
        System.out.println("\n----- Create a New Account -----");

        // Get account holder information
        System.out.println("\nPlease enter your personal information:");
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Birthday (MM/DD/YYYY): ");
        String birthday = scanner.nextLine();

        System.out.print("Social Security Number (SSN): ");
        int ssn = getIntInput();

        System.out.print("Bank Code: ");
        int bankCode = getIntInput();

        // Create new account holder with personal information
        currentAccountHolder = new AccountHolder();
        currentAccountHolder.setPersonalInfo(lastName, birthday, ssn, bankCode);
        currentAccountHolder.setPassword("default123"); // Set a default password
        currentAccountHolder.hidePersonalInfo(); // Hide personal info by default

        // Add account holder to the system
        accountDatabase.addAccountHolder(currentAccountHolder);
        
        // Choose account category
        System.out.println("\nSelect Account Category:");
        System.out.println("1. Personal Account");
        System.out.println("2. Business Account");
        System.out.print("Select account category (1-2): ");
        int accountCategoryChoice = getIntInput();
        boolean isBusinessAccount = (accountCategoryChoice == 2);
        
        // Choose account type
        System.out.println("\nSelect Account Type:");
        System.out.println("1. Checking");
        System.out.println("2. Savings");
        System.out.print("Enter your choice (1-2): ");
        int accountTypeChoice = getIntInput();
        AccountType accountType;
        while (true) {
            if (accountTypeChoice == 1) {
                accountType = AccountType.CHECKING;
                break;
            } else if (accountTypeChoice == 2) {
                accountType = AccountType.SAVINGS;
                break;
            } else {
                System.out.println("Invalid choice. Please enter 1 for Checking or 2 for Savings.");
                System.out.print("Enter your choice (1-2): ");
                accountTypeChoice = getIntInput();
            }
        }
        System.out.print("\nEnter initial deposit amount (or 0 for empty account): ");

        try {
            double initialAmount = Double.parseDouble(scanner.nextLine());

            if (initialAmount < 0) {
                System.out.println("Initial amount cannot be negative.");
                return;
            }
            // Create appropriate account type based on user choice
            if (isBusinessAccount) {
                currentAccount = initialAmount == 0 ? 
                    new BusinessAccount(accountType) : new BusinessAccount(initialAmount, accountType);
                System.out.println("\nBusiness Account created successfully!");
            } else {
                currentAccount = initialAmount == 0 ? 
                    new BankAccount(accountType) : new BankAccount(initialAmount, accountType);
                System.out.println("\nPersonal Account created successfully!");
            }

            accountDatabase.addBankAccount(currentAccount);
            System.out.println("Account number: " + currentAccount.hashCode());
            System.out.println(accountType + " account created.");
            System.out.printf("Current balance: $%.2f%n", currentAccount.getCurrentBalance());
            
            // Display account limits
            System.out.printf("Maximum withdrawal limit: $%.2f%n", currentAccount.getMaxWithdrawalLimit());
            System.out.printf("Maximum deposit limit: $%.2f%n", currentAccount.getMaxDepositLimit());
            
            currentAccountHolder.addBankAccount(currentAccountHolder, currentAccount.hashCode());

            System.out.println("\nYour personal information has been saved.");
            System.out.println("Default password is: default123");
            System.out.println("Please change your password for security.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        }
    }
    
    /**
     * Selects an existing account.
     * 
     * @return true if account selection was successful, false otherwise
     */
    private boolean selectAccount() {
        System.out.println("\n----- Select Account -----");
        System.out.print("Enter account number: ");
        
        try {
            int accountNumber = Integer.parseInt(scanner.nextLine().trim());
            if (accountDatabase.hasBankAccount(accountNumber)) {
                currentAccount = accountDatabase.getBankAccount(accountNumber);
                // Find the account holder associated with this account
                currentAccountHolder = findAccountHolder(accountNumber);
                if (currentAccountHolder == null) {
                    System.out.println("Warning: No account holder information found for this account.");
                }
                System.out.println("Account selected successfully!");
                return true;
            } else {
                System.out.println("Account not found. Please check the account number.");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid account number format.");
            return false;
        }
    }
    
    /**
     * Finds the account holder associated with a bank account
     * 
     * @param accountNumber The bank account number to find the holder for
     * @return The AccountHolder object if found, null otherwise
     */
    private AccountHolder findAccountHolder(int accountNumber) {
        ArrayList<AccountHolder> accountHolders = new ArrayList<>(accountDatabase.getAccountHolders());
        for (AccountHolder holder : accountHolders) {
            if (holder.findBankAccount(holder, accountNumber)) {
                return holder;
            }
        }
        return null;
    }
    
    /**
     * Displays the current account balance.
     */
    private void checkBalance() {
        if (accountExists()) {
            System.out.printf("Current Balance: $%.2f%n", currentAccount.getCurrentBalance());
        }
    }
    
    /**
     * Handles the deposit process.
     */
    private void deposit() {
        if (!accountExists()) return;
        
        System.out.print("Enter amount to deposit: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount <= 0) {
                System.out.println("Deposit amount must be positive.");
                return;
            }
            
            try {
                currentAccount.deposit(amount);
                System.out.printf("Deposit successful. Your new balance is $%.2f%n", 
                    currentAccount.getCurrentBalance());
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        }
    }
    
    /**
     * Handles the withdrawal process.
     */
    private void withdraw() {
        if (!accountExists()) return;
        
        System.out.print("Enter amount to withdraw: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount <= 0) {
                System.out.println("Withdrawal amount must be positive.");
                return;
            }
            
            try {
                if (currentAccount.withdraw(amount)) {
                    System.out.printf("Withdrawal successful. Your new balance is $%.2f%n", 
                        currentAccount.getCurrentBalance());
                } else {
                    System.out.println("Insufficient funds in account.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        }
    }
    
    /**
     * Displays the transaction history for the given account.
     */
    private void viewTransactionHistory(BankAccount account) {
        List<Transaction> history = account.getTransactionHistory();
        if (history.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        
        System.out.println("\nTransaction History:");
        System.out.println("-------------------");
        for (Transaction transaction : history) {
            System.out.printf("%s - %s: $%.2f (Balance: $%.2f)%n",
                transaction.getDate(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getFinalBalance());
        }
    }
    
    /**
     * Displays the transaction management submenu.
     */
    private void showTransactionMenu() {
        System.out.println("\nTransaction Management Menu:");
        System.out.println("1. View All Transactions");
        System.out.println("2. View Transactions by Type");
        System.out.println("3. Generate Transaction Analysis Report");
        System.out.println("4. Return to Main Menu");
        System.out.print("Enter your choice (1-4): ");
        
        int choice = getIntInput();
        switch (choice) {
            case 1:
                viewTransactionHistory(currentAccount);
                break;
            case 2:
                viewTransactionsByType();
                break;
            case 3:
                generateTransactionAnalysisReport();
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    /**
     * Displays the current account limits.
     */
    private void viewLimits() {
        if (accountExists()) {
            System.out.printf("Maximum withdrawal limit: $%.2f%n", currentAccount.getMaxWithdrawalLimit());
            System.out.printf("Maximum deposit limit: $%.2f%n", currentAccount.getMaxDepositLimit());
        }
    }
    
    /**
     * Gets integer input from the user with error handling.
     * 
     * @return The integer entered by the user
     */
    private int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number: ");
            }
        }
    }
    
    /**
     * Gets double input from the user with error handling.
     * 
     * @return The double entered by the user
     */
    private double getDoubleInput() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number: ");
            }
        }
    }
    
    /**
     * Allows the user to view transaction filtered by type.
     */
    private void viewTransactionsByType() {
        System.out.println("\nSelect Transaction Type:");
        System.out.println("1. Deposits");
        System.out.println("2. Withdrawals");
        System.out.println("3. Transfers");
        System.out.println("4. Failed Transactions");
        System.out.print("Enter your choice (1-4): ");
        
        int choice = getIntInput();
        TransactionType type = null;
        
        switch (choice) {
            case 1:
                type = TransactionType.DEPOSIT;
                break;
            case 2:
                type = TransactionType.WITHDRAWAL;
                break;
            case 3:
                type = TransactionType.TRANSFER;
                break;
            case 4:
                type = TransactionType.FAILED;
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        List<Transaction> filteredTransactions = currentAccount.getTransactionHistoryByType(type);
        if (filteredTransactions.isEmpty()) {
            System.out.println("No transactions found of type: " + type);
            return;
        }
        
        System.out.println("\n" + type + " Transactions:");
        System.out.println("----------------------");
        for (Transaction transaction : filteredTransactions) {
            System.out.printf("%s: $%.2f (Balance: $%.2f) - %s%n",
                transaction.getDate(),
                transaction.getAmount(),
                transaction.getFinalBalance(),
                transaction.getDescription());
        }
    }
    
    /**
     * Generates and displays a comprehensive transaction analysis report.
     */
    private void generateTransactionAnalysisReport() {
        List<Transaction> history = currentAccount.getTransactionHistory();
        if (history.isEmpty()) {
            System.out.println("No transactions available for analysis.");
            return;
        }
        
        TransactionStatistics stats = calculateTransactionStatistics(history);
        int totalTransactions = history.size();
        
        System.out.println("\nTransaction Analysis Report");
        System.out.println("==========================");
        
        displayTransactionSummary(stats, totalTransactions);
        System.out.println();
        displayFinancialSummary(stats);
        System.out.println();
        displayTransactionAverages(stats);
        System.out.println();
        displayLargestTransactions(stats);
        System.out.println();
        displayRecommendations(stats);
    }
    
    /**
     * Calculates statistics from transaction history.
     * 
     * @param history List of transactions to analyze
     * @return TransactionStatistics object containing the analysis results
     */
    private TransactionStatistics calculateTransactionStatistics(List<Transaction> history) {
        TransactionStatistics stats = new TransactionStatistics();
        
        for (Transaction transaction : history) {
            switch (transaction.getType()) {
                case DEPOSIT:
                    stats.totalDeposits += transaction.getAmount();
                    stats.depositCount++;
                    if (stats.largestDeposit == null || transaction.getAmount() > stats.largestDeposit.getAmount()) {
                        stats.largestDeposit = transaction;
                    }
                    break;
                    
                case WITHDRAWAL:
                    stats.totalWithdrawals += transaction.getAmount();
                    stats.withdrawalCount++;
                    if (stats.largestWithdrawal == null || transaction.getAmount() > stats.largestWithdrawal.getAmount()) {
                        stats.largestWithdrawal = transaction;
                    }
                    break;
                    
                case TRANSFER:
                    stats.totalTransfers += transaction.getAmount();
                    stats.transferCount++;
                    break;
                    
                case FAILED:
                    stats.failedCount++;
                    break;
                    
                case SCHEDULED:
                case ADMIN:
                case LIMIT_CHANGE:
                case RECURRING_PAYMENT:
                    // These transaction types are not included in statistics
                    break;
            }
        }
        
        return stats;
    }
    
    /**
     * Displays the transaction count summary section of the report.
     * 
     * @param stats The transaction statistics
     * @param totalTransactions Total number of transactions
     */
    private void displayTransactionSummary(TransactionStatistics stats, int totalTransactions) {
        System.out.println("Transaction Count Summary:");
        System.out.println("-------------------------");
        System.out.printf("Total Transactions: %d%n", totalTransactions);
        System.out.printf("Deposits: %d%n", stats.depositCount);
        System.out.printf("Withdrawals: %d%n", stats.withdrawalCount);
        System.out.printf("Transfers: %d%n", stats.transferCount);
        System.out.printf("Failed Transactions: %d%n", stats.failedCount);
    }
    
    /**
     * Displays the financial summary section of the report.
     * 
     * @param stats The transaction statistics
     */
    private void displayFinancialSummary(TransactionStatistics stats) {
        System.out.println("Financial Summary:");
        System.out.println("-----------------");
        System.out.printf("Total Deposits: $%.2f%n", stats.totalDeposits);
        System.out.printf("Total Withdrawals: $%.2f%n", stats.totalWithdrawals);
        System.out.printf("Total Transfers: $%.2f%n", stats.totalTransfers);
        System.out.printf("Net Flow: $%.2f%n", stats.totalDeposits - stats.totalWithdrawals - stats.totalTransfers);
    }
    
    /**
     * Displays the transaction averages section of the report.
     * 
     * @param stats The transaction statistics
     */
    private void displayTransactionAverages(TransactionStatistics stats) {
        System.out.println("Transaction Averages:");
        System.out.println("--------------------");
        if (stats.depositCount > 0) {
            System.out.printf("Average Deposit: $%.2f%n", stats.totalDeposits / stats.depositCount);
        }
        if (stats.withdrawalCount > 0) {
            System.out.printf("Average Withdrawal: $%.2f%n", stats.totalWithdrawals / stats.withdrawalCount);
        }
        if (stats.transferCount > 0) {
            System.out.printf("Average Transfer: $%.2f%n", stats.totalTransfers / stats.transferCount);
        }
    }
    
    /**
     * Displays the largest transactions section of the report.
     * 
     * @param stats The transaction statistics
     */
    private void displayLargestTransactions(TransactionStatistics stats) {
        System.out.println("Largest Transactions:");
        System.out.println("--------------------");
        if (stats.largestDeposit != null) {
            System.out.printf("Largest Deposit: $%.2f on %s%n",
                stats.largestDeposit.getAmount(),
                stats.largestDeposit.getDate());
        }
        if (stats.largestWithdrawal != null) {
            System.out.printf("Largest Withdrawal: $%.2f on %s%n",
                stats.largestWithdrawal.getAmount(),
                stats.largestWithdrawal.getDate());
        }
    }
    
    /**
     * Displays personalized recommendations based on transaction patterns.
     * 
     * @param stats The transaction statistics
     */
    private void displayRecommendations(TransactionStatistics stats) {
        System.out.println("Personalized Recommendations:");
        System.out.println("--------------------------");
        
        // Check for high failed transaction rate
        double failedRate = stats.failedCount / (double) (stats.depositCount + stats.withdrawalCount + stats.transferCount + stats.failedCount);
        if (failedRate > 0.1) { // More than 10% failed transactions
            System.out.println("- Consider maintaining a higher balance to avoid failed transactions");
        }
        
        // Check for frequent small transactions
        double avgWithdrawal = stats.withdrawalCount > 0 ? stats.totalWithdrawals / stats.withdrawalCount : 0;
        if (stats.withdrawalCount > 10 && avgWithdrawal < 50) { // Many small withdrawals
            System.out.println("- Consider consolidating smaller withdrawals to reduce transaction frequency");
        }
        
        // Check for balance management
        if (stats.totalWithdrawals > stats.totalDeposits * 0.9) { // Spending close to income
            System.out.println("- Consider setting up a savings plan to maintain a higher balance");
        }
        
        // Suggest account upgrade if high transaction volume
        int totalTransactions = stats.depositCount + stats.withdrawalCount + stats.transferCount;
        if (totalTransactions > 50) {
            System.out.println("- You may benefit from upgrading to a premium account with higher transaction limits");
        }
    }
    
    /**
     * Displays the account holder's personal information.
     */
    private void viewPersonalInfo() {
        if (currentAccountHolder != null) {
            System.out.println("\nPersonal Information:");
            System.out.println(currentAccountHolder.getPersonalInfo("default123")); // Use default password
        } else {
            System.out.println("No account holder information available.");
        }
    }
    
    /**
     * Allows the user to change their password.
     */
    private void changePassword() {
        if (currentAccountHolder == null) {
            System.out.println("No account holder selected.");
            return;
        }
        
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();
        
        if (!currentAccountHolder.showPersonalInfo(currentPassword)) {
            System.out.println("Incorrect password.");
            return;
        }
        
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        
        currentAccountHolder.setPassword(newPassword);
        System.out.println("Password changed successfully.");
    }
    
    private void manageRecurringPayments() {
        if (!accountExists()) return;
        
        System.out.println("\nRecurring Payments Menu:");
        System.out.println("1. View Recurring Payments");
        System.out.println("2. Schedule New Payment");
        System.out.println("3. Cancel Payment");
        System.out.println("4. Process Due Payments");
        System.out.println("5. Return to Main Menu");
        System.out.print("Enter your choice (1-5): ");
        
        int choice = getIntInput();
        switch (choice) {
            case 1:
                viewRecurringPayments();
                break;
            case 2:
                scheduleNewRecurringPayment();
                break;
            case 3:
                cancelRecurringPayment();
                break;
            case 4:
                processDuePayments();
                break;
            case 5:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    private void viewRecurringPayments() {
        List<RecurringPayment> payments = currentAccount.getRecurringPayments();
        if (payments.isEmpty()) {
            System.out.println("No recurring payments scheduled.");
            return;
        }
        
        System.out.println("\nRecurring Payments:");
        System.out.println("------------------");
        for (RecurringPayment payment : payments) {
            System.out.printf("%s - $%.2f (%s)%n",
                payment.getDescription(),
                payment.getAmount(),
                payment.isActive() ? "Active" : "Inactive");
            System.out.printf("Next payment date: %s%n", payment.getNextPaymentDate());
            System.out.println();
        }
    }
    
    private void scheduleNewRecurringPayment() {
        System.out.print("Enter payment amount: ");
        double amount = getDoubleInput();
        
        System.out.print("Enter payment description: ");
        String description = scanner.nextLine();
        
        System.out.println("\nSelect payment frequency:");
        System.out.println("1. Weekly");
        System.out.println("2. Monthly");
        System.out.println("3. Yearly");
        System.out.print("Enter your choice (1-3): ");
        
        int frequencyChoice = getIntInput();
        RecurringPayment.PaymentFrequency frequency;
        
        switch (frequencyChoice) {
            case 1:
                frequency = RecurringPayment.PaymentFrequency.WEEKLY;
                break;
            case 2:
                frequency = RecurringPayment.PaymentFrequency.MONTHLY;
                break;
            case 3:
                frequency = RecurringPayment.PaymentFrequency.YEARLY;
                break;
            default:
                System.out.println("Invalid frequency choice.");
                return;
        }
        
        System.out.print("Enter recipient account ID: ");
        String recipientId = scanner.nextLine();
        
        try {
            Date startDate = new Date(); // Start from today
            RecurringPayment payment = currentAccount.scheduleRecurringPayment(
                amount, description, startDate, frequency, recipientId);
            
            System.out.println("Recurring payment scheduled successfully!");
            System.out.printf("First payment of $%.2f will be on: %s%n",
                payment.getAmount(), payment.getNextPaymentDate());
        } catch (IllegalArgumentException e) {
            System.out.println("Error scheduling payment: " + e.getMessage());
        }
    }
    
    private void cancelRecurringPayment() {
        List<RecurringPayment> payments = currentAccount.getRecurringPayments();
        if (payments.isEmpty()) {
            System.out.println("No recurring payments to cancel.");
            return;
        }
        
        System.out.println("\nSelect payment to cancel:");
        for (int i = 0; i < payments.size(); i++) {
            RecurringPayment payment = payments.get(i);
            System.out.printf("%d. %s - $%.2f%n",
                i + 1,
                payment.getDescription(),
                payment.getAmount());
        }
        
        System.out.print("Enter payment number to cancel (1-" + payments.size() + "): ");
        int choice = getIntInput();
        
        if (choice < 1 || choice > payments.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        
        RecurringPayment selectedPayment = payments.get(choice - 1);
        currentAccount.cancelRecurringPayment(selectedPayment);
        System.out.println("Payment cancelled successfully.");
    }
    
    private void processDuePayments() {
        int processed = currentAccount.processRecurringPayments();
        System.out.printf("%d payment(s) processed.%n", processed);
    }
}