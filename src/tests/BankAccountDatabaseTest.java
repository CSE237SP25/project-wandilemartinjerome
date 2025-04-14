package tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import bankingapp.BankAccountDatabase;
import bankingapp.BankAccount;
import bankingapp.AccountHolder;

public class BankAccountDatabaseTest {
    private BankAccountDatabase database;
    private AccountHolder accountHolder;
    private BankAccount bankAccount;
    
    private static final String TEST_LASTNAME = "Doe";
    private static final String TEST_BIRTHDAY = "01/01/1990";
    private static final int TEST_SSN = 123456789;
    private static final int TEST_BANK_CODE = 1234;
    private static final double INITIAL_BALANCE = 1000.0;

    @Before
    public void setUp() {
        database = new BankAccountDatabase();
        accountHolder = new AccountHolder(TEST_LASTNAME, TEST_BIRTHDAY, TEST_SSN, TEST_BANK_CODE);
        bankAccount = new BankAccount(INITIAL_BALANCE);
    }

    @Test
    public void testAddAndFindAccountHolder() {
        int accountNumber = database.generateAccountNumber(accountHolder);
        database.addAccountHolder(accountHolder);
        
        assertTrue(database.hasAccountHolder(accountNumber));
        assertEquals(1, database.getAccountCount());
    }

    @Test
    public void testAddAndFindBankAccount() {
        BankAccount account = new BankAccount(500.0);
        int accountNumber = database.generateBankAccountNumber(account);
        database.addBankAccount(account);

        assertTrue(database.hasBankAccount(accountNumber));
        assertEquals(account, database.getBankAccount(accountNumber));
    }

    @Test
    public void testRemoveAccountHolder() {
        int accountNumber = database.generateAccountNumber(accountHolder);
        database.addAccountHolder(accountHolder);
        
        assertTrue(database.hasAccountHolder(accountNumber));
        database.removeAccountHolder(accountNumber);
        assertFalse(database.hasAccountHolder(accountNumber));
    }

    @Test
    public void testRemoveBankAccount() {
        BankAccount account = new BankAccount(500.0);
        int accountNumber = database.generateBankAccountNumber(account);
        database.addBankAccount(account);

        assertTrue(database.hasBankAccount(accountNumber));
        database.removeBankAccount(accountNumber);
        assertFalse(database.hasBankAccount(accountNumber));
    }

    @Test
    public void testAccountStatus() {
        int accountNumber = database.generateAccountNumber(accountHolder);
        database.addAccountHolder(accountHolder);
        
        // Account should be active by default
        assertTrue(database.isAccountActive(accountNumber));
        
        // Freeze account
        assertTrue(database.freezeAccount(accountNumber));
        assertFalse(database.isAccountActive(accountNumber));
        
        // Unfreeze account
        assertTrue(database.unfreezeAccount(accountNumber));
        assertTrue(database.isAccountActive(accountNumber));
    }

    @Test
    public void testAccountCounts() {
        // Add multiple accounts
        AccountHolder holder1 = new AccountHolder("Smith", "02/02/1990", 111111111, 1111);
        AccountHolder holder2 = new AccountHolder("Johnson", "03/03/1990", 222222222, 2222);
        
        database.addAccountHolder(holder1);
        database.addAccountHolder(holder2);
        
        assertEquals(2, database.getAccountCount());
        assertEquals(2, database.getActiveAccountCount());
        assertEquals(0, database.getFrozenAccountCount());
        
        // Freeze one account
        database.freezeAccount(database.generateAccountNumber(holder1));
        assertEquals(2, database.getAccountCount());
        assertEquals(1, database.getActiveAccountCount());
        assertEquals(1, database.getFrozenAccountCount());
    }

    @Test
    public void testGetAccountHolders() {
        database.addAccountHolder(accountHolder);
        AccountHolder holder2 = new AccountHolder("Smith", "02/02/1990", 111111111, 1111);
        database.addAccountHolder(holder2);
        
        assertEquals(2, database.getAccountHolders().size());
    }

    @Test
    public void testGetAccountBalance() {
        BankAccount account = new BankAccount(500.0);
        int accountNumber = database.generateBankAccountNumber(account);
        database.addBankAccount(account);

        assertEquals(500.0, database.getAccountBalance(accountNumber), 0.01);
        database.removeBankAccount(accountNumber);
        assertEquals(-1, database.getAccountBalance(accountNumber), 0.01); // Account not found
    }
}