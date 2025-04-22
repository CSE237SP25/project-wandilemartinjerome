package tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import bankingapp.BankAccount;
import bankingapp.RecurringPayment;
import bankingapp.Transaction;
import bankingapp.TransactionType;

/**
 * Test cases for recurring payment functionality.
 * 
 * @author Martin Rivera
 * @author Wandile Hannah
 * @author Jerome Hsing
 */
public class RecurringPaymentTest {
    private BankAccount account;
    private Date startDate;
    private Calendar calendar;

    @Before
    public void setUp() {
        account = new BankAccount(1000.0); // Start with $1000
        
        // Set up a fixed test date
        calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.APRIL, 22, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        startDate = calendar.getTime();
        
        // Set the same date for RecurringPayment's internal date checks
        System.setProperty("test.current.time", String.valueOf(startDate.getTime()));
    }

    @Test
    public void testScheduleRecurringPayment() {
        RecurringPayment payment = account.scheduleRecurringPayment(
            100.0,
            "Monthly Subscription",
            startDate,
            RecurringPayment.PaymentFrequency.MONTHLY,
            "MONTHLY001"
        );
        
        assertNotNull("Payment should be created", payment);
        assertEquals("Payment amount should match", 100.0, payment.getAmount(), 0.01);
        assertEquals("Payment description should match", "Monthly Subscription", payment.getDescription());
        assertEquals("Payment frequency should match", RecurringPayment.PaymentFrequency.MONTHLY, payment.getFrequency());
        assertEquals("Payment should have correct recipient", "MONTHLY001", payment.getRecipientAccountId());
        assertTrue("Payment should be active", payment.isActive());
        assertEquals("Initial next payment date should match start date", 
                    startDate, payment.getNextPaymentDate());
    }
    
