package bankingapp;

import java.util.Scanner;

public class Menu {

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
}
