package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bankingapp.Menu;

public class MenuTests {
    
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final InputStream originalIn = System.in;

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

    @Test
    public void testAccountTypeSelection() {
        // Format: 1 (create account) -> last name -> birthday -> SSN -> bank code -> account type (1=checking) -> initial amount -> 11 (exit)
        String checkingInput = "1\nSmith\n01/01/1990\n123456789\n101\n1\n500\n11\n";
        System.setIn(new ByteArrayInputStream(checkingInput.getBytes()));

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
        // Format: 1 (create account) -> last name -> birthday -> SSN -> bank code -> account type (2=savings) -> initial amount -> 11 (exit)
        String savingsInput = "1\nJohnson\n02/02/1995\n987654321\n202\n2\n1000\n11\n";
        System.setIn(new ByteArrayInputStream(savingsInput.getBytes()));
        
        // Create and run the menu again
        Menu menu2 = new Menu();
        menu2.showMainMenu();
        
        // Check output contains savings account creation confirmation
        String savingsOutput = savingsOutputStream.toString();
        assertTrue("Should create savings account successfully", savingsOutput.contains( "SAVINGS account created"));
    }
}
