package bankingapp;

import java.util.Date;
import java.util.HashMap;

/**
 * Represents an administrator account with elevated privileges
 * for managing the banking system.
 * 
 * @author Martin Rivera
 * @author Wandile Hannah
 * @author Jerome Hsing
 */
public class AdminAccount extends AccountHolder {
    
    private final String adminId;
    private final String securityClearance;
    private boolean isActive;
    private HashMap<String, Date> auditLog;
    
    /**
     * Creates a new admin account with default values
     */
    public AdminAccount() {
        super();
        this.adminId = "ADMIN" + System.currentTimeMillis();
        this.securityClearance = "STANDARD";
        this.isActive = true;
        this.auditLog = new HashMap<>();
    }
    
    /**
     * Creates a new admin account with the specified details
     * 
     * @param firstName First name of the admin
     * @param lastName Last name of the admin
     * @param birthday Birthday of the admin
     * @param ssn Social Security Number of the admin
     * @param adminId Unique administrator ID
     * @param securityClearance Security clearance level
     */
    public AdminAccount(String firstName, String lastName, int birthday, int ssn, 
                        String adminId, String securityClearance) {
        super();
        this.adminId = adminId;
        this.securityClearance = securityClearance;
        this.isActive = true;
        this.auditLog = new HashMap<>();
    }
    
    /**
     * Gets the administrator's unique ID
     * 
     * @return The admin ID
     */
    public String getAdminId() {
        return this.adminId;
    }
    
    /**
     * Gets the administrator's security clearance level
     * 
     * @return The security clearance
     */
    public String getSecurityClearance() {
        return this.securityClearance;
    }
    
    /**
     * Checks if the admin account is active
     * 
     * @return true if the account is active, false otherwise
     */
    public boolean isActive() {
        return this.isActive;
    }
    
    /**
     * Sets the active status of the admin account
     * 
     * @param active The new active status
     */
    public void setActive(boolean active) {
        this.isActive = active;
        logAction("Admin status changed to: " + (active ? "active" : "inactive"));
    }
    
    /**
     * Overrides customer account limits
     * 
     * @param account The account to modify
     * @param newWithdrawalLimit The new withdrawal limit
     * @param newDepositLimit The new deposit limit
     * @return true if successful, false otherwise
     */
    public boolean overrideAccountLimits(BankAccount account, double newWithdrawalLimit, double newDepositLimit) {
        if (!isActive) {
            return false;
        }
        
        if (account == null) {
            return false;
        }
        
        try {
            // Note: These methods need to be implemented in BankAccount class
            // If you merge from the withdraw branch, they will be available
            // For now, we'll just log the action
            logAction("Modified account limits for account: " + account.hashCode());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Creates a new bank account for a customer
     * 
     * @param customer The customer to create an account for
     * @param initialBalance The initial balance
     * @return The newly created bank account
     */
    public BankAccount createAccountForCustomer(AccountHolder customer, double initialBalance) {
        if (!isActive) {
            return null;
        }
        
        BankAccount newAccount = new BankAccount(initialBalance);
        customer.addBankacount(customer, newAccount.hashCode());
        
        logAction("Created new account for customer: " + customer.hashCode());
        return newAccount;
    }
    
    /**
     * Freezes a customer's account in the system
     * 
     * @param allAccounts The system's account registry
     * @param accountHash The hash code of the account to freeze
     * @return true if successful, false otherwise
     */
    public boolean freezeCustomerAccount(AllUserAccount allAccounts, int accountHash) {
        if (!isActive) {
            return false;
        }
        
        boolean result = allAccounts.freezeAccount(accountHash);
        if (result) {
            logAction("Froze account with hash: " + accountHash);
        } else {
            logAction("Failed to freeze account with hash: " + accountHash);
        }
        return result;
    }
    
    /**
     * Unfreezes a customer's account in the system
     * 
     * @param allAccounts The system's account registry
     * @param accountHash The hash code of the account to unfreeze
     * @return true if successful, false otherwise
     */
    public boolean unfreezeCustomerAccount(AllUserAccount allAccounts, int accountHash) {
        if (!isActive) {
            return false;
        }
        
        boolean result = allAccounts.unfreezeAccount(accountHash);
        if (result) {
            logAction("Unfroze account with hash: " + accountHash);
        } else {
            logAction("Failed to unfreeze account with hash: " + accountHash);
        }
        return result;
    }
    
    /**
     * Checks if a customer's account is active (not frozen)
     * 
     * @param allAccounts The system's account registry
     * @param accountHash The hash code of the account to check
     * @return true if the account is active, false if it's frozen or not found
     */
    public boolean isCustomerAccountActive(AllUserAccount allAccounts, int accountHash) {
        if (!isActive) {
            return false;
        }
        
        return allAccounts.isAccountActive(accountHash);
    }
    
    /**
     * Generates a system report
     * 
     * @param reportType The type of report to generate
     * @return A string containing the report
     */
    public String generateSystemReport(String reportType) {
        if (!isActive) {
            return "Admin account inactive";
        }
        
        logAction("Generated report of type: " + reportType);
        return "Report of type " + reportType + " generated by admin " + adminId;
    }
    
    /**
     * Generates an account status report
     * 
     * @param allAccounts The system's account registry
     * @return A string containing the account status report
     */
    public String generateAccountStatusReport(AllUserAccount allAccounts) {
        if (!isActive) {
            return "Admin account inactive";
        }
        
        StringBuilder report = new StringBuilder();
        report.append("Account Status Report\n");
        report.append("Generated by: ").append(adminId).append("\n");
        report.append("Time: ").append(new Date()).append("\n\n");
        report.append("Total accounts: ").append(allAccounts.getAccountCount()).append("\n");
        report.append("Active accounts: ").append(allAccounts.getActiveAccountCount()).append("\n");
        report.append("Frozen accounts: ").append(allAccounts.getFrozenAccountCount()).append("\n");
        
        logAction("Generated account status report");
        return report.toString();
    }
    
    /**
     * Audits a specific transaction
     * 
     * @param transactionId The ID of the transaction to audit
     * @return The audit result
     */
    public String auditTransaction(String transactionId) {
        if (!isActive) {
            return "Admin account inactive";
        }
        
        logAction("Audited transaction: " + transactionId);
        return "Transaction " + transactionId + " audited by admin " + adminId;
    }
    
    /**
     * Logs an administrative action with timestamp
     * 
     * @param action The action to log
     */
    private void logAction(String action) {
        auditLog.put(action, new Date());
    }
    
    /**
     * Gets the audit log of administrative actions
     * 
     * @return The audit log
     */
    public HashMap<String, Date> getAuditLog() {
        return new HashMap<>(auditLog); // Return a copy to prevent modification
    }
    
    /**
     * Clears the audit log
     * 
     * @return true if successful, false otherwise
     */
    public boolean clearAuditLog() {
        if (!isActive) {
            return false;
        }
        
        auditLog.clear();
        logAction("Audit log cleared");
        return true;
    }
}
