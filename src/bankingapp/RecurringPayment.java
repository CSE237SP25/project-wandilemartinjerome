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
    private BankAccount bankAccount;

    public enum PaymentFrequency {
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY
    }

    public RecurringPayment(double amount, String description, Date startDate, 
                           PaymentFrequency frequency, String recipientAccountId, BankAccount bankAccount) {
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
        this.bankAccount = bankAccount;
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

    // Checks if the payment is due based on the provided current time
    public boolean isPaymentDue(Calendar currentTime) { 
        if (!isActive) return false;
        Calendar next = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        next.setTime(nextPaymentDate);
        // Ensure 'next' is also set to midnight for comparison
        next.set(Calendar.HOUR_OF_DAY, 0);
        next.set(Calendar.MINUTE, 0);
        next.set(Calendar.SECOND, 0);
        next.set(Calendar.MILLISECOND, 0);

        // Compare provided currentTime (already at midnight) with next payment date (at midnight)
        return currentTime.getTimeInMillis() >= next.getTimeInMillis();
    }

    // Updates the next payment date based on the frequency, relative to the provided current time 'now'
    public void updateNextPaymentDate(Calendar now) { 
        // Start with current next payment date
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        cal.setTime(nextPaymentDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        // Keep adding the frequency until we find a date strictly *after* 'now'
        // Use <= to ensure if payment is due exactly today, we still advance it
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

    // Getters
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public Date getStartDate() { return startDate; }
    public Date getNextPaymentDate() { return nextPaymentDate; }
    public PaymentFrequency getFrequency() { return frequency; }
    public String getRecipientAccountId() { return recipientAccountId; }
    public boolean isActive() { return isActive; }
    public BankAccount getBankAccount() { return bankAccount; }

    // Setters
    public void setAmount(double amount) { this.amount = amount; }
    public void setActive(boolean active) { this.isActive = active; }
    public void setNextPaymentDate(Date nextPaymentDate) { this.nextPaymentDate = nextPaymentDate; }
}
