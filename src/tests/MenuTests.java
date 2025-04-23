package tests;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bankingapp.*;

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
        Date tomorrow = new Date(System.currentTimeMillis() + 86400000);
        
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
        processed = sourceAccount.processScheduledTransfers(tomorrow);
        assertEquals(1, processed);
        assertEquals(INITIAL_BALANCE - transferAmount, sourceAccount.getCurrentBalance(), 0.01);
        assertEquals(1000.0, destinationAccount.getCurrentBalance(), 0.01);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testScheduleTransferPastDate() {
        Date yesterday = new Date(System.currentTimeMillis() - 86400000);
        sourceAccount.scheduleTransfer(destinationAccount, 100.0, yesterday, "Past transfer");
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void testScheduleTransferNegativeAmount() {
        Date tomorrow = new Date(System.currentTimeMillis() + 86400000);
        sourceAccount.scheduleTransfer(destinationAccount, -100.0, tomorrow, "Negative amount");
    }
    
    @Test
    public void testMultipleScheduledTransfers() {
        Date tomorrow = new Date(System.currentTimeMillis() + 86400000);
        
        // Schedule 3 transfers
        sourceAccount.scheduleTransfer(destinationAccount, 100.0, tomorrow, "Transfer 1");
        sourceAccount.scheduleTransfer(destinationAccount, 200.0, tomorrow, "Transfer 2");
        sourceAccount.scheduleTransfer(destinationAccount, 300.0, tomorrow, "Transfer 3");
        
        assertEquals(3, sourceAccount.getScheduledTransfers().size());
        
        // Process all transfers
        int processed = sourceAccount.processScheduledTransfers(tomorrow);
        
        assertEquals(3, processed);
        assertEquals(400.0, sourceAccount.getCurrentBalance(), 0.01);
        assertEquals(1100.0, destinationAccount.getCurrentBalance(), 0.01);
    }

    @Test
    public void testCheckBalance() {
        // Create account first, then select it, check balance, exit
        String input = "1\nDoe\n01/01/1990\n123456789\n101\n1\n1\n500\n2\n1\n12\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Menu menu = new Menu();
        menu.showMainMenu();
        
        String output = outputStream.toString();
        assertTrue("Should show current balance", output.contains("Current balance: $500.00"));
    }
    
    @Test
    public void testDepositFunds() {
        // Create account first, then select it, deposit, amount, exit
        String input = "1\nDoe\n01/01/1990\n123456789\n101\n1\n1\n500\n2\n1\n4\n500\n12\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Menu menu = new Menu();
        menu.showMainMenu();
        
        String output = outputStream.toString();
        assertTrue("Should confirm deposit", output.contains("Deposit successful. Your new balance is $1000.00"));
    }

    @Test
    public void testWithdrawFunds() {
        // Create account first, then select it, withdraw, amount, exit
        String input = "1\nDoe\n01/01/1990\n123456789\n101\n1\n1\n500\n2\n1\n5\n200\n12\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Menu menu = new Menu();
        menu.showMainMenu();
        
        String output = outputStream.toString();
        assertTrue("Should confirm withdrawal", output.contains("Withdrawal successful. Your new balance is $300.00"));
    }

    @Test
    public void testInsufficientFundsWithdraw() {
        // Create account first, then select it, withdraw, amount, exit
        String input = "1\nDoe\n01/01/1990\n123456789\n101\n1\n2\n500\n2\n1\n5\n5000\n12\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Menu menu = new Menu();
        menu.showMainMenu();
        
        String output = outputStream.toString();
        assertTrue("Should show error for savings account withdrawal", output.contains("Error: Can not withdraw from Savings Account"));
    }

    @Test
    public void testAccountTypeSelection() {
        // Test checking account creation with all required fields
        // Format: mainMenu(1=create), lastName, birthday, ssn, bankCode, accountCategory(1=personal), accountType(1=checking), initialAmount, exit
        String input = "1\nDoe\n01/01/1990\n123456789\n101\n1\n1\n500\n12\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Menu menu = new Menu();
        menu.showMainMenu();
        
        String output = outputStream.toString();
        assertTrue("Should create checking account successfully", output.contains("CHECKING account created"));
    }
}
