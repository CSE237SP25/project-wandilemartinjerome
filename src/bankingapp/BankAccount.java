package bankingapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

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
    
    // Recurring payments
    private List<RecurringPayment> recurringPayments;

    /**
     * Constructs a new bank account with an initial balance of 0.
     */
    public BankAccount() {
        this.balance = 0;
        this.maxWithdrawalLimit = DEFAULT_MAX_WITHDRAWAL;
        this.maxDepositLimit = DEFAULT_MAX_DEPOSIT;
        this.transactionHistory = new ArrayList<>();
        this.recurringPayments = new ArrayList<>();
        this.accountType = AccountType.CHECKING; // Default to checking account
    }

    /**
     * Constructs a new bank account with the specified account type.
     * 
     * @param accountType The type of account (CHECKING, SAVINGS, etc.)
     */
    public BankAccount(AccountType accountType) {
        this.balance = 0;
        this.maxWithdrawalLimit = DEFAULT_MAX_WITHDRAWAL;
        this.maxDepositLimit = DEFAULT_MAX_DEPOSIT;
        this.transactionHistory = new ArrayList<>();
        this.recurringPayments = new ArrayList<>();
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
        this.recurringPayments = new ArrayList<>();
        // Record initial deposit if balance is positive
        if (initBalance > 0) {
            recordTransaction(TransactionType.DEPOSIT, initBalance, "Initial deposit");
        }
    }

    /**
     * Constructs a new bank account with the specified initial balance and account type.
     * 
     * @param initBalance The initial balance of the account.
     * @param accountType The type of account (CHECKING, SAVINGS, etc.)
     */
    public BankAccount(double initBalance, AccountType accountType) {
        this.balance = initBalance;
        this.maxWithdrawalLimit = DEFAULT_MAX_WITHDRAWAL;
        this.maxDepositLimit = DEFAULT_MAX_DEPOSIT;
        this.transactionHistory = new ArrayList<>();
        this.recurringPayments = new ArrayList<>();
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
        this.recurringPayments = new ArrayList<>();
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
     * @throws IllegalArgumentException if the amount is negative or exceeds withdrawal limits
     */
    public synchronized boolean withdraw(double amount) {
        return withdraw(amount, true); // Default to recording the transaction
    }

    /**
     * Withdraws a specified amount from the account, optionally recording the transaction.
     * 
     * @param amount The amount to withdraw.
     * @param recordTransaction If true, record a standard withdrawal transaction.
     * @return true if the withdrawal was successful
     * @throws IllegalArgumentException if the amount is negative or exceeds withdrawal limits.
     */
    public synchronized boolean withdraw(double amount, boolean recordTransaction) {
        if (amount < 0) {
            throw new IllegalArgumentException("Withdrawal amount cannot be negative");
        }
        
        if (amount > this.maxWithdrawalLimit) {
            throw new IllegalArgumentException("Withdrawal amount exceeds maximum limit of $" + this.maxWithdrawalLimit);
        }
        
        if (amount > this.balance) {
            if (recordTransaction) {
                recordTransaction(TransactionType.FAILED, amount, "Failed withdrawal - Insufficient funds");
            }
            return false;
        }
        
        this.balance -= amount;
        if (recordTransaction) {
            recordTransaction(TransactionType.WITHDRAWAL, amount, "Withdrawal");
        }
        return true;
    }

    /**
     * Transfers funds from this account to another account
     * 
     * @param destinationAccount The account to transfer funds to
     * @param amount The amount to transfer
     * @return true if transfer was successful
     * @throws IllegalArgumentException if amount is negative, destination is null, or exceeds withdrawal limit
     */
    public synchronized boolean transfer(BankAccount destinationAccount, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Transfer amount cannot be negative");
        }
        if (destinationAccount == null) {
            throw new IllegalArgumentException("Destination account cannot be null");
        }
        if (this == destinationAccount) {
            throw new IllegalArgumentException("Source and destination accounts cannot be the same");
        }

        // Check withdrawal limit before attempting the withdrawal part of the transfer
        if (amount > maxWithdrawalLimit) {
            recordTransaction(TransactionType.FAILED, amount, "Transfer failed: Exceeds withdrawal limit");
            throw new IllegalArgumentException("Transfer amount " + amount + " exceeds withdrawal limit of " + maxWithdrawalLimit);
        }

        // Withdraw from source (this account)
        if (!withdraw(amount, false)) {
            // If withdrawal fails due to insufficient funds
            recordTransaction(TransactionType.FAILED, amount, "Transfer failed: Insufficient funds");
            return false;
        }
        
        // Record the withdrawal part of the transfer
        recordTransaction(TransactionType.WITHDRAWAL, amount, "Withdrawal for transfer");
        
        // Deposit into destination
        try {
            destinationAccount.deposit(amount); // This will record its own deposit transaction
            // Record successful transfer transaction in this account's history
            recordTransaction(TransactionType.TRANSFER, amount, "Transfer to account " + destinationAccount.hashCode());
            return true;
        } catch (IllegalArgumentException depositError) {
            // If deposit fails (e.g., exceeds destination's deposit limit), we need to refund the source account
            System.err.println("Transfer failed during deposit phase: " + depositError.getMessage() + ". Refunding source account.");
            try {
                deposit(amount); // Refund the source account
                recordTransaction(TransactionType.FAILED, amount, "Transfer failed: Destination rejected deposit. Refunded.");
            } catch (IllegalArgumentException refundError) {
                // This should ideally not happen if the deposit limit wasn't violated by the refund
                System.err.println("CRITICAL ERROR: Failed to refund source account after failed transfer deposit. Amount: " + amount + ". Error: " + refundError.getMessage());
            }
            // Re-throw the original deposit error to indicate the transfer failure
            throw depositError; 
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
     * Adds a new recurring payment to this account.
     * 
     * @param amount The amount to be paid.
     * @param description Description of the payment.
     * @param startDate When the payments should start.
     * @param frequency How often the payment should occur.
     * @param recipientAccountId The account ID to send payment to.
     * @return The created RecurringPayment object.
     * @throws IllegalArgumentException if amount is invalid or exceeds limits.
     */
    public RecurringPayment scheduleRecurringPayment(double amount, String description, 
            Date startDate, RecurringPayment.PaymentFrequency frequency, String recipientAccountId) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
        if (amount > maxWithdrawalLimit) {
            throw new IllegalArgumentException("Payment amount exceeds withdrawal limit");
        }
        
        RecurringPayment payment = new RecurringPayment(amount, description, startDate, 
                                                       frequency, recipientAccountId, this);
        recurringPayments.add(payment);
        return payment;
    }

    // Utility method to get the current Calendar instance for tests or real time
    private Calendar getCurrentCalendar() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5")); // Use GMT-5
        String testTime = System.getProperty("test.current.time");
        if (testTime != null && !testTime.trim().isEmpty()) {
            try {
                cal.setTimeInMillis(Long.parseLong(testTime));
            } catch (NumberFormatException e) {
                System.err.println("[BankAccount] WARN: Could not parse test time property '" + testTime + "'. Using real time.");
                // Fall through to use real time
            }
        } // Otherwise, use real time by default

        // Set to midnight for consistent date comparisons
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public void cancelRecurringPayment(RecurringPayment payment) {
        if (recurringPayments.contains(payment)) {
            payment.setActive(false);
        } else {
            System.out.println("Payment not found in scheduled list.");
        }
    }

    public List<RecurringPayment> getRecurringPayments() {
        return new ArrayList<>(recurringPayments); // Return a copy
    }

    /**
     * Gets the account type.
     * 
     * @return The account type (CHECKING, SAVINGS, etc.)
     */
    public AccountType getAccountType() {
        return accountType;
    }
    
    /**
     * Sets the account type.
     * 
     * @param accountType The account type to set
     */
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    
    // Processes all active recurring payments that are due
    public int processRecurringPayments() {
        int paymentsProcessed = 0;
        // Get the current time (respecting test property) ONCE for this processing run
        Calendar now = getCurrentCalendar(); 
        System.out.println("[ProcessRecurringPayments] Processing with 'now' = " + now.getTime()); // Optional: Debugging

        // Iterate using an iterator to allow removal during iteration if needed (though not strictly necessary here as we only deactivate)
        Iterator<RecurringPayment> iterator = recurringPayments.iterator();
        while (iterator.hasNext()) {
            RecurringPayment payment = iterator.next();
            System.out.println("[ProcessRecurringPayments] Checking payment: " + payment.getDescription() + ", Active: " + payment.isActive()); // Optional: Debugging

            // Pass 'now' to isPaymentDue
            if (payment.isActive() && payment.isPaymentDue(now)) { 
                System.out.println("[ProcessRecurringPayments] Payment DUE: " + payment.getDescription() + ", Amount: " + payment.getAmount()); // Optional: Debugging
                try {
                    // Withdraw without recording a 'Withdrawal' transaction here
                    boolean withdrawalSuccess = withdraw(payment.getAmount(), false);
                    if (withdrawalSuccess) {
                        // Explicitly record as a RECURRING_PAYMENT
                        recordTransaction(TransactionType.RECURRING_PAYMENT, payment.getAmount(), "Recurring payment: " + payment.getDescription());
                        System.out.println("[ProcessRecurringPayments] Payment SUCCESS: " + payment.getDescription()); // Optional: Debugging
                        // Pass 'now' to updateNextPaymentDate
                        payment.updateNextPaymentDate(now); 
                        System.out.println("[ProcessRecurringPayments] Updated next payment date for " + payment.getDescription() + " to: " + payment.getNextPaymentDate()); // Optional: Debugging
                        paymentsProcessed++;
                    } else {
                        // Log the error but continue processing others
                        // Record failed payment transaction
                        recordTransaction(TransactionType.FAILED, payment.getAmount(), "Failed recurring payment '" + payment.getDescription() + "': Insufficient funds");
                        System.err.println("Insufficient funds for recurring payment '" + payment.getDescription() + "'");
                    }
                } catch (Exception e) { // Catch other potential exceptions during withdrawal
                    // Record failed payment transaction
                    recordTransaction(TransactionType.FAILED, payment.getAmount(), "Failed recurring payment '" + payment.getDescription() + "': " + e.getMessage());
                    System.err.println("Error processing recurring payment '" + payment.getDescription() + "': " + e.getMessage());
                }
            }
        }
        return paymentsProcessed;
    }
}
