package bankingapp;

import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Represents a recurring payment schedule.
 */
public class RecurringPayment {
    private double amount;
    private String description;
    private Date startDate;
    private Date nextPaymentDate;
    private PaymentFrequency frequency;
    private String recipientAccountId;
    private boolean isActive;
    private BankAccount account;

    public enum PaymentFrequency {
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY
    }

    public RecurringPayment(double amount, String description, Date startDate, 
                           PaymentFrequency frequency, String recipientAccountId, BankAccount account) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        if (recipientAccountId == null || recipientAccountId.trim().isEmpty()) {
            throw new IllegalArgumentException("Recipient account ID cannot be empty");
        }
        
        this.amount = amount;
        this.description = description;
        this.frequency = frequency;
        this.recipientAccountId = recipientAccountId;
        this.account = account;
        this.isActive = true;
        
        // Set start and next payment dates to midnight
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        cal.setTime(startDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        this.startDate = cal.getTime();
        this.nextPaymentDate = cal.getTime();
    }

    private Calendar getCurrentCalendar() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        String testTime = System.getProperty("test.current.time");
        if (testTime != null) {
            try {
                cal.setTimeInMillis(Long.parseLong(testTime));
            } catch (NumberFormatException e) {
                // Ignore and use current time
            }
        }
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public boolean isPaymentDue() {
        if (!isActive) return false;
        
        // Get current date at midnight
        Calendar now = getCurrentCalendar();
        
        // Get next payment date at midnight
        Calendar next = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        next.setTime(nextPaymentDate);
        next.set(Calendar.HOUR_OF_DAY, 0);
        next.set(Calendar.MINUTE, 0);
        next.set(Calendar.SECOND, 0);
        next.set(Calendar.MILLISECOND, 0);
        
        // Compare dates at midnight - include the current day (>=)
        return now.getTimeInMillis() >= next.getTimeInMillis();
    }

    public void updateNextPaymentDate() {
        // Start with current next payment date
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        cal.setTime(nextPaymentDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        // Get current date for comparison
        Calendar now = getCurrentCalendar();
        
        // Keep adding the frequency until we find a date in the future
        do {
            // Add appropriate time interval
            switch (frequency) {
                case DAILY:
                    cal.add(Calendar.DATE, 1);
                    break;
                case WEEKLY:
                    cal.add(Calendar.WEEK_OF_YEAR, 1);
                    break;
                case MONTHLY:
                    cal.add(Calendar.MONTH, 1);
                    break;
                case YEARLY:
                    cal.add(Calendar.YEAR, 1);
                    break;
            }
        } while (cal.getTimeInMillis() <= now.getTimeInMillis());
        
        nextPaymentDate = cal.getTime();
    }

    // Removed redundant processPayment method - logic is in BankAccount.processRecurringPayments()
    /*
    public int processPayment() {
        if (!isPaymentDue()) {
            System.out.println("[DEBUG] Payment not due - next payment date: " + nextPaymentDate);
            return 0;
        }
        try {
            // No getAccountNumber() exists in BankAccount
            // System.out.println("[DEBUG] Attempting payment of " + amount + " from account " + account.getAccountNumber()); 
            System.out.println("[DEBUG] Attempting payment of " + amount);
            account.withdraw(amount); // Correct withdraw signature
            updateNextPaymentDate();
            return 1;
        } catch (InsufficientFundsException e) {
            System.out.println("[DEBUG] Insufficient funds for payment: " + e.getMessage());
            return 0;
        }
    }
    */

    // Getters
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public Date getStartDate() { return startDate; }
    public Date getNextPaymentDate() { return nextPaymentDate; }
    public PaymentFrequency getFrequency() { return frequency; }
    public String getRecipientAccountId() { return recipientAccountId; }
    public boolean isActive() { return isActive; }
    public BankAccount getAccount() { return account; }

    // Setters
    public void setAmount(double amount) { this.amount = amount; }
    public void setActive(boolean active) { this.isActive = active; }
    public void setNextPaymentDate(Date nextPaymentDate) { this.nextPaymentDate = nextPaymentDate; }
}
