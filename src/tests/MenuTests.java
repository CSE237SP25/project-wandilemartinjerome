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
        String input = "1\nJohn\n01/01/1990\n123456789\n101\n1\n500\n12\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        Menu menu = new Menu();
        menu.showMainMenu();
        
        String output = outputStream.toString();
        assertTrue("Should create checking account", output.contains("CHECKING account created"));
    }
}
