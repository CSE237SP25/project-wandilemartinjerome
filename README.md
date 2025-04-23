# cse237-project25

Team Members:

* Wandile Hannah
* Martin Rivera
* Jerome Hsing

# Banking Application Project Iteration Report

## Completed User Stories

### Expand Account Types
- Add support for Checking, Savings, and Business account types
- Implement interest accrual for savings accounts
- Enforce overdraft and fee policies for checking accounts

### Recurring Transactions
- Implement support for scheduling recurring payments and transfers
- Add menu options for managing and canceling recurring transactions
- Validate scheduled dates and ensured transactions execute at runtime triggers

## Planned User Stories for Next Iteration

N/A

## Implementation Challenges

1. **Menu Tests**: Making sure exact formatting in the Menu class is catched by the Menu tests class.
2. **Runtime Triggers**: Figuring out how to work with calendar features and threads.
   
## Compilation and Execution Instructions

## Step 1: Clone the Repository
```bash
git clone https://github.com/CSE237SP25/project-wandilemartinjerome.git
cd project-wandilemartinjerome
```

## Step 2: Running Application
### Unix/Linux/Mac
```bash
chmod +x compile_and_run.sh
./compile_and_run.sh
```

## Step 3: Using the Banking Application
After running the application, you'll see a menu with these options:
1. Create a new account
2. Select account
3. Check Balance
4. Deposit Funds
5. Withdraw Funds
6. View Transaction History
7. Transaction Management
8. View Account Limits
9. View Personal Information
10. Change Password
11. Manage Recurring Payments
12. Exit
  
Enter the number corresponding to your choice and follow the prompts.

## Step 4: Running the Tests
### Unix/Linux/Mac
```bash
chmod +x run_tests.sh
./run_tests.sh
```
