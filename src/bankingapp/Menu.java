package bankingapp;

import java.util.List;
import java.util.Scanner;
import java.text.SimpleDateFormat;

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
    private AllUserAccount allAccounts;
    private AdminAccount adminAccount;
    private AccountHolder currentAccountHolder;
    
    /**
     * Constructs a new Menu with initialized components.
     */
    public Menu() {
        scanner = new Scanner(System.in);
        currentAccount = new BankAccount(1000.0); // Default account with $1000
        allAccounts = new AllUserAccount();
        adminAccount = new AdminAccount();
        currentAccountHolder = new AccountHolder();
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
            System.out.println("11. Exit");
            System.out.print("Enter your choice (1-11): ");
            
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
        allAccounts.AddAcount(currentAccountHolder);
        
        System.out.print("\nEnter initial deposit amount (or 0 for empty account): ");
        
        try {
            double initialAmount = Double.parseDouble(scanner.nextLine());
            
            if (initialAmount < 0) {
                System.out.println("Initial amount cannot be negative.");
                return;
            }
            
            if (initialAmount == 0) {
                currentAccount = new BankAccount();
            } else {
                currentAccount = new BankAccount(initialAmount);
            }
            
            allAccounts.addAccount(currentAccount);
            System.out.println("\nAccount created successfully!");
            System.out.println("Account number: " + currentAccount.hashCode());
            System.out.println("Current balance: $" + currentAccount.getCurrentBalance());
            
            // Link the bank account to the account holder
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
            if (allAccounts.findAccount(accountNumber)) {
                currentAccount = allAccounts.getAccount(accountNumber);
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
            System.out.println("Invalid account number. Please enter a valid number.");
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
        // This is a simplified implementation - in a real system, you'd want a more robust
        // way to associate accounts with account holders
        for (AccountHolder holder : allAccounts.getAccountHolders()) {
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
        if (!accountExists()) return;
        
        System.out.println("\n--- Account Balance ---");
        System.out.printf("Current Balance: $%.2f\n", currentAccount.getCurrentBalance());
    }
    
    /**
     * Handles the deposit process.
     */
    private void deposit() {
        if (!accountExists()) return;
        
        System.out.println("\n--- Deposit Funds ---");
        System.out.print("Enter amount to deposit: $");
        double amount = getDoubleInput();
        
        try {
            currentAccount.deposit(amount);
            System.out.printf("Successfully deposited $%.2f\n", amount);
            System.out.printf("New Balance: $%.2f\n", currentAccount.getCurrentBalance());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * Handles the withdrawal process.
     */
    private void withdraw() {
        if (!accountExists()) return;
        
        System.out.println("\n--- Withdraw Funds ---");
        System.out.print("Enter amount to withdraw: $");
        double amount = getDoubleInput();
        
        try {
            boolean success = currentAccount.withdraw(amount);
            if (success) {
                System.out.printf("Successfully withdrew $%.2f\n", amount);
                System.out.printf("New Balance: $%.2f\n", currentAccount.getCurrentBalance());
            } else {
                System.out.println("Withdrawal failed. Insufficient funds.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Displays the transaction history for the given account.
     */
    private static void viewTransactionHistory(BankAccount account) {
        List<Transaction> history = account.getTransactionHistory();
        if (history.isEmpty()) {
            System.out.println("No transaction history available.");
        }
        else {
            System.out.println("\n===== Transaction History =====");
            for (Transaction transaction : history) {
                System.out.println(transaction);
            }
            System.out.println("==============================\n");
        }
    }

    /**
     * Displays the transaction management submenu.
     */
    private void showTransactionMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\nTransaction Management:");
            System.out.println("1. View All Transactions");
            System.out.println("2. View Transactions by Type");
            System.out.println("3. Transaction Analysis Report");
            System.out.println("4. Back to Main Menu");
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
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    /**
     * Displays the current account limits.
     */
    private void viewLimits() {
        System.out.println("\n--- Account Limits ---");
        System.out.printf("Maximum Withdrawal Limit: $%.2f\n", currentAccount.getMaxWithdrawalLimit());
        System.out.printf("Maximum Deposit Limit: $%.2f\n", currentAccount.getMaxDepositLimit());
    }
    
    /**
     * Gets integer input from the user with error handling.
     * 
     * @return The integer entered by the user
     */
    private int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
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
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    /**
     * Allows the user to view transaction filtered by type.
     */
    private void viewTransactionsByType() {
        System.out.println("\nSelect transaction type to view:");
        System.out.println("1. Deposits");
        System.out.println("2. Withdrawals");
        System.out.println("3. Transfers");
        System.out.println("4. Failed Transactions");
        System.out.println("5. All Types");
        System.out.print("Enter your choice (1-5): ");
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
            case 5:
                viewTransactionHistory(currentAccount);
                return;
            default:
                System.out.println("Invalid choice. Showing all transactions");
                viewTransactionHistory(currentAccount);
                return;
        }

        List<Transaction> filteredTransactions = currentAccount.getTransactionHistoryByType(type);

        if (filteredTransactions.isEmpty()) {
            System.out.println("No transactions of type " + type + " found.");
        }
        else {
            System.out.println("\n===== " + type + " Transactions =====");
            for (Transaction transaction : filteredTransactions) {
                System.out.println(transaction);
            }
            System.out.println("==============================\n");
        }
    }

    /**
     * Generates and displays a comprehensive transaction analysis report.
     */
    private void generateTransactionAnalysisReport() {
        List<Transaction> history = currentAccount.getTransactionHistory();
        if (history.isEmpty()) {
            System.out.println("No transaction history available for analysis.");
            return;
        }
        
        // Calculate transaction statistics
        TransactionStatistics stats = calculateTransactionStatistics(history);
        
        // Display the report sections
        displayTransactionSummary(stats, history.size());
        displayFinancialSummary(stats);
        displayTransactionAverages(stats);
        displayLargestTransactions(stats);
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
            TransactionType type = transaction.getType();
            double amount = transaction.getAmount();
            
            switch (type) {
                case DEPOSIT:
                    stats.totalDeposits += amount;
                    stats.depositCount++;
                    
                    // Update largest deposit
                    if (stats.largestDeposit == null || amount > stats.largestDeposit.getAmount()) {
                        stats.largestDeposit = transaction;
                    }
                    break;
                    
                case WITHDRAWAL:
                    stats.totalWithdrawals += amount;
                    stats.withdrawalCount++;
                    
                    // Update largest withdrawal
                    if (stats.largestWithdrawal == null || amount > stats.largestWithdrawal.getAmount()) {
                        stats.largestWithdrawal = transaction;
                    }
                    break;
                    
                case TRANSFER:
                    stats.totalTransfers += amount;
                    stats.transferCount++;
                    break;
                    
                case FAILED:
                    stats.failedCount++;
                    break;
                    
                default:
                    // Ignore other transaction types for analysis
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
        System.out.println("\n========== TRANSACTION ANALYSIS REPORT ==========");
        System.out.println("\nTRANSACTION SUMMARY:");
        System.out.printf("Total Transactions: %d\n", totalTransactions);
        
        if (totalTransactions > 0) {
            System.out.printf("Deposits: %d (%.2f%%)\n", stats.depositCount, 
                    (double)stats.depositCount / totalTransactions * 100);
            System.out.printf("Withdrawals: %d (%.2f%%)\n", stats.withdrawalCount, 
                    (double)stats.withdrawalCount / totalTransactions * 100);
            System.out.printf("Transfers: %d (%.2f%%)\n", stats.transferCount, 
                    (double)stats.transferCount / totalTransactions * 100);
            System.out.printf("Failed Transactions: %d (%.2f%%)\n", stats.failedCount, 
                    (double)stats.failedCount / totalTransactions * 100);
        }
    }
    
    /**
     * Displays the financial summary section of the report.
     * 
     * @param stats The transaction statistics
     */
    private void displayFinancialSummary(TransactionStatistics stats) {
        System.out.println("\nFINANCIAL SUMMARY:");
        System.out.printf("Total Deposits: $%.2f\n", stats.totalDeposits);
        System.out.printf("Total Withdrawals: $%.2f\n", stats.totalWithdrawals);
        System.out.printf("Total Transfers: $%.2f\n", stats.totalTransfers);
        System.out.printf("Net Cash Flow: $%.2f\n", stats.totalDeposits - stats.totalWithdrawals);
    }
    
    /**
     * Displays the transaction averages section of the report.
     * 
     * @param stats The transaction statistics
     */
    private void displayTransactionAverages(TransactionStatistics stats) {
        double avgDeposit = stats.depositCount > 0 ? stats.totalDeposits / stats.depositCount : 0;
        double avgWithdrawal = stats.withdrawalCount > 0 ? stats.totalWithdrawals / stats.withdrawalCount : 0;
        
        System.out.println("\nAVERAGES:");
        System.out.printf("Average Deposit: $%.2f\n", avgDeposit);
        System.out.printf("Average Withdrawal: $%.2f\n", avgWithdrawal);
    }
    
    /**
     * Displays the largest transactions section of the report.
     * 
     * @param stats The transaction statistics
     */
    private void displayLargestTransactions(TransactionStatistics stats) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        if (stats.largestDeposit != null || stats.largestWithdrawal != null) {
            System.out.println("\nLARGEST TRANSACTIONS:");
            
            if (stats.largestDeposit != null) {
                System.out.printf("Largest Deposit: $%.2f on %s\n", 
                        stats.largestDeposit.getAmount(), 
                        dateFormat.format(stats.largestDeposit.getDate()));
            }
            
            if (stats.largestWithdrawal != null) {
                System.out.printf("Largest Withdrawal: $%.2f on %s\n", 
                        stats.largestWithdrawal.getAmount(), 
                        dateFormat.format(stats.largestWithdrawal.getDate()));
            }
        }
    }
    
    /**
     * Displays personalized recommendations based on transaction patterns.
     * 
     * @param stats The transaction statistics
     */
    private void displayRecommendations(TransactionStatistics stats) {
        System.out.println("\nRECOMMENDATIONS:");
        
        // Provide some basic financial advice based on the analysis
        if (stats.totalWithdrawals > stats.totalDeposits) {
            System.out.println("- Your withdrawals exceed your deposits. Consider reducing expenses or increasing income.");
        } else if (stats.totalDeposits > stats.totalWithdrawals * 2) {
            System.out.println("- You're saving well! Consider investing some of your savings for better returns.");
        }
        
        if (stats.failedCount > 0) {
            System.out.println("- You have " + stats.failedCount + " failed transactions. Review your spending habits to avoid insufficient funds.");
        }
        
        System.out.println("\n=================================================\n");
    }
    
    /**
     * Helper class to store transaction statistics during analysis.
     */
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
     * Displays the account holder's personal information.
     */
    private void viewPersonalInfo() {
        if (currentAccountHolder == null) {
            System.out.println("No account holder information available.");
            return;
        }

        System.out.println("\n--- Personal Information ---");
        
        // Always require password for personal info
        System.out.print("Enter password to view personal information: ");
        String password = scanner.nextLine();
        
        String info = currentAccountHolder.getPersonalInfo(password);
        if (info == null) {
            System.out.println("Incorrect password. Access denied.");
            return;
        }
        
        System.out.println("\nPersonal Information:");
        System.out.println(info);
        
        // Offer to change password
        System.out.print("\nWould you like to change your password? (y/n): ");
        String changePassword = scanner.nextLine().toLowerCase();
        if (changePassword.equals("y")) {
            changePassword();
        }
    }
    
    /**
     * Allows the user to change their password.
     */
    private void changePassword() {
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();
        
        // Verify current password
        if (!currentAccountHolder.showPersonalInfo(currentPassword)) {
            System.out.println("Incorrect current password. Password change failed.");
            return;
        }
        
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        
        // Set new password and hide personal info again
        currentAccountHolder.setPassword(newPassword);
        currentAccountHolder.hidePersonalInfo();
        
        System.out.println("Password changed successfully!");
    }
}