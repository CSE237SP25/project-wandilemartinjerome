package tests;

import static org.junit.Assert.*;
import org.junit.Test;

import bankingapp.BankAccount;
import bankingapp.BusinessAccount;

/**
 * Test class for BusinessAccount functionality
 */
public class BusinessAccountTests {
    
    // Constants for business account default limits
    private static final double EXPECTED_BUSINESS_MAX_WITHDRAWAL = 5000.0;
    private static final double EXPECTED_BUSINESS_MAX_DEPOSIT = 50000.0;
    
    // Constants for regular account default limits (for comparison)
    private static final double REGULAR_MAX_WITHDRAWAL = 1000.0;
    private static final double REGULAR_MAX_DEPOSIT = 10000.0;
    
    @Test
    public void testBusinessAccountCreationWithZeroBalance() {
        BusinessAccount account = new BusinessAccount();
        assertEquals(0.0, account.getCurrentBalance(), 0.001);
        assertEquals(EXPECTED_BUSINESS_MAX_WITHDRAWAL, account.getMaxWithdrawalLimit(), 0.001);
        assertEquals(EXPECTED_BUSINESS_MAX_DEPOSIT, account.getMaxDepositLimit(), 0.001);
    }
    
    @Test
    public void testBusinessAccountCreationWithInitialBalance() {
        double initialBalance = 2500.0;
        BusinessAccount account = new BusinessAccount(initialBalance);
        assertEquals(initialBalance, account.getCurrentBalance(), 0.001);
        assertEquals(EXPECTED_BUSINESS_MAX_WITHDRAWAL, account.getMaxWithdrawalLimit(), 0.001);
        assertEquals(EXPECTED_BUSINESS_MAX_DEPOSIT, account.getMaxDepositLimit(), 0.001);
    }
    
    @Test
    public void testBusinessAccountWithCustomLimits() {
        double initialBalance = 1000.0;
        double customWithdrawalLimit = 10000.0;
        double customDepositLimit = 100000.0;
        
        BusinessAccount account = new BusinessAccount(initialBalance, customWithdrawalLimit, customDepositLimit);
        assertEquals(initialBalance, account.getCurrentBalance(), 0.001);
        assertEquals(customWithdrawalLimit, account.getMaxWithdrawalLimit(), 0.001);
        assertEquals(customDepositLimit, account.getMaxDepositLimit(), 0.001);
    }
    
    @Test
    public void testBusinessAccountToString() {
        double initialBalance = 5000.0;
        BusinessAccount account = new BusinessAccount(initialBalance);
        String accountString = account.toString();
        assertTrue(accountString.contains("Business Account"));
        assertTrue(accountString.contains("5000.00"));
    }
    
    @Test
    public void testBusinessVsRegularAccountLimits() {
        BankAccount regularAccount = new BankAccount();
        BusinessAccount businessAccount = new BusinessAccount();
        
        // Verify business account limits are higher than regular account limits
        assertTrue(businessAccount.getMaxWithdrawalLimit() > regularAccount.getMaxWithdrawalLimit());
        assertTrue(businessAccount.getMaxDepositLimit() > regularAccount.getMaxDepositLimit());
        
        // Verify exact values
        assertEquals(REGULAR_MAX_WITHDRAWAL, regularAccount.getMaxWithdrawalLimit(), 0.001);
        assertEquals(REGULAR_MAX_DEPOSIT, regularAccount.getMaxDepositLimit(), 0.001);
        assertEquals(EXPECTED_BUSINESS_MAX_WITHDRAWAL, businessAccount.getMaxWithdrawalLimit(), 0.001);
        assertEquals(EXPECTED_BUSINESS_MAX_DEPOSIT, businessAccount.getMaxDepositLimit(), 0.001);
    }
    
    @Test
    public void testBusinessAccountDeposit() {
        BusinessAccount account = new BusinessAccount(1000.0);
        double depositAmount = 20000.0;
        
        // Should succeed because the amount is within the business account's higher deposit limit
        account.deposit(depositAmount);
        assertEquals(21000.0, account.getCurrentBalance(), 0.001);
    }
    
    @Test
    public void testRegularAccountFailsHighDeposit() {
        BankAccount account = new BankAccount(1000.0);
        double depositAmount = 20000.0;
        
        // Should fail because the amount exceeds the regular account's deposit limit
        try {
            account.deposit(depositAmount);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
            assertEquals(1000.0, account.getCurrentBalance(), 0.001);
        }
    }
    
    @Test
    public void testBusinessAccountWithdrawal() {
        BusinessAccount account = new BusinessAccount(10000.0);
        double withdrawalAmount = 4000.0;
        
        // Should succeed because the amount is within the business account's higher withdrawal limit
        account.withdraw(withdrawalAmount);
        assertEquals(6000.0, account.getCurrentBalance(), 0.001);
    }
    
    @Test
    public void testRegularAccountFailsHighWithdrawal() {
        BankAccount account = new BankAccount(10000.0);
        double withdrawalAmount = 4000.0;
        
        // Should fail because the amount exceeds the regular account's withdrawal limit
        try {
            account.withdraw(withdrawalAmount);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
            assertEquals(10000.0, account.getCurrentBalance(), 0.001);
        }
    }
    
    @Test
    public void testBusinessAccountExceedsLimit() {
        BusinessAccount account = new BusinessAccount(10000.0);
        double withdrawalAmount = 7000.0;
        
        // Should fail because the amount exceeds even the business account's higher withdrawal limit
        try {
            account.withdraw(withdrawalAmount);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
            assertEquals(10000.0, account.getCurrentBalance(), 0.001);
        }
    }
    
    @Test
    public void testBusinessAccountTransfer() {
        BusinessAccount sourceAccount = new BusinessAccount(10000.0);
        BusinessAccount destinationAccount = new BusinessAccount(2000.0);
        double transferAmount = 4500.0;
        
        // Should succeed because the amount is within the business account's withdrawal limit
        boolean success = sourceAccount.transfer(destinationAccount, transferAmount);
        
        assertTrue(success);
        assertEquals(5500.0, sourceAccount.getCurrentBalance(), 0.001);
        assertEquals(6500.0, destinationAccount.getCurrentBalance(), 0.001);
    }
    
    @Test
    public void testChangeBusinessAccountLimits() {
        BusinessAccount account = new BusinessAccount();
        double newWithdrawalLimit = 15000.0;
        double newDepositLimit = 200000.0;
        
        account.setMaxWithdrawalLimit(newWithdrawalLimit);
        account.setMaxDepositLimit(newDepositLimit);
        
        assertEquals(newWithdrawalLimit, account.getMaxWithdrawalLimit(), 0.001);
        assertEquals(newDepositLimit, account.getMaxDepositLimit(), 0.001);
    }
}