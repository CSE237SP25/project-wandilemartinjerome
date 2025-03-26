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

### Windows (compile_and_run.bat)
```batch
@echo off
echo Compiling Banking Application...

:: Create bin directory if it doesn't exist
if not exist bin mkdir bin

:: Compile all Java files
javac -d bin src/bankingapp/*.java

:: Run the application
echo Running Banking Application...
java -cp bin bankingapp.Menu

echo Done!
```

### Unix/Linux/Mac (compile_and_run.sh)
```bash
#!/bin/bash
echo "Compiling Banking Application..."

# Create bin directory if it doesn't exist
mkdir -p bin

# Compile all Java files
javac -d bin src/bankingapp/*.java

# Run the application
echo "Running Banking Application..."
java -cp bin bankingapp.Menu

echo "Done!"
```

### Running Tests

```bash
# Windows
javac -d bin src/bankingapp/*.java src/tests/*.java
java -cp bin;lib/junit-4.13.2.jar;lib/junit-jupiter-api-5.9.1.jar;lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore tests.BankAccountTests tests.MenuTests tests.AdminAccountTests

# Unix/Linux/Mac
javac -d bin src/bankingapp/*.java src/tests/*.java
java -cp bin:lib/junit-4.13.2.jar:lib/junit-jupiter-api-5.9.1.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore tests.BankAccountTests tests.MenuTests tests.AdminAccountTests
```
