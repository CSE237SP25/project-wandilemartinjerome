package bankingapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a bank account with basic operations.
 * 
 * @author Martin Rivera
 * @author Wandile Hannah
 * @author Jerome Hsing
 */
public class BankAccount {

    private double balance;
    private AccountType accountType;
    
    // Default maximum values for transactions
    private static final double DEFAULT_MAX_WITHDRAWAL = 1000.0;
    private static final double DEFAULT_MAX_DEPOSIT = 10000.0;
    
    // Instance-specific maximum values
    private double maxWithdrawalLimit;
    private double maxDepositLimit;

    // Transaction history
    private List<Transaction> transactionHistory;

    /**
     * Constructs a new bank account with an initial balance of 0.
     */
    public BankAccount() {
        this.balance = 0;
        this.maxWithdrawalLimit = DEFAULT_MAX_WITHDRAWAL;
        this.maxDepositLimit = DEFAULT_MAX_DEPOSIT;
        this.transactionHistory = new ArrayList<>();
    }

    /**
     * Constructs a new bank account with an initial balance of 0 and a specified account type.
     * 
     * @param accountType The type of account (CHECKING or SAVINGS).
     */
    public BankAccount(AccountType accountType) {
        this.balance = 0;
        this.maxWithdrawalLimit = DEFAULT_MAX_WITHDRAWAL;
        this.maxDepositLimit = DEFAULT_MAX_DEPOSIT;
        this.transactionHistory = new ArrayList<>();
        this.accountType = accountType;
    }

    /**
     * Constructs a new bank account with the specified initial balance.
     * 
     * @param initBalance The initial balance of the account.
     */
    public BankAccount(double initBalance) {
        this.balance = initBalance;
        this.maxWithdrawalLimit = DEFAULT_MAX_WITHDRAWAL;
        this.maxDepositLimit = DEFAULT_MAX_DEPOSIT;
        this.transactionHistory = new ArrayList<>();
        // Record initial deposit if balance is positive
        if (initBalance > 0) {
            recordTransaction(TransactionType.DEPOSIT, initBalance, "Initial deposit");
        }
    }

    /**
     * Constructs a new bank account with the specified initial balance and account type.
     * 
     * @param initBalance The initial balance of the account.
     * @param accountType The type of account (CHECKING or SAVINGS).
     */
    public BankAccount(double initBalance, AccountType accountType) {
        this.balance = initBalance;
        this.maxWithdrawalLimit = DEFAULT_MAX_WITHDRAWAL;
        this.maxDepositLimit = DEFAULT_MAX_DEPOSIT;
        this.transactionHistory = new ArrayList<>();
        this.accountType = accountType;
        // Record initial deposit if balance is positive
        if (initBalance > 0) {
            recordTransaction(TransactionType.DEPOSIT, initBalance, "Initial deposit");
        }
    }
    
    /**
     * Constructs a new bank account with the specified initial balance and transaction limits.
     * 
     * @param initBalance The initial balance of the account.
     * @param maxWithdrawal The maximum withdrawal limit.
     * @param maxDeposit The maximum deposit limit.
     */
    public BankAccount(double initBalance, double maxWithdrawal, double maxDeposit) {
        this.balance = initBalance;
        this.maxWithdrawalLimit = maxWithdrawal;
        this.maxDepositLimit = maxDeposit;
        this.transactionHistory = new ArrayList<>();
        // Record initial deposit if balance is positive
        if (initBalance > 0) {
            recordTransaction(TransactionType.DEPOSIT, initBalance, "Initial deposit");
        }
    }

    /**
     * Constructs a new bank account with the specified initial balance, transaction limits, and account type.
     * 
     * @param initBalance The initial balance of the account.
     * @param maxWithdrawal The maximum withdrawal limit.
     * @param maxDeposit The maximum deposit limit.
     * @param accountType The type of account (CHECKING or SAVINGS).
     */
    public BankAccount(double initBalance, double maxWithdrawal, double maxDeposit, AccountType accountType) {
        this.balance = initBalance;
        this.maxWithdrawalLimit = maxWithdrawal;
        this.maxDepositLimit = maxDeposit;
        this.transactionHistory = new ArrayList<>();
        this.accountType = accountType;
        // Record initial deposit if balance is positive
        if (initBalance > 0) {
            recordTransaction(TransactionType.DEPOSIT, initBalance, "Initial deposit");
        }
    }

    /**
     * Returns the current balance of the account.
     * 
     * @return The current balance.
     */
    public double getCurrentBalance() {
        return this.balance;
    }
    
    /**
     * Returns the maximum withdrawal limit for this account.
     * 
     * @return The maximum withdrawal limit.
     */
    public double getMaxWithdrawalLimit() {
        return this.maxWithdrawalLimit;
    }
    
