package bankingapp;

import java.util.Date;

public class ScheduledTransfer {
    private final BankAccount sourceAccount;
    private final BankAccount destinationAccount;
    private final double amount;
    private final Date scheduledDate;
    private boolean executed;
    private String description;

    public ScheduledTransfer(BankAccount source, BankAccount destination, double amount, Date scheduledDate, String description) {
        this.sourceAccount = source;
        this.destinationAccount = destination;
        this.amount = amount;
        this.scheduledDate = scheduledDate;
        this.description = description;
        this.executed = false;
    }

    public boolean isReadyToExecute(Date currentDate) {
        return !executed && !currentDate.before(scheduledDate);
    }

    public boolean execute() {
        if (!executed) {
            boolean success = sourceAccount.transfer(destinationAccount, amount);
            if (success) {
                executed = true;
            }
            return success;
        }
        return false;
    }

    public Date getScheduledDate() {
        return scheduledDate;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public boolean isExecuted() {
        return executed;
    }
}
