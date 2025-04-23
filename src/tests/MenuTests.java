package tests;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Test;
import org.junit.Before;

import bankingapp.*;
import java.util.Date;
import java.util.Calendar;

import bankingapp.Menu;

/**
 * Tests for the Menu class functionality.
 */
public class MenuTests {
    private BankAccount sourceAccount;
    private BankAccount destinationAccount;
    private static final double INITIAL_BALANCE = 1000.0;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;
    
    @Before
    public void setUp() {
        sourceAccount = new BankAccount(INITIAL_BALANCE);
        destinationAccount = new BankAccount(500.0);
        setUpStreams();
    }
    
    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outputStream));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
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

    @Test
    public void testCheckBalance() {
        String input = "3\n12\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Menu menu = new Menu();
        menu.showMainMenu();
        
        String output = outputStream.toString();
        assertTrue("Should show current balance", output.contains("Current Balance:"));
    }

    @Test
    public void testDepositFunds() {
        String input = "2\n4\n500\n12\n"; // Select account, then deposit
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Menu menu = new Menu();
        menu.showMainMenu();
        
        String output = outputStream.toString();
        assertTrue("Should confirm deposit", output.contains("Deposit successful. Your new balance is $500.00"));
    }

    @Test
    public void testWithdrawFunds() {
        String input = "2\n5\n200\n12\n"; // Select account, then withdraw
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Menu menu = new Menu();
        menu.showMainMenu();
        
        String output = outputStream.toString();
        assertTrue("Should confirm withdrawal", output.contains("Withdrawal successful. Your new balance is $300.00"));
    }

    @Test
    public void testInsufficientFundsWithdraw() {
        String input = "2\n5\n5000\n12\n"; // Select account, then attempt to withdraw
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Menu menu = new Menu();
        menu.showMainMenu();
        
        String output = outputStream.toString();
        assertTrue("Should show insufficient funds", output.contains("Insufficient funds in account."));
    }

    @Test
    public void testAccountTypeSelection() {
        // Test checking account creation
        String input = "1\nJohn\n01/01/1990\n123456789\n101\n1\n500\n12\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Menu menu = new Menu();
        menu.showMainMenu();
        
        String output = outputStream.toString();
        assertTrue("Should create checking account successfully", output.contains("CHECKING account created"));
        
        System.setOut(originalOut);
        System.setIn(originalIn);
        
        // Set up new streams for savings account test
        ByteArrayOutputStream savingsOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(savingsOutputStream));
        
        // Simulate user input for creating a savings account
        String savingsInput = "1\nJohnson\n02/02/1995\n987654321\n202\n2\n1000\n11\n";
        System.setIn(new ByteArrayInputStream(savingsInput.getBytes()));
        
        // Create and run the menu again
        Menu menu2 = new Menu();
        menu2.showMainMenu();
        
        // Check output contains savings account creation confirmation
        String savingsOutput = savingsOutputStream.toString();
        assertTrue("Should create savings account successfully", savingsOutput.contains("SAVINGS account created"));
    }
}
