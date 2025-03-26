# cse237-project25

Team Members:

* Wandile Hannah
* Martin Rivera
* Jerome Hsing

# Banking Application Project Iteration Report

## Completed User Stories

### Basic Account Management
- Implemented `BankAccount` class with core banking functionality:
  - Deposit and withdrawal operations with validation
  - Transfer functionality between accounts
  - Maximum withdrawal and deposit limits
  - Balance tracking and account operations

### Administrative Capabilities
- Created [AdminAccount]
  - Audit logging for administrative actions
  - Security clearance levels for administrators
  - Reporting capabilities for account status
  - Override capabilities for account limits

### User Account Management
- Implemented [AllUserAccount]
  - Add, find, and delete account functionality
  - Account status tracking (active/frozen)
  - Statistics functions for account management
  - Account holder information management

### Package Consolidation
- Successfully consolidated all code into the `bankingapp` package
- Removed the redundant `bankapp` package across all branches
- Ensured consistent package structure throughout the project

## Planned User Stories for Next Iteration

### User Interface Enhancements
- Expand the `Menu` class to provide a comprehensive user interface
- Implement a command-line interface for interacting with the banking system
- Add user-friendly prompts and error messages

### Transaction History and Reporting
- Implement transaction history tracking for each account
- Add reporting features for transaction analysis
- Create functionality to export account statements

### Security Enhancements
- Implement robust authentication mechanisms
- Add password protection for administrative functions
- Implement encryption for sensitive account data

### Integration and Testing
- Create comprehensive integration tests for all components
- Implement end-to-end testing scenarios
- Add automated test suites for continuous integration

## Implementation Challenges

1. **Branch Integration**: While individual branches (`accountDeposit`, `withdraw`, `Accounts`) contain working functionality, they haven't been fully integrated into a cohesive application.

2. **Menu Implementation**: The `Menu` class exists but doesn't yet provide a complete user interface for interacting with the banking system.

## Compilation and Execution Instructions

## Step 1: Clone the Repository
```bash
git clone https://github.com/CSE237SP25/project-wandilemartinjerome.git
cd project-wandilemartinjerome
```

## Step 2: Running Application
### Windows
```bash
.\compile_and_run.bat
```
### Unix/Linux/Mac
```bash
chmod +x compile_and_run.sh
./compile_and_run.sh
```

## Step 3: Using the Banking Application
After running the application, you'll see a menu with these options:
* Check Balance
* Deposit Funds
* Withdraw Funds
* View Account Limits
* Exit
Enter the number corresponding to your choice and follow the prompts.

## Step 4: Running the Tests
### Windows
```bash
.\run_tests.bat
```
### Unix/Linux/Mac
```bash
chmod +x run_tests.sh
./run_tests.sh
```
