package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

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
        account.scheduleRecurringPayment(
            100.0,
            "Monthly Subscription",
            startDate,
            RecurringPayment.PaymentFrequency.MONTHLY,
            "MONTHLY001"
        );
        
        assertNotNull("Payment should be created", account.getRecurringPayments().get(0));
        assertEquals("Payment amount should match", 100.0, account.getRecurringPayments().get(0).getAmount(), 0.01);
        assertEquals("Payment description should match", "Monthly Subscription", account.getRecurringPayments().get(0).getDescription());
        assertEquals("Payment frequency should match", RecurringPayment.PaymentFrequency.MONTHLY, account.getRecurringPayments().get(0).getFrequency());
        assertEquals("Payment should have correct recipient", "MONTHLY001", account.getRecurringPayments().get(0).getRecipientAccountId());
        assertTrue("Payment should be active", account.getRecurringPayments().get(0).isActive());
        assertEquals("Initial next payment date should match start date", 
                    startDate, account.getRecurringPayments().get(0).getNextPaymentDate());
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
        account.scheduleRecurringPayment(
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
        account.scheduleRecurringPayment(
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
        account.scheduleRecurringPayment(
                50.0, "Gym Membership", startDate, 
                RecurringPayment.PaymentFrequency.MONTHLY, "gym456");

        // Process first payment
        account.processRecurringPayments();
        assertEquals("Balance after first payment", 150.0, account.getCurrentBalance(), 0.01);

        // Cancel the payment
        account.cancelRecurringPayment(account.getRecurringPayments().get(0));

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
        account.scheduleRecurringPayment(
                50.0, "Daily Test", startDate, 
                RecurringPayment.PaymentFrequency.DAILY, "daily1");

        // Process payment for Jan 1st
        int processed = account.processRecurringPayments();
        assertEquals("Payment should process", 1, processed);

        // Check next payment date
        Calendar actualCal = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        actualCal.setTime(account.getRecurringPayments().get(0).getNextPaymentDate());

        assertEquals("Next payment year should be 2020", 2020, actualCal.get(Calendar.YEAR));
        assertEquals("Next payment month should be February", Calendar.FEBRUARY, actualCal.get(Calendar.MONTH)); 
        assertEquals("Next payment day should be 1st", 1, actualCal.get(Calendar.DAY_OF_MONTH));

        System.clearProperty("test.current.time");
    }

    @Test
    public void testIsPaymentDue() {
        // Set current time for test to Jan 15th, 2020
        long jan15_2020_millis = getDate(2020, Calendar.JANUARY, 15).getTime();
        System.setProperty("test.current.time", String.valueOf(jan15_2020_millis));
        Calendar testNow = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        testNow.setTimeInMillis(jan15_2020_millis);
        testNow.set(Calendar.HOUR_OF_DAY, 0);
        testNow.set(Calendar.MINUTE, 0);
        testNow.set(Calendar.SECOND, 0);
        testNow.set(Calendar.MILLISECOND, 0);

        // Scenario 1: Payment due today
        Date startDateDue = getDate(2020, Calendar.JANUARY, 15); // Start date is today
        account.scheduleRecurringPayment(50.0, "Due Today", startDateDue, RecurringPayment.PaymentFrequency.MONTHLY, "recipientDue");
        RecurringPayment duePayment = account.getRecurringPayments().get(0); // Assuming it's the first/only one
        assertTrue("Payment starting today should be due", duePayment.isPaymentDue(testNow));

        // Scenario 2: Payment due in the past
        Date startDatePast = getDate(2020, Calendar.JANUARY, 1); // Start date was Jan 1st
        account.scheduleRecurringPayment(50.0, "Past Due", startDatePast, RecurringPayment.PaymentFrequency.MONTHLY, "recipientPast");
        RecurringPayment pastDuePayment = account.getRecurringPayments().get(1);
        assertTrue("Payment starting in the past should be due", pastDuePayment.isPaymentDue(testNow));

        // Scenario 3: Payment due in the future
        Date startDateFuture = getDate(2020, Calendar.FEBRUARY, 1); // Start date Feb 1st
        account.scheduleRecurringPayment(50.0, "Future Payment", startDateFuture, RecurringPayment.PaymentFrequency.MONTHLY, "recipientFuture");
        RecurringPayment futurePayment = account.getRecurringPayments().get(2);
        assertFalse("Payment starting in the future should not be due", futurePayment.isPaymentDue(testNow));

        // Scenario 4: Inactive payment (due date doesn't matter)
        Date startDateInactive = getDate(2020, Calendar.JANUARY, 1);
        account.scheduleRecurringPayment(50.0, "Inactive", startDateInactive, RecurringPayment.PaymentFrequency.MONTHLY, "recipientInactive");
        RecurringPayment inactivePayment = account.getRecurringPayments().get(3);
        inactivePayment.setActive(false);
        assertFalse("Inactive payment should not be due", inactivePayment.isPaymentDue(testNow));

        // Clean up system property
        System.clearProperty("test.current.time");
    }

    @Test
    public void testCancelRecurringPayment() {
        Date startDate = getDate(2020, Calendar.JANUARY, 1);
        account.scheduleRecurringPayment(
            100.0,
            "Cancellable Payment",
            startDate,
            RecurringPayment.PaymentFrequency.MONTHLY,
            "recipientCancel"
        );
        RecurringPayment paymentToCancel = account.getRecurringPayments().get(0);

        // Cancel the payment
        account.cancelRecurringPayment(paymentToCancel);
        assertFalse("Payment should be inactive", paymentToCancel.isActive());

        // Try to process payments
        // Set time forward to when payment *would* have been due
        long feb1_2020_millis = getDate(2020, Calendar.FEBRUARY, 1).getTime();
        System.setProperty("test.current.time", String.valueOf(feb1_2020_millis)); 
        Calendar testNow = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        testNow.setTimeInMillis(feb1_2020_millis);
        testNow.set(Calendar.HOUR_OF_DAY, 0);
        testNow.set(Calendar.MINUTE, 0);
        testNow.set(Calendar.SECOND, 0);
        testNow.set(Calendar.MILLISECOND, 0);
        int processed = account.processRecurringPayments();
        assertEquals("No payments should be processed for cancelled payment", 0, processed);
        assertEquals("Balance should not change", 1000.0, account.getCurrentBalance(), 0.01); 

        // Clean up system property
        System.clearProperty("test.current.time");
    }
    
    @Test
    public void testPaymentCancellationWithTime() {
        // Set initial time
        long jan1_2020_millis = getDate(2020, Calendar.JANUARY, 1).getTime(); 
        System.setProperty("test.current.time", String.valueOf(jan1_2020_millis));
        
        BankAccount account = new BankAccount(200.0);
        Date startDate = getDate(2020, Calendar.JANUARY, 1);
        account.scheduleRecurringPayment(
                50.0, "Gym Membership", startDate, 
                RecurringPayment.PaymentFrequency.MONTHLY, "gym456");

        // Process first payment
        account.processRecurringPayments();
        assertEquals("Balance after first payment", 150.0, account.getCurrentBalance(), 0.01);

        // Cancel the payment
        account.cancelRecurringPayment(account.getRecurringPayments().get(0));

        // Set time forward to make next payment due
        long feb1_2020_millis = getDate(2020, Calendar.FEBRUARY, 1).getTime();
        System.setProperty("test.current.time", String.valueOf(feb1_2020_millis)); 
        Calendar testNow = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        testNow.setTimeInMillis(feb1_2020_millis);
        testNow.set(Calendar.HOUR_OF_DAY, 0);
        testNow.set(Calendar.MINUTE, 0);
        testNow.set(Calendar.SECOND, 0);
        testNow.set(Calendar.MILLISECOND, 0);
        
        // Process payments again - the cancelled one should not run
        int processedCount = account.processRecurringPayments();
        assertEquals("No payment should process after cancellation", 0, processedCount);
        assertEquals("Balance should remain unchanged after cancellation", 150.0, account.getCurrentBalance(), 0.01);

        System.clearProperty("test.current.time");
    }

    // Helper to create Date objects easily
    private Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
