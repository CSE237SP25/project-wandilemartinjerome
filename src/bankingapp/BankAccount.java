package bankingapp;

/**
 * Represents a bank account with basic operations.
 * 
 * @author Martin Rivera
 * @author Wandile Hannah
 * @author Jerome Hsing
 */
public class BankAccount {

    private double balance;
    
    // Default maximum values for transactions
    private static final double DEFAULT_MAX_WITHDRAWAL = 1000.0;
    private static final double DEFAULT_MAX_DEPOSIT = 10000.0;
    
    // Instance-specific maximum values
    private double maxWithdrawalLimit;
    private double maxDepositLimit;

    /**
     * Constructs a new bank account with an initial balance of 0.
     */
    public BankAccount() {
        this.balance = 0;
        this.maxWithdrawalLimit = DEFAULT_MAX_WITHDRAWAL;
        this.maxDepositLimit = DEFAULT_MAX_DEPOSIT;
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
    }

    /**
     * Withdraws the specified amount from the account if possible
     * 
     * @param amount The amount of money to withdraw
     * @return true if the withdrawal was successful, false otherwise
     * @throws IllegalArgumentException if the withdrawal amount is negative or exceeds the maximum withdrawal limit
     */
    public boolean withdraw(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Withdrawal amount cannot be negative");
        }
        
        if (amount > this.maxWithdrawalLimit) {
            throw new IllegalArgumentException("Withdrawal amount exceeds maximum limit of $" + this.maxWithdrawalLimit);
        }
        
        if (amount > this.balance) {
            return false; // Insufficient funds
        }
        
        this.balance -= amount;
        return true;
    }

    /**
     * Transfers funds from this account to another account
     * 
     * @param destinationAccount The account to transfer funds to
     * @param amount The amount to transfer
     * @return true if transfer was successful, false otherwise
     * @throws IllegalArgumentException if amount is negative or destination is null
     */
    public boolean transfer(BankAccount destinationAccount, double amount) {
        if (destinationAccount == null) {
            throw new IllegalArgumentException("Destination account cannot be null");
        }

        // Use withdraw method to check if we can withdraw the amount
        if (this.withdraw(amount)) {
            try {
                destinationAccount.deposit(amount);
                return true;
            } catch (IllegalArgumentException e) {
                // If deposit fails, revert the withdrawal
                this.balance += amount;
                throw e;
            }
        }
        return false;
    }
}
