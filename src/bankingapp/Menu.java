package bankingapp;

import java.util.Scanner;

public class Menu {
    private BankAccount currentAccount;
    private Scanner scanner;
    private BankAccountLibrary accountLibrary;
    
    public Menu() {
        scanner = new Scanner(System.in);
        currentAccount = null;
        accountLibrary = new BankAccountLibrary();
    }
    
    public void start() {
        boolean running = true;
        
        while (running) {
            displayMainMenu();
            int choice = getChoice();
            
            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    if (selectAccount()) {
                        deposit();
                    }
                    break;
                case 3:
                    if (selectAccount()) {
                        withdraw();
                    }
                    break;
                case 4:
                    if (selectAccount()) {
                        checkBalance();
                    }
                    break;
                case 5:
                    running = false;
                    System.out.println("Thank you for using our Banking Application!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        
        scanner.close();
    }
    
    private void displayMainMenu() {
        System.out.println("\n===== Banking Application Menu =====");
        System.out.println("1. Create a new account");
        System.out.println("2. Deposit money");
        System.out.println("3. Withdraw money");
        System.out.println("4. Check balance");
        System.out.println("5. Exit");
        System.out.print("Enter your choice (1-5): ");
    }
    
    private int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private void createAccount() {
        System.out.println("\n----- Create a New Account -----");
        System.out.print("Enter initial deposit amount (or 0 for empty account): ");
        
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
            
            accountLibrary.addAccount(currentAccount);
            int accountNumber = accountLibrary.BankAccountNumber(currentAccount);
            System.out.println("Account created successfully with account number: " + accountNumber);
            System.out.println("Current balance: $" + currentAccount.getCurrentBalance());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        }
    }
    
    private boolean selectAccount() {
        System.out.println("\n----- Select Account -----");
        System.out.print("Enter account number: ");
        
        try {
            int accountNumber = Integer.parseInt(scanner.nextLine().trim());
            if (accountLibrary.findAccount(accountNumber)) {
                // We need to modify BankAccountLibrary to have a getAccount method
                // For now, we're still using the same account which is incorrect
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
    
    private void deposit() {
        System.out.println("\n----- Deposit Money -----");
        System.out.print("Enter amount to deposit: ");
        
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            
            try {
                currentAccount.deposit(amount);
                System.out.println("Deposit successful. New balance: $" + currentAccount.getCurrentBalance());
            } catch (IllegalArgumentException e) {
                System.out.println("Deposit failed. Amount must be positive.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        }
    }
    
    private void withdraw() {
        System.out.println("\n----- Withdraw Money -----");
        System.out.print("Enter amount to withdraw: ");
        
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            
            try {
                currentAccount.withdraw(amount);
                System.out.println("Withdrawal successful. New balance: $" + currentAccount.getCurrentBalance());
            } catch (IllegalArgumentException e) {
                System.out.println("Withdrawal failed. Amount must be positive and less than your current balance.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
        }
    }
    
    private void checkBalance() {
        System.out.println("\n----- Account Balance -----");
        System.out.println("Current balance: $" + currentAccount.getCurrentBalance());
    }
    
    private boolean accountExists() {
        if (currentAccount == null) {
            System.out.println("No account exists. Please create an account first.");
            return false;
        }
        return true;
    }
    
    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.start();
    }
}
