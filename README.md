# cse237-project25

Team Members:

* Wandile Hannah
* Martin Rivera
* Jerome Hsing

# Banking Application Project Iteration Report

## Completed User Stories

### Transaction History and Reporting
- Implemented transaction history tracking for each account
- Added reporting features for transaction analysis
- Created functionality to export account statements

### Security Enhancements
- Implemented robust authentication mechanisms
- Added password protection for administrative functions
- Implemented encryption for sensitive account data

## Planned User Stories for Next Iteration

### Expand Account Types
- Add support for Checking, Savings, and Business account types
- Implement interest accrual for savings accounts
- Enforce overdraft and fee policies for checking accounts

### Recurring Transactions
- Implement support for scheduling recurring payments and transfers
- Add menu options for managing and canceling recurring transactions
- Validate scheduled dates and ensured transactions execute at runtime triggers

## Implementation Challenges

1. **Account Limits**: Override account limits method in AdminAccount needs to be implemented in the BankAccount class in order to be effective. 
   
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
* Create a new account
* Select account
* Check Balance
* Deposit Funds
* Withdraw Funds
* View Transaction History
* Transaction Management
* View Account Limits
* View Personal Information
* Change Password
* Exit
Enter the number corresponding to your choice and follow the prompts.

## Step 4: Running the Tests
### Unix/Linux/Mac
```bash
chmod +x run_tests.sh
./run_tests.sh
```
