package bankingapp;

import java.util.Scanner;

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


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AllUserAccount allUsers = new AllUserAccount();
        BankAccountLibrary library = new BankAccountLibrary();
        AdminAccount admin = new AdminAccount(); // Simple admin

        while (true) {
            System.out.println("\nWelcome to the Banking App");
            System.out.println("1. Admin Login");
            System.out.println("2. User Login");
            System.out.println("0. Exit");
            System.out.print("Select option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Admin ID: ");
                    String adminId = scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String password = scanner.nextLine();
                    if (admin.getAdminId().equals(adminId) && admin.getAdminId().equals(password)) {
                        new AdminWorkPage(admin, allUsers, library).launch();
                    } else {
                        System.out.println("Invalid admin credentials.");
                    }
                }
                case 2 -> {
                	System.out.print("Enter customer first name: ");
                    String fname = scanner.nextLine();
                    System.out.print("Enter customer last name: ");
                    String lname = scanner.nextLine();
                    System.out.print("Enter customer birthday (yyyyMMdd): ");
                    String birthday = scanner.nextLine();
                    System.out.print("Enter customer SSN: ");
                    int ssn = scanner.nextInt();
                    System.out.print("Enter customer Bank Code: ");
                    int BankCode = scanner.nextInt();
                    
                    AccountHolder customer = new AccountHolder();
                    customer.lastname = lname; customer.birthday = birthday; customer.ssn = ssn;
                    
                    AccountHolder user = allUsers.returnAccount(customer.hashCode());
                    if (user != null) {
                        new UserWorkPage(user, library).launch();
                    } else {
                        System.out.println("Invalid user credentials.");
                    }
                }
                case 0 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
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

