package tests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import bankingapp.*;

public class CompoundInterestTest  {
    private BankAccountDatabase db;
    private BankAccount savingsAccount;
    private BankAccount checkingAccount;
    private int savingsNumber;
    
    @BeforeEach
    public void setUp() {
        CompoundInterest.resetTestInterestFlag();
        db = new BankAccountDatabase();
        
        // Create a savings account with $1000
        savingsAccount = new BankAccount(1000.0, AccountType.SAVINGS);
        savingsNumber = db.generateBankAccountNumber(savingsAccount);
        db.addBankAccount(savingsAccount);
        
        // Create a checking account with $1000 (for comparison)
        checkingAccount = new BankAccount(1000.0, AccountType.CHECKING);
        db.addBankAccount(checkingAccount);
    }

    @Test
    public void testCompoundInterestAppliedToSavings() throws InterruptedException {
        // Set test mode and create compound interest task
        System.setProperty("test.mode", "true");
        CompoundInterest interestTask = new CompoundInterest(db, 2000); // 2s interval
        Thread thread = new Thread(interestTask);
        
        try {
            // Start compound interest thread
            thread.start();
            
            // Wait slightly longer than the interest interval
            Thread.sleep(3000);
            
            // Verify savings account received interest (20% of 1000 = 200, new balance = 1200)
            assertEquals(1200.0, savingsAccount.getCurrentBalance(), 0.001,
                "Savings account should receive 20% interest");
                
            // Verify checking account did not receive interest
            assertEquals(1000.0, checkingAccount.getCurrentBalance(), 0.001,
                "Checking account should not receive interest");
        } finally {
            // Clean up
            thread.interrupt();
            thread.join();
            System.clearProperty("test.mode");
        }
    }
    
    @Test
    public void testInterestRate() {
        assertEquals(20.0, CompoundInterest.getInterestRate(), 0.001,
            "Interest rate should be 20%");
    }
    
    @Test
    public void testNoInterestOnInactiveAccount() throws InterruptedException {
        // Deactivate savings account
        db.deactivateAccount(savingsNumber);
        
        // Set test mode and create compound interest task
        System.setProperty("test.mode", "true");
        CompoundInterest interestTask = new CompoundInterest(db, 2000);
        Thread thread = new Thread(interestTask);
        
        try {
            thread.start();
            Thread.sleep(3000);
            
            // Verify no interest was applied to inactive account
            assertEquals(1000.0, savingsAccount.getCurrentBalance(), 0.001,
                "Inactive savings account should not receive interest");
        } finally {
            thread.interrupt();
            thread.join();
            System.clearProperty("test.mode");
        }
    }
}