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
    
    /**
     * Constructs a new Menu with initialized components.
     */
    public Menu() {
        scanner = new Scanner(System.in);
        currentAccount = new BankAccount(1000.0); // Default account with $1000
        allAccounts = new AllUserAccount();
        adminAccount = new AdminAccount();
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
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Funds");
            System.out.println("3. Withdraw Funds");
            System.out.println("4. View Transaction History");
            System.out.println("5. Transaction Management");
            System.out.println("6. View Account Limits");
            System.out.println("7. Exit");
            System.out.print("Enter your choice (1-7): ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    checkBalance();
                    break;
                case 2:
                    deposit();
                    break;
                case 3:
                    withdraw();
                    break;
                case 4:
                    viewTransactionHistory(currentAccount);
                    break;
                case 5:
                    showTransactionMenu();
                    break;
                case 6:
                    viewLimits();
                    break;
                case 7:
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
     * Displays the current account balance.
     */
    private void checkBalance() {
        System.out.println("\n--- Account Balance ---");
        System.out.printf("Current Balance: $%.2f\n", currentAccount.getCurrentBalance());
    }
    
    /**
     * Handles the deposit process.
     */
    private void deposit() {
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
                    generateTransactionAnalysisReport();;
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
}
