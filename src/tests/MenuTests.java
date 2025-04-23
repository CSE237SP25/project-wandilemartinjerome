package tests;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import bankingapp.*;
import java.util.Date;
import java.util.Calendar;

public class MenuTests {
    private BankAccount sourceAccount;
    private BankAccount destinationAccount;
    private static final double INITIAL_BALANCE = 1000.0;
    
    @Before
    public void setUp() {
        sourceAccount = new BankAccount(INITIAL_BALANCE);
        destinationAccount = new BankAccount(500.0);
    }
    
    @Test
    public void testScheduledTransfer() {
        // Schedule a transfer for tomorrow
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = cal.getTime();
        
        double transferAmount = 500.0;
        ScheduledTransfer transfer = sourceAccount.scheduleTransfer(
            destinationAccount, 
            transferAmount, 
            tomorrow,
            "Test scheduled transfer"
        );
        
        // Verify transfer is scheduled but not executed
        assertFalse(transfer.isExecuted());
        assertEquals(INITIAL_BALANCE, sourceAccount.getCurrentBalance(), 0.01);
        assertEquals(500.0, destinationAccount.getCurrentBalance(), 0.01);
        
        // Process transfers with current date (should not execute)
        int processed = sourceAccount.processScheduledTransfers(new Date());
        assertEquals(0, processed);
        assertEquals(INITIAL_BALANCE, sourceAccount.getCurrentBalance(), 0.01);
        
        // Process transfers with future date (should execute)
        cal.add(Calendar.DAY_OF_MONTH, 1); // Day after tomorrow
        processed = sourceAccount.processScheduledTransfers(cal.getTime());
        assertEquals(1, processed);
        assertEquals(INITIAL_BALANCE - transferAmount, sourceAccount.getCurrentBalance(), 0.01);
        assertEquals(1000.0, destinationAccount.getCurrentBalance(), 0.01);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testScheduleTransferPastDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1); // Yesterday
        sourceAccount.scheduleTransfer(destinationAccount, 100.0, cal.getTime(), "Past transfer");
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void testScheduleTransferNegativeAmount() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        sourceAccount.scheduleTransfer(destinationAccount, -100.0, cal.getTime(), "Negative amount");
    }
    
    @Test
    public void testMultipleScheduledTransfers() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        
        // Schedule 3 transfers
        sourceAccount.scheduleTransfer(destinationAccount, 100.0, cal.getTime(), "Transfer 1");
        sourceAccount.scheduleTransfer(destinationAccount, 200.0, cal.getTime(), "Transfer 2");
        sourceAccount.scheduleTransfer(destinationAccount, 300.0, cal.getTime(), "Transfer 3");
        
        assertEquals(3, sourceAccount.getScheduledTransfers().size());
        
        // Process all transfers
        cal.add(Calendar.DAY_OF_MONTH, 1);
        int processed = sourceAccount.processScheduledTransfers(cal.getTime());
        
        assertEquals(3, processed);
        assertEquals(400.0, sourceAccount.getCurrentBalance(), 0.01);
        assertEquals(1100.0, destinationAccount.getCurrentBalance(), 0.01);
    }
}
