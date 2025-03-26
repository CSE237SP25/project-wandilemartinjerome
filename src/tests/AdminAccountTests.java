package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import bankingapp.AccountHolder;
import bankingapp.AdminAccount;
import bankingapp.AllUserAccount;
import bankingapp.BankAccount;

/**
 * Test class for AdminAccount functionality
 */
public class AdminAccountTests {

    @Test
    public void testAdminAccountCreation() {
        AdminAccount admin = new AdminAccount("John", "Doe", 19900101, 123456789, "ADMIN001", "HIGH");
        assertNotNull(admin);
        assertEquals("ADMIN001", admin.getAdminId());
        assertEquals("HIGH", admin.getSecurityClearance());
        assertTrue(admin.isActive());
    }
    
    @Test
    public void testSetActiveStatus() {
        AdminAccount admin = new AdminAccount();
        assertTrue(admin.isActive()); // Default is active
        
        admin.setActive(false);
        assertFalse(admin.isActive());
        
        admin.setActive(true);
        assertTrue(admin.isActive());
    }
    
    @Test
    public void testCreateAccountForCustomer() {
        AdminAccount admin = new AdminAccount();
        AccountHolder customer = new AccountHolder();
        
        BankAccount newAccount = admin.createAccountForCustomer(customer, 500.0);
        assertNotNull(newAccount);
        assertEquals(500.0, newAccount.getCurrentBalance(), 0.005);
        assertTrue(customer.findBankAccount(customer, newAccount.hashCode()));
    }
    
    @Test
    public void testAuditLog() {
        AdminAccount admin = new AdminAccount();
        
        // Perform some actions to generate audit entries
        admin.setActive(false);
        admin.setActive(true);
        admin.generateSystemReport("DAILY");
        admin.auditTransaction("TX12345");
        
        // Check that the audit log contains entries
        assertFalse(admin.getAuditLog().isEmpty());
        assertTrue(admin.getAuditLog().size() >= 4);
        
        // Test clearing the log
        assertTrue(admin.clearAuditLog());
        assertTrue(admin.getAuditLog().size() == 1); // Should have one entry for the clear action
    }
    
    @Test
    public void testInactiveAdminRestrictions() {
        AdminAccount admin = new AdminAccount();
        admin.setActive(false);
        
        // Verify that admin functions fail when inactive
        AccountHolder customer = new AccountHolder();
        BankAccount newAccount = admin.createAccountForCustomer(customer, 500.0);
        assertEquals(null, newAccount);
        
        assertFalse(admin.freezeCustomerAccount(new AllUserAccount(), 123456789));
        assertEquals("Admin account inactive", admin.generateSystemReport("DAILY"));
        assertEquals("Admin account inactive", admin.auditTransaction("TX12345"));
        assertFalse(admin.clearAuditLog());
    }
    
    @Test
    public void testGenerateSystemReport() {
        AdminAccount admin = new AdminAccount("Jane", "Smith", 19850315, 987654321, "ADMIN002", "MEDIUM");
        String report = admin.generateSystemReport("MONTHLY");
        assertTrue(report.contains("MONTHLY"));
        assertTrue(report.contains("ADMIN002"));
    }
    
    @Test
    public void testAuditTransaction() {
        AdminAccount admin = new AdminAccount();
        String auditResult = admin.auditTransaction("TX98765");
        assertTrue(auditResult.contains("TX98765"));
    }
    
    @Test
    public void testAccountFreezing() {
        AdminAccount admin = new AdminAccount();
        AllUserAccount allAccounts = new AllUserAccount();
        AccountHolder customer = new AccountHolder();
        
        // Add the customer to the system
        allAccounts.AddAcount(customer);
        int accountHash = allAccounts.AccountNumber(customer);
        
        // Test freezing
        assertTrue(admin.freezeCustomerAccount(allAccounts, accountHash));
        assertFalse(admin.isCustomerAccountActive(allAccounts, accountHash));
        
        // Test unfreezing
        assertTrue(admin.unfreezeCustomerAccount(allAccounts, accountHash));
        assertTrue(admin.isCustomerAccountActive(allAccounts, accountHash));
    }
    
    @Test
    public void testAccountStatusReport() {
        AdminAccount admin = new AdminAccount();
        AllUserAccount allAccounts = new AllUserAccount();
        
        // Add some accounts
        for (int i = 0; i < 5; i++) {
            allAccounts.AddAcount(new AccountHolder());
        }
        
        // Freeze a couple of accounts
        AccountHolder customer = new AccountHolder();
        allAccounts.AddAcount(customer);
        int accountHash = allAccounts.AccountNumber(customer);
        admin.freezeCustomerAccount(allAccounts, accountHash);
        
        // Generate and check the report
        String report = admin.generateAccountStatusReport(allAccounts);
        assertTrue(report.contains("Account Status Report"));
        assertTrue(report.contains("Total accounts: 6"));
        assertTrue(report.contains("Active accounts: 5"));
        assertTrue(report.contains("Frozen accounts: 1"));
    }
}
