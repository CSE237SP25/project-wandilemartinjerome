package bankingapp;

import java.util.Date;
import java.util.Calendar;

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

    public enum PaymentFrequency {
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY
    }

    public RecurringPayment(double amount, String description, Date startDate, 
                           PaymentFrequency frequency, String recipientAccountId) {
        this.amount = amount;
        this.description = description;
        this.startDate = startDate;
        this.frequency = frequency;
        this.recipientAccountId = recipientAccountId;
        this.nextPaymentDate = startDate;
        this.isActive = true;
    }

    public boolean isPaymentDue() {
        return isActive && new Date().after(nextPaymentDate);
    }

    public void updateNextPaymentDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(nextPaymentDate);
        
        switch (frequency) {
            case DAILY:
                cal.add(Calendar.DAY_OF_MONTH, 1);
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

    // Setters
    public void setAmount(double amount) { this.amount = amount; }
    public void setActive(boolean active) { this.isActive = active; }
}