    /**
     * Sets the maximum withdrawal limit for this account.
     * 
     * @param maxLimit The new maximum withdrawal limit.
     * @throws IllegalArgumentException if the limit is negative.
     */
    public void setMaxWithdrawalLimit(double maxLimit) {
        if (maxLimit < 0) {
            throw new IllegalArgumentException("Maximum withdrawal limit cannot be negative");
        }
        this.maxWithdrawalLimit = maxLimit;
        recordTransaction(TransactionType.LIMIT_CHANGE, maxLimit, "Changed withdrawal limit");
    }
    
    /**
     * Returns the maximum deposit limit for this account.
     * 
     * @return The maximum deposit limit.
     */
    public double getMaxDepositLimit() {
        return this.maxDepositLimit;
    }
    
    /**
     * Sets the maximum deposit limit for this account.
     * 
     * @param maxLimit The new maximum deposit limit.
     * @throws IllegalArgumentException if the limit is negative.
     */
    public void setMaxDepositLimit(double maxLimit) {
        if (maxLimit < 0) {
            throw new IllegalArgumentException("Maximum deposit limit cannot be negative");
        }
        this.maxDepositLimit = maxLimit;
        recordTransaction(TransactionType.LIMIT_CHANGE, maxLimit, "Changed deposit limit");
    }

    /**
     * Deposits the specified amount into the account.
     * 
     * @param amount The amount to deposit.
     * @throws IllegalArgumentException if the deposit amount is negative or exceeds the maximum deposit limit.
     */
    public void deposit(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Deposit amount cannot be negative");
        }
        
        if (amount > this.maxDepositLimit) {
            throw new IllegalArgumentException("Deposit amount exceeds maximum limit of $" + this.maxDepositLimit);
        }
        
        this.balance += amount;
        recordTransaction(TransactionType.DEPOSIT, amount, "Deposit");
    }

    /**
     * Withdraws the specified amount from the account if possible
     * 
     * @param amount The amount of money to withdraw
     * @return true if the withdrawal was successful
     * @throws IllegalArgumentException if the withdrawal amount is negative, exceeds the maximum withdrawal limit, or if there are insufficient funds
     */
    public boolean withdraw(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Withdrawal amount cannot be negative");
        }
        
        if (amount > this.maxWithdrawalLimit) {
            throw new IllegalArgumentException("Withdrawal amount exceeds maximum limit of $" + this.maxWithdrawalLimit);
        }
        
        if (amount > this.balance) {
            recordTransaction(TransactionType.FAILED, amount, "Failed withdrawal - Insufficient funds");
            throw new IllegalArgumentException("Insufficient funds");
        }
        
        this.balance -= amount;
        recordTransaction(TransactionType.WITHDRAWAL, amount, "Withdrawal");
        return true;
    }

    /**
     * Transfers funds from this account to another account
     * 
     * @param destinationAccount The account to transfer funds to
     * @param amount The amount to transfer
     * @return true if transfer was successful
     * @throws IllegalArgumentException if amount is negative, destination is null, or if there are insufficient funds
     */
    public boolean transfer(BankAccount destinationAccount, double amount) {
        if (destinationAccount == null) {
            throw new IllegalArgumentException("Destination account cannot be null");
        }

        if (amount < 0) {
            throw new IllegalArgumentException("Transfer amount cannot be negative");
        }

        try {
            if (this.withdraw(amount)) {
                try {
                    destinationAccount.deposit(amount);
                    recordTransaction(TransactionType.TRANSFER, amount, "Transfer to account " + destinationAccount.hashCode());
                    return true;
                } catch (IllegalArgumentException e) {
                    // If deposit fails, revert the withdrawal
                    this.balance += amount;
                    recordTransaction(TransactionType.FAILED, amount, "Failed transfer - " + e.getMessage());
                    throw e;
                }
            }
            return false;
        } catch (IllegalArgumentException e) {
            recordTransaction(TransactionType.FAILED, amount, "Failed transfer - " + e.getMessage());
            throw e;
        }
    }

    /**
     * Gets the transaction history for this account.
     * 
     * @return A list of all transactions for this account.
     */
    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory); // Return a copy to prevent modification
    }

    /**
     * Gets the transaction history for this account filtered by type.
     * 
     * @param type The type of transactions to filter by.
     * @return A list of transactions of the specified type.
     */
    public List<Transaction> getTransactionHistoryByType(TransactionType type) {
        List<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction transaction : transactionHistory) {
            if (transaction.getType() == type) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }

    /**
     * Records a transaction in the transaction history.
     * 
     * @param type The type of transaction.
     * @param amount The amount involved in the transaction.
     * @param description A description of the transaction.
     */
    private void recordTransaction(TransactionType type, double amount, String description) {
        Transaction transaction = new Transaction(type, amount, description, new Date(), this.balance);
        transactionHistory.add(transaction);
    }

    /**
     * Clears the transaction history.
     */
    public void clearTransactionHistory() {
        transactionHistory.clear();
        recordTransaction(TransactionType.ADMIN, 0, "Transaction history cleared");
    }

    /**
     * Returns the type of account.
     * 
     * @return The account type (CHECKING or SAVINGS).
     */
    public AccountType getAccountType() {
        return this.accountType;
    }
}
