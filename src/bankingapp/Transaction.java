package bankingapp;

import java.util.Date;

/**
 * Represents a transaction in the transaction history.
 * 
 * @author Martin Rivera
 * @author Wandile Hannah
 * @author Jerome Hsing
 */
public class Transaction {
    private TransactionType type;
    private double amount;
    private String description;
    private Date date;
    private double balanceAfterTransaction;

    /**
     * Creates a new transaction.
     * 
     * @param type The type of transaction.
     * @param amount The amount involved in the transaction.
     * @param description A description of the transaction.
     * @param date The date and time of the transaction.
     * @param balanceAfterTransaction The account balance after the transaction.
     */
    public Transaction(TransactionType type, double amount, String description, Date date, double balanceAfterTransaction) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.balanceAfterTransaction = balanceAfterTransaction;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public double getBalanceAfterTransaction() {
        return balanceAfterTransaction;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: $%.2f - %s (Balance $%.2f)", date, type, amount, description, balanceAfterTransaction);
    }
}
