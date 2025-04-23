package tests;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bankingapp.Menu;

/**
 * Tests for the Menu class functionality.
 */
public class MenuTests {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

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
        assertTrue("Should confirm deposit", output.contains("successfully deposited"));
    }

    @Test
    public void testWithdrawFunds() {
        String input = "2\n5\n200\n12\n"; // Select account, then withdraw
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Menu menu = new Menu();
        menu.showMainMenu();
        
        String output = outputStream.toString();
        assertTrue("Should confirm withdrawal", output.contains("successfully withdrawn"));
    }

    @Test
    public void testInsufficientFundsWithdraw() {
        String input = "2\n5\n5000\n12\n"; // Select account, then attempt to withdraw
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Menu menu = new Menu();
        menu.showMainMenu();
        
        String output = outputStream.toString();
        assertTrue("Should show insufficient funds", output.contains("Insufficient funds"));
    }

    @Test
    public void testAccountTypeSelection() {
        // Test creating a checking account
        ByteArrayOutputStream checkingOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(checkingOutputStream));
        
        String checkingInput = "1\nSmith\n01/01/1990\n123456789\n101\n1\n500\n12\n"; // Correct input for account creation
        System.setIn(new ByteArrayInputStream(checkingInput.getBytes()));
        
        Menu menu1 = new Menu();
        menu1.showMainMenu();
        
        String checkingOutput = checkingOutputStream.toString();
        assertTrue("Should create checking account successfully", checkingOutput.contains("CHECKING account created"));
        
        // Test creating a savings account
        ByteArrayOutputStream savingsOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(savingsOutputStream));
        
        String savingsInput = "1\nJohnson\n02/02/1995\n987654321\n202\n2\n1000\n12\n"; // Correct input for account creation
        System.setIn(new ByteArrayInputStream(savingsInput.getBytes()));
        
        Menu menu2 = new Menu();
        menu2.showMainMenu();
        
        String savingsOutput = savingsOutputStream.toString();
        assertTrue("Should create savings account successfully", savingsOutput.contains("SAVINGS account created"));
    }
}
