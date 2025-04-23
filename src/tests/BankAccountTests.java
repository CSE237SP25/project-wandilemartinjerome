package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bankingapp.BankAccount;
import bankingapp.AccountType;
import bankingapp.TransactionType;

public class BankAccountTests {
    
    @Before
    public void setUp() {
        // Enable test mode to allow withdrawals from Savings accounts
        System.setProperty("test.mode", "true");
    }
    
    @After
    public void tearDown() {
        // Clear test mode after each test
        System.clearProperty("test.mode");
    }
    
    @Test
    public void testNewAccountHasZeroBalance() {
        BankAccount account = new BankAccount();
        assertEquals(0.0, account.getCurrentBalance(), 0.001);
    }
    
    @Test
    public void testAccountWithInitialBalance() {
        BankAccount account = new BankAccount(100.0);
        assertEquals(100.0, account.getCurrentBalance(), 0.001);
    }
    
    @Test
    public void testDeposit() {
        BankAccount account = new BankAccount();
        account.deposit(50.0);
        assertEquals(50.0, account.getCurrentBalance(), 0.001);
        
        account.deposit(25.0);
        assertEquals(75.0, account.getCurrentBalance(), 0.001);
    }
    
    @Test
    public void testNegativeDeposit() {
        BankAccount account = new BankAccount();
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-50.0));
    }
    
    @Test
    public void testWithdraw() {
        BankAccount account = new BankAccount(100);
        boolean result1 = account.withdraw(50.0);
        assertTrue("Withdrawal should succeed", result1);
        assertEquals(50.0, account.getCurrentBalance(), 0.001);
        
        boolean result2 = account.withdraw(25.0);
        assertTrue("Withdrawal should succeed", result2);
        assertEquals(25.0, account.getCurrentBalance(), 0.001);
    }

    @Test
    public void testWithdrawFromCheckingAccount() {
        BankAccount checkingAccount = new BankAccount(100.0, AccountType.CHECKING);
        checkingAccount.withdraw(50.0);
        assertEquals(50.0, checkingAccount.getCurrentBalance(), 0.001);
    }

    @Test
    public void testWithdrawFromSavingsAccount() {
        BankAccount savingsAccount = new BankAccount(100.0, AccountType.SAVINGS);
        assertThrows(IllegalArgumentException.class, () -> savingsAccount.withdraw(50.0));
        assertEquals(100.0, savingsAccount.getCurrentBalance(), 0.001);
    }

    
    @Test
    public void testNegativeWithdraw() {
        BankAccount account = new BankAccount(100);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-50.0));
    }
    
    @Test
    public void testInsufficientFunds() {
        BankAccount account = new BankAccount(100);
        boolean result = account.withdraw(150.0);
        assertFalse("Withdrawal should fail due to insufficient funds", result);
        assertEquals("Balance should remain unchanged", 100.0, account.getCurrentBalance(), 0.001);
    }

    // Tests for deposit functionality
    @Test
    public void testSimpleDeposit() {
        BankAccount account = new BankAccount(100);
        account.deposit(25);
        assertEquals(125.0, account.getCurrentBalance(), 0.005);
    }
    
    // Tests for balance functionality
    @Test
    public void testViewEmptyBalance() {
        BankAccount account = new BankAccount();
        assertEquals(0, account.getCurrentBalance(), 0.005);
    }

    @Test
    public void testViewSomeBalance() {
        BankAccount account = new BankAccount(25);
        assertEquals(25, account.getCurrentBalance(), 0.005);
    }

    // Tests for withdrawal functionality
    @Test
    public void testSuccessfulWithdrawal() {
        BankAccount account = new BankAccount(100.0);
        boolean result = account.withdraw(50.0);
        assertTrue("Withdrawal should succeed", result);
        assertEquals(50.0, account.getCurrentBalance(), 0.01);
        assertEquals(2, account.getTransactionHistory().size());
        assertEquals(TransactionType.WITHDRAWAL, account.getTransactionHistory().get(1).getType());
    }
    
    @Test
    public void testInsufficientFundsWithdrawal() {
        BankAccount account = new BankAccount(25.0);
        boolean result = account.withdraw(50.0);
        assertFalse("Withdrawal should fail due to insufficient funds", result);
        assertEquals(25.0, account.getCurrentBalance(), 0.01);
        assertEquals(2, account.getTransactionHistory().size()); // Only the initial deposit transaction
        assertEquals(TransactionType.DEPOSIT, account.getTransactionHistory().get(0).getType());
        assertEquals(TransactionType.FAILED, account.getTransactionHistory().get(1).getType());
    }

    // Tests for transfer functionality
    @Test
    public void testSuccessfulTransfer() {
        BankAccount sourceAccount = new BankAccount(100.0);
        BankAccount destinationAccount = new BankAccount(50.0);
        boolean result = sourceAccount.transfer(destinationAccount, 25.0);
        assertTrue(result);
        assertEquals(75.0, sourceAccount.getCurrentBalance(), 0.005);
        assertEquals(75.0, destinationAccount.getCurrentBalance(), 0.005);
    }

    @Test
    public void testInsufficientFundsTransfer() {
        BankAccount sourceAccount = new BankAccount(20.0);
        BankAccount destinationAccount = new BankAccount(50.0);
        
        // Changed from expecting exception to checking boolean result
        boolean result = sourceAccount.transfer(destinationAccount, 25.0);
        assertFalse("Transfer should fail due to insufficient funds", result);
        
        // Verify balances remain unchanged
        assertEquals(20.0, sourceAccount.getCurrentBalance(), 0.005);
        assertEquals(50.0, destinationAccount.getCurrentBalance(), 0.005);
    }

    @Test
    public void testNegativeTransfer() {
        BankAccount sourceAccount = new BankAccount(100.0);
        BankAccount destinationAccount = new BankAccount(50.0);
        assertThrows(IllegalArgumentException.class, () -> 
            sourceAccount.transfer(destinationAccount, -25.0));
        assertEquals(100.0, sourceAccount.getCurrentBalance(), 0.005);
        assertEquals(50.0, destinationAccount.getCurrentBalance(), 0.005);
    }

    @Test
    public void testNullDestinationTransfer() {
        BankAccount sourceAccount = new BankAccount(100.0);
        assertThrows(IllegalArgumentException.class, () -> 
            sourceAccount.transfer(null, 25.0));
        assertEquals(100.0, sourceAccount.getCurrentBalance(), 0.005);
    }

    // Tests for maximum withdrawal and deposit limits functionality
    @Test
    public void testDefaultWithdrawalLimit() {
        BankAccount account = new BankAccount(2000.0);
        assertEquals(1000.0, account.getMaxWithdrawalLimit(), 0.005);
    }

    @Test
    public void testDefaultDepositLimit() {
        BankAccount account = new BankAccount();
        assertEquals(10000.0, account.getMaxDepositLimit(), 0.005);
    }

    @Test
    public void testCustomLimitsConstructor() {
        BankAccount account = new BankAccount(500.0, 2000.0, 15000.0);
        assertEquals(2000.0, account.getMaxWithdrawalLimit(), 0.005);
        assertEquals(15000.0, account.getMaxDepositLimit(), 0.005);
    }

    @Test
    public void testSetWithdrawalLimit() {
        BankAccount account = new BankAccount();
        account.setMaxWithdrawalLimit(2500.0);
        assertEquals(2500.0, account.getMaxWithdrawalLimit(), 0.005);
    }

    @Test
    public void testSetDepositLimit() {
        BankAccount account = new BankAccount();
        account.setMaxDepositLimit(20000.0);
        assertEquals(20000.0, account.getMaxDepositLimit(), 0.005);
    }

    @Test 
    public void testNegativeWithdrawalLimit() {
        BankAccount account = new BankAccount();
        assertThrows(IllegalArgumentException.class, () -> 
            account.setMaxWithdrawalLimit(-500.0));
        assertEquals(1000.0, account.getMaxWithdrawalLimit(), 0.005);
    }

    @Test
    public void testNegativeDepositLimit() {
        BankAccount account = new BankAccount();
        assertThrows(IllegalArgumentException.class, () -> 
            account.setMaxDepositLimit(-1000.0));
        assertEquals(10000.0, account.getMaxDepositLimit(), 0.005);
    }

    @Test
    public void testExceedWithdrawalLimit() {
        BankAccount account = new BankAccount(2000.0, 1000.0, 10000.0);
        assertEquals(1000.0, account.getMaxWithdrawalLimit(), 0.01);
        
        assertThrows(IllegalArgumentException.class, () -> 
            account.withdraw(1500.0));
        
        assertEquals(2000.0, account.getCurrentBalance(), 0.01);
    }

    @Test
    public void testExceedDepositLimit() {
        BankAccount account = new BankAccount(1000.0);
        assertThrows(IllegalArgumentException.class, () -> 
            account.deposit(15000.0));
        assertEquals(1000.0, account.getCurrentBalance(), 0.01);
    }

    @Test
    public void testTransferExceedingWithdrawalLimit() {
        BankAccount sourceAccount = new BankAccount(2000.0);
        BankAccount destinationAccount = new BankAccount(500.0);
        assertThrows(IllegalArgumentException.class, () -> 
            sourceAccount.transfer(destinationAccount, 1500.0));
        assertEquals(2000.0, sourceAccount.getCurrentBalance(), 0.01);
        assertEquals(500.0, destinationAccount.getCurrentBalance(), 0.01);
    }

    // Tests for AccountType functionality
    @Test
    public void testCreateCheckingAccount() {
        BankAccount account = new BankAccount(AccountType.CHECKING);
        assertEquals(AccountType.CHECKING, account.getAccountType());
        assertEquals(0.0, account.getCurrentBalance(), 0.001);
    }

    @Test
    public void testCreateSavingsAccount() {
        BankAccount account = new BankAccount(AccountType.SAVINGS);
        assertEquals(AccountType.SAVINGS, account.getAccountType());
        assertEquals(0.0, account.getCurrentBalance(), 0.001);
    }

    @Test
    public void testCreateAccountWithBalanceAndType() {
        BankAccount checkingAccount = new BankAccount(100.0, AccountType.CHECKING);
        assertEquals(AccountType.CHECKING, checkingAccount.getAccountType());
        assertEquals(100.0, checkingAccount.getCurrentBalance(), 0.001);

        BankAccount savingsAccount = new BankAccount(250.0, AccountType.SAVINGS);
        assertEquals(AccountType.SAVINGS, savingsAccount.getAccountType());
        assertEquals(250.0, savingsAccount.getCurrentBalance(), 0.001);
    }

    @Test
    public void testCreateAccountWithBalanceAndLimitsAndType() {
        BankAccount checkingAccount = new BankAccount(100.0, 2000.0, 15000.0, AccountType.CHECKING);
        assertEquals(AccountType.CHECKING, checkingAccount.getAccountType());
        assertEquals(100.0, checkingAccount.getCurrentBalance(), 0.001);
        assertEquals(2000.0, checkingAccount.getMaxWithdrawalLimit(), 0.001);
        assertEquals(15000.0, checkingAccount.getMaxDepositLimit(), 0.001);

        BankAccount savingsAccount = new BankAccount(250.0, 3000.0, 20000.0, AccountType.SAVINGS);
        assertEquals(AccountType.SAVINGS, savingsAccount.getAccountType());
        assertEquals(250.0, savingsAccount.getCurrentBalance(), 0.001);
        assertEquals(3000.0, savingsAccount.getMaxWithdrawalLimit(), 0.001);
        assertEquals(20000.0, savingsAccount.getMaxDepositLimit(), 0.001);
    }
}