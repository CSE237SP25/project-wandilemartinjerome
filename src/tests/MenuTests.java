package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bankingapp.Menu;

public class MenuTests {
    
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    
    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outputStream));
    }
    
    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }
    
    @Test
    public void testCheckBalance() {
        // Simulate user input: select account (2), account number, check balance (3), exit (11)
        String input = "3\n11\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Menu menu = new Menu();
        menu.showMainMenu();
        
        String output = outputStream.toString();
        assertTrue("Output should show current balance", output.contains("Current Balance:"));
        assertTrue("Output should show balance amount", output.contains("$1000.00"));
    }
    
    @Test
    public void testDepositFunds() {
        // Simulate user input: deposit (4), amount, exit (11)
        String input = "4\n500\n11\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Menu menu = new Menu();
        menu.showMainMenu();
        
        String output = outputStream.toString();
        assertTrue("Output should confirm deposit", output.contains("Successfully deposited $500.00"));
        assertTrue("Output should show new balance", output.contains("New Balance: $1500.00"));
    }
    
    @Test
    public void testWithdrawFunds() {
        // Simulate user input: withdraw (5), amount, exit (11)
        String input = "5\n300\n11\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Menu menu = new Menu();
        menu.showMainMenu();
        
        String output = outputStream.toString();
        assertTrue("Output should confirm withdrawal", output.contains("Successfully withdrew $300.00"));
        assertTrue("Output should show new balance", output.contains("New Balance: $700.00"));
    }
    
    @Test
    public void testInsufficientFundsWithdraw() {
        // First deposit some funds to establish a baseline, then withdraw more than available
        String input = "5\n100\n5\n1000\n3\n11\n";  // Try to withdraw $2000 from the default account (with $1000)
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Menu menu = new Menu();
        menu.showMainMenu();
        
        String output = outputStream.toString();
        assertTrue("Output should show insufficient funds message", output.contains("Insufficient funds"));
    }
}
