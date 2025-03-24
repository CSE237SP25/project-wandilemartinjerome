package bankingapp;

import java.util.Scanner;

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
            System.out.println("4. View Account Limits");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1-5): ");
            
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
                    viewLimits();
                    break;
                case 5:
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
}
