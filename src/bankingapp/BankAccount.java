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
                    withdraw(payment.getAmount()); // Use BankAccount's withdraw
                    System.out.println("[ProcessRecurringPayments] Payment SUCCESS: " + payment.getDescription()); // Optional: Debugging
                    // Pass 'now' to updateNextPaymentDate
                    payment.updateNextPaymentDate(now); 
                    System.out.println("[ProcessRecurringPayments] Updated next payment date for " + payment.getDescription() + " to: " + payment.getNextPaymentDate()); // Optional: Debugging
                    paymentsProcessed++;
                } catch (IllegalArgumentException e) {
                    // Log the error but continue processing others
                    System.err.println("Insufficient funds for recurring payment '" + payment.getDescription() + "': " + e.getMessage());
                    // Optionally, deactivate the payment after failed attempt?
                    // payment.setActive(false); 
                } catch (Exception e) { // Catch other potential exceptions during withdrawal
                    System.err.println("Error processing recurring payment '" + payment.getDescription() + "': " + e.getMessage());
                }
            }
        }
        return paymentsProcessed;
    }
}
