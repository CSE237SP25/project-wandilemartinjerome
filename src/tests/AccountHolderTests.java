package tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import bankingapp.AccountHolder;

public class AccountHolderTests {
    
    private AccountHolder accountHolder;
    
    @Before
    public void setUp() {
        accountHolder = new AccountHolder();
    }
    
    @Test
    public void testSetPassword() {
        accountHolder.setPassword("test123");
        assertTrue(accountHolder.showPersonalInfo("test123"));
    }
    
    @Test
    public void testHidePersonalInfo() {
        accountHolder.hidePersonalInfo();
        assertNull(accountHolder.getPersonalInfo("wrongpassword"));
    }
    
    @Test
    public void testShowPersonalInfo() {
        accountHolder.setPassword("test123");
        accountHolder.hidePersonalInfo();
        
        // Test with correct password
        assertTrue(accountHolder.showPersonalInfo("test123"));
        assertNotNull(accountHolder.getPersonalInfo("test123"));
        
        // Test with incorrect password
        assertFalse(accountHolder.showPersonalInfo("wrongpassword"));
        assertNull(accountHolder.getPersonalInfo("wrongpassword"));
    }
    
    @Test
    public void testGetPersonalInfo() {
        // Test when info is not hidden
        assertNotNull(accountHolder.getPersonalInfo(null));
        
        // Test when info is hidden with correct password
        accountHolder.setPassword("test123");
        accountHolder.hidePersonalInfo();
        assertNotNull(accountHolder.getPersonalInfo("test123"));
        
        // Test when info is hidden with incorrect password
        assertNull(accountHolder.getPersonalInfo("wrongpassword"));
    }
    
    @Test
    public void testPersonalInfoVisibility() {
        // Initially visible
        assertNotNull(accountHolder.getPersonalInfo(null));
        
        // Hide with password
        accountHolder.setPassword("test123");
        accountHolder.hidePersonalInfo();
        assertNull(accountHolder.getPersonalInfo("wrongpassword"));
        
        // Show with correct password
        accountHolder.showPersonalInfo("test123");
        assertNotNull(accountHolder.getPersonalInfo("test123"));
    }
} 