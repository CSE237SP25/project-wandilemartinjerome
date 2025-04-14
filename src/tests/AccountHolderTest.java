package tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import bankingapp.AccountHolder;

public class AccountHolderTest {
    private AccountHolder accountHolder;
    private static final String TEST_PASSWORD = "test123";
    private static final String TEST_LASTNAME = "Doe";
    private static final String TEST_BIRTHDAY = "01/01/1990";
    private static final int TEST_SSN = 123456789;
    private static final int TEST_BANK_CODE = 1234;

    @Before
    public void setUp() {
        accountHolder = new AccountHolder();
        accountHolder.setPersonalInfo(TEST_LASTNAME, TEST_BIRTHDAY, TEST_SSN, TEST_BANK_CODE);
    }

    @Test
    public void testGetPersonalInfo_NoPassword() {
        // When no password is set and info is not hidden
        String info = accountHolder.getPersonalInfo(null);
        assertNotNull(info);
        assertTrue(info.contains(TEST_LASTNAME));
        assertTrue(info.contains(TEST_BIRTHDAY));
        assertTrue(info.contains(String.valueOf(TEST_SSN)));
        assertTrue(info.contains(String.valueOf(TEST_BANK_CODE)));
    }

    @Test
    public void testGetPersonalInfo_WithPassword() {
        // When password is set but info is not hidden
        accountHolder.setPassword(TEST_PASSWORD);
        String info = accountHolder.getPersonalInfo(TEST_PASSWORD);
        assertNotNull(info);
        assertTrue(info.contains(TEST_LASTNAME));
    }

    @Test
    public void testGetPersonalInfo_WrongPassword() {
        // When password is set but wrong password is provided
        accountHolder.setPassword(TEST_PASSWORD);
        String info = accountHolder.getPersonalInfo("wrongpassword");
        assertNull(info);
    }

    @Test
    public void testGetPersonalInfo_HiddenInfo() {
        // When info is hidden and password is required
        accountHolder.setPassword(TEST_PASSWORD);
        accountHolder.hidePersonalInfo();
        
        // Wrong password
        String info = accountHolder.getPersonalInfo("wrongpassword");
        assertNull(info);
        
        // Correct password
        info = accountHolder.getPersonalInfo(TEST_PASSWORD);
        assertNotNull(info);
        assertTrue(info.contains(TEST_LASTNAME));
    }

    @Test
    public void testShowPersonalInfo() {
        accountHolder.setPassword(TEST_PASSWORD);
        accountHolder.hidePersonalInfo();
        
        // Wrong password
        String wrongPassword = "wrongpassword";
        boolean result = accountHolder.showPersonalInfo(wrongPassword);
        assertFalse("Wrong password should return false", result);
        assertNull("Info should stay hidden with wrong password", 
                  accountHolder.getPersonalInfo(wrongPassword));
        
        // Correct password
        result = accountHolder.showPersonalInfo(TEST_PASSWORD);
        assertTrue("Correct password should return true", result);
        String info = accountHolder.getPersonalInfo(TEST_PASSWORD);
        assertNotNull("Info should be visible with correct password", info);
        assertTrue("Info should contain lastname", info.contains(TEST_LASTNAME));
    }

    @Test
    public void testBankAccountManagement() {
        int accountNumber = 12345;
        
        // Add bank account
        accountHolder.addBankAccount(accountHolder, accountNumber);
        assertTrue(accountHolder.findBankAccount(accountHolder, accountNumber));
        
        // Remove bank account
        accountHolder.removeBankAccount(accountHolder, accountNumber);
        assertFalse(accountHolder.findBankAccount(accountHolder, accountNumber));
    }
} 