    @Test
    public void testScheduleRecurringPaymentNegativeAmount() {
        try {
            account.scheduleRecurringPayment(
                -100.0, 
                "Invalid Payment", 
                startDate,
                RecurringPayment.PaymentFrequency.MONTHLY, 
                "INVALID001"
            );
            fail("Should throw exception for negative amount");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testScheduleRecurringPaymentExceedsLimit() {
        try {
            account.scheduleRecurringPayment(
                2000.0, // Exceeds default withdrawal limit of 1000
                "Excessive Payment", 
                startDate,
                RecurringPayment.PaymentFrequency.MONTHLY, 
                "EXCESSIVE001"
            );
            fail("Should throw exception for amount exceeding withdrawal limit");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testProcessRecurringPayments() {
        RecurringPayment payment = account.scheduleRecurringPayment(
            100.0,
            "Monthly Payment",
            startDate,
            RecurringPayment.PaymentFrequency.MONTHLY,
            "MONTHLY001"
        );
        
        // Set test time to the start date
        System.setProperty("test.current.time", String.valueOf(startDate.getTime()));

        // Process payments - should process one payment
        int processed = account.processRecurringPayments();
        assertEquals("Should process one payment", 1, processed);
        assertEquals("Balance should be reduced", 900.0, account.getCurrentBalance(), 0.01);

        // Verify transaction was recorded
        List<Transaction> history = account.getTransactionHistory();
        Transaction lastTransaction = history.get(history.size() - 1);
        assertEquals("Transaction type should be recurring payment", 
                    TransactionType.RECURRING_PAYMENT, 
                    lastTransaction.getType());
        assertEquals("Transaction amount should match", 100.0, lastTransaction.getAmount(), 0.01);
    }
    
    @Test
    public void testInsufficientFundsForRecurringPayment() {
        System.setProperty("test.current.time", "2020-01-01T10:00:00"); // Set current time for test
        BankAccount account = new BankAccount(100.0); // Start with 100
        Date startDate = getDate(2020, Calendar.JANUARY, 1);
        RecurringPayment payment = account.scheduleRecurringPayment(
                75.0, "Monthly Subscription", startDate, 
                RecurringPayment.PaymentFrequency.MONTHLY, "recipient123");

        // Process first payment - should succeed
        int processed1 = account.processRecurringPayments();
        assertEquals("First payment should process", 1, processed1);
        assertEquals("Balance after first payment", 25.0, account.getCurrentBalance(), 0.01);

        // Set time forward to make next payment due
        System.setProperty("test.current.time", "2020-02-01T10:00:00"); 
        
        // Process second payment - should fail (insufficient funds)
        int processed2 = account.processRecurringPayments();
        assertEquals("Second payment should not process due to insufficient funds", 0, processed2);
        assertEquals("Balance should remain unchanged after failed payment", 25.0, account.getCurrentBalance(), 0.01);

        System.clearProperty("test.current.time");
    }
    
    @Test
    public void testPaymentCancellation() {
        System.setProperty("test.current.time", "2020-01-01T10:00:00");
        BankAccount account = new BankAccount(200.0);
        Date startDate = getDate(2020, Calendar.JANUARY, 1);
        RecurringPayment payment = account.scheduleRecurringPayment(
                50.0, "Gym Membership", startDate, 
                RecurringPayment.PaymentFrequency.MONTHLY, "gym456");

        // Process first payment
        account.processRecurringPayments();
        assertEquals("Balance after first payment", 150.0, account.getCurrentBalance(), 0.01);

        // Cancel the payment
        account.cancelRecurringPayment(payment);

        // Set time forward to make next payment due
        System.setProperty("test.current.time", "2020-02-01T10:00:00"); 
        
        // Process payments again - the cancelled one should not run
        int processedCount = account.processRecurringPayments();
        assertEquals("No payment should process after cancellation", 0, processedCount);
        assertEquals("Balance should remain unchanged after cancellation", 150.0, account.getCurrentBalance(), 0.01);

        System.clearProperty("test.current.time");
    }

    @Test
    public void testNextPaymentDateCalculation() {
        System.setProperty("test.current.time", "2020-01-01T10:00:00");
        BankAccount account = new BankAccount(500.0);
        Date startDate = getDate(2020, Calendar.JANUARY, 1); // Start Jan 1st
        RecurringPayment payment = account.scheduleRecurringPayment(
                50.0, "Daily Test", startDate, 
                RecurringPayment.PaymentFrequency.DAILY, "daily1");

        // Process payment for Jan 1st
        int processed = account.processRecurringPayments();
        assertEquals("Payment should process", 1, processed);

        // Check next payment date
        Calendar actualNext = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        actualNext.setTime(payment.getNextPaymentDate());

        // Expected: Jan 2nd (Day 2 of the year)
        int EXPECTED_NEXT_MONTH = Calendar.JANUARY;
        int EXPECTED_NEXT_DAY_OF_MONTH = 2;
        int EXPECTED_NEXT_DAY_OF_YEAR = 2; 

        assertEquals("Next payment month should be January", EXPECTED_NEXT_MONTH, actualNext.get(Calendar.MONTH));
        assertEquals("Next payment day of month should be 2nd", EXPECTED_NEXT_DAY_OF_MONTH, actualNext.get(Calendar.DAY_OF_MONTH));
        assertEquals("Next payment day of year should be 2", EXPECTED_NEXT_DAY_OF_YEAR, actualNext.get(Calendar.DAY_OF_YEAR));

        System.clearProperty("test.current.time");
    }

    @Test
    public void testCancelRecurringPayment() {
        RecurringPayment payment = account.scheduleRecurringPayment(
            100.0,
            "Cancellable Payment",
            startDate,
            RecurringPayment.PaymentFrequency.MONTHLY,
            "CANCEL001"
        );

        // Cancel the payment
        account.cancelRecurringPayment(payment);
        assertFalse("Payment should be inactive", payment.isActive());

        // Try to process payments
        int processed = account.processRecurringPayments();
        assertEquals("No payments should be processed", 0, processed);
        assertEquals("Balance should remain unchanged", 1000.0, account.getCurrentBalance(), 0.01);
    }
    
    // Helper to create Date objects easily
    private Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
