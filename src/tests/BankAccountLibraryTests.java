package tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import bankingapp.BankAccount;
import bankingapp.BankAccountLibrary;

public class BankAccountLibraryTests {
    
    private BankAccountLibrary library;
    private BankAccount account;
    
    @Before
    public void setUp() {
        library = new BankAccountLibrary();
        account = new BankAccount(100.0);
    }
    
    @Test
    public void testBankAccountNumber() {
        int hashCode = library.BankAccountNumber(account);
        assertNotEquals(0, hashCode);
    }
    
    @Test
    public void testaddAccount() {
        library.addAccount(account);
        int hash = library.BankAccountNumber(account);
        assertTrue(library.findAccount(hash));
    }
    
    @Test
    public void testFindAccount() {
        library.addAccount(account);
        int hash = library.BankAccountNumber(account);
        assertTrue(library.findAccount(hash));
        
        // Test with non-existent account
        assertFalse(library.findAccount(12345));
    }
    
    @Test
    public void testDeleteAccount() {
        library.addAccount(account);
        int hash = library.BankAccountNumber(account);
        
        // Verify the account exists before deletion
        assertTrue(library.findAccount(hash));
        
        // Delete the account
        library.deleteAccount(account, hash);
        
        // The account should no longer be found after deletion
        assertFalse(library.findAccount(hash));
    }
} 