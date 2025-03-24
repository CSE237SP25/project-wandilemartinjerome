package tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import bankingapp.BankAccount;

public class BankAccountTests {
    
    private BankAccount account;
    private BankAccount accountWithInitialBalance;
    
    @Before
    public void setUp() {
        account = new BankAccount();
        accountWithInitialBalance = new BankAccount(100.0);
    }
    
    @Test
    public void testNewAccountHasZeroBalance() {
        assertEquals(0.0, account.getCurrentBalance(), 0.001);
    }
    
    @Test
    public void testAccountWithInitialBalance() {
        assertEquals(100.0, accountWithInitialBalance.getCurrentBalance(), 0.001);
    }
    
    @Test
    public void testDeposit() {
        account.deposit(50.0);
        assertEquals(50.0, account.getCurrentBalance(), 0.001);
        
        account.deposit(25.0);
        assertEquals(75.0, account.getCurrentBalance(), 0.001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDeposit() {
        account.deposit(-50.0);
    }
    
    @Test
    public void testWithdraw() {
        accountWithInitialBalance.withdraw(50.0);
        assertEquals(50.0, accountWithInitialBalance.getCurrentBalance(), 0.001);
        
        accountWithInitialBalance.withdraw(25.0);
        assertEquals(25.0, accountWithInitialBalance.getCurrentBalance(), 0.001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeWithdraw() {
        accountWithInitialBalance.withdraw(-50.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInsufficientFunds() {
        accountWithInitialBalance.withdraw(150.0);
    }
} 