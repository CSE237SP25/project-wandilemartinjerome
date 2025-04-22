package tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
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
        calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.APRIL, 22); // Set to current test date
        startDate = calendar.getTime();
    }

    @Test
    public void testScheduleRecurringPayment() {
        RecurringPayment payment = account.scheduleRecurringPayment(
            100.0,
            "Monthly Rent",
            startDate,
            RecurringPayment.PaymentFrequency.MONTHLY,
            "RENT001"
        );

        assertNotNull("Payment should be created", payment);
        assertEquals("Amount should match", 100.0, payment.getAmount(), 0.01);
        assertEquals("Description should match", "Monthly Rent", payment.getDescription());
        assertEquals("Frequency should match", RecurringPayment.PaymentFrequency.MONTHLY, payment.getFrequency());
        assertEquals("Recipient should match", "RENT001", payment.getRecipientAccountId());
        assertTrue("Payment should be active", payment.isActive());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testScheduleRecurringPaymentNegativeAmount() {
        account.scheduleRecurringPayment(
            -100.0,
            "Invalid Payment",
            startDate,
            RecurringPayment.PaymentFrequency.MONTHLY,
            "TEST001"
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testScheduleRecurringPaymentExceedsLimit() {
        account.scheduleRecurringPayment(
            2000.0, // Exceeds default withdrawal limit of 1000.0
            "Exceeds Limit",
            startDate,
            RecurringPayment.PaymentFrequency.MONTHLY,
            "TEST001"
        );
    }

    @Test
    public void testProcessRecurringPayments() {
        // Schedule a daily payment
        account.scheduleRecurringPayment(
            100.0,
            "Daily Payment",
            startDate,
            RecurringPayment.PaymentFrequency.DAILY,
            "DAILY001"
        );

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
        // Schedule a payment larger than account balance
        account.scheduleRecurringPayment(
            900.0,
            "Large Payment",
            startDate,
            RecurringPayment.PaymentFrequency.DAILY,
            "LARGE001"
        );

        // Process first payment - should succeed
        int processed1 = account.processRecurringPayments();
        assertEquals("First payment should process", 1, processed1);
        assertEquals("Balance after first payment", 100.0, account.getCurrentBalance(), 0.01);

        // Process second payment - should fail due to insufficient funds
        int processed2 = account.processRecurringPayments();
        assertEquals("Second payment should not process", 0, processed2);
        assertEquals("Balance should remain unchanged", 100.0, account.getCurrentBalance(), 0.01);

        // Verify failed transaction was recorded
        List<Transaction> history = account.getTransactionHistory();
        Transaction lastTransaction = history.get(history.size() - 1);
        assertEquals("Transaction type should be failed", 
                    TransactionType.FAILED, 
                    lastTransaction.getType());
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

    @Test
    public void testNextPaymentDateCalculation() {
        // Test daily payment
        RecurringPayment dailyPayment = account.scheduleRecurringPayment(
            10.0, "Daily", startDate, RecurringPayment.PaymentFrequency.DAILY, "DAILY001"
        );
        account.processRecurringPayments();
        Calendar expected = Calendar.getInstance();
        expected.setTime(startDate);
        expected.add(Calendar.DAY_OF_MONTH, 1);
        assertEquals("Daily payment next date", 
                    expected.getTime().getTime() / 86400000, 
                    dailyPayment.getNextPaymentDate().getTime() / 86400000);

        // Test monthly payment
        RecurringPayment monthlyPayment = account.scheduleRecurringPayment(
            10.0, "Monthly", startDate, RecurringPayment.PaymentFrequency.MONTHLY, "MONTHLY001"
        );
        account.processRecurringPayments();
        expected.setTime(startDate);
        expected.add(Calendar.MONTH, 1);
        assertEquals("Monthly payment next date", 
                    expected.getTime().getTime() / 86400000, 
                    monthlyPayment.getNextPaymentDate().getTime() / 86400000);
    }
}
