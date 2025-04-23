package bankingapp;

import java.util.HashMap;
import java.util.Collection;

public class BankAccountDatabase {
    private HashMap<Integer, AccountHolder> userAccounts = new HashMap<>();
    private HashMap<Integer, BankAccount> bankAccounts = new HashMap<>();
    private HashMap<Integer, Boolean> accountStatus = new HashMap<>(); // Track account status (active/frozen)

    public int generateAccountNumber(AccountHolder info) {
        return info.hashCode();
    }

    public int generateBankAccountNumber(BankAccount account) {
        return account.hashCode();
    }

    public void addBankAccount(BankAccount account) {
        int hash = generateBankAccountNumber(account);
        if (!bankAccounts.containsKey(hash)) {
            bankAccounts.put(hash, account);
            accountStatus.put(hash, true); // Set account as active by default
        }
    }

    public boolean hasBankAccount(int accountNumber) {
        return bankAccounts.containsKey(accountNumber);
    }

    public BankAccount getBankAccount(int accountNumber) {
        return bankAccounts.get(accountNumber);
    }

    public void removeBankAccount(int accountNumber) {
        bankAccounts.remove(accountNumber);
        accountStatus.remove(accountNumber);
    }

    public double getAccountBalance(int accountNumber) {
        if (bankAccounts.containsKey(accountNumber)) {
            return bankAccounts.get(accountNumber).getCurrentBalance();
        }
        return -1; // Return -1 if account not found
    }

    public void addAccountHolder(AccountHolder holder) {
        int hash = generateAccountNumber(holder);
        if (!userAccounts.containsKey(hash)) {
            userAccounts.put(hash, holder);
            accountStatus.put(hash, true); // Set account as active by default
        }
    }

    public AccountHolder getAccountHolder(int accountNumber) {
        return userAccounts.get(accountNumber);
    }

    public Collection<AccountHolder> getAccountHolders() {
        return userAccounts.values();
    }

    public void removeAccountHolder(int accountNumber) {
        userAccounts.remove(accountNumber);
        accountStatus.remove(accountNumber);
    }

    /**
     * Checks if an account holder exists in the database.
     * 
     * @param accountNumber The account number to check
     * @return true if the account holder exists, false otherwise
     */
    public boolean hasAccountHolder(int accountNumber) {
        return userAccounts.containsKey(accountNumber);
    }

    /**
     * Gets the total number of accounts in the database.
     * 
     * @return The total number of accounts
     */
    public int getAccountCount() {
        return userAccounts.size();
    }

    /**
     * Gets the number of active accounts in the database.
     * 
     * @return The number of active accounts
     */
    public int getActiveAccountCount() {
        int count = 0;
        for (Boolean status : accountStatus.values()) {
            if (status) {
                count++;
            }
        }
        return count;
    }

    /**
     * Gets the number of frozen accounts in the database.
     * 
     * @return The number of frozen accounts
     */
    public int getFrozenAccountCount() {
        return accountStatus.size() - getActiveAccountCount();
    }

    /**
     * Checks if an account is active.
     * 
     * @param accountNumber The account number to check
     * @return true if the account is active, false if inactive or not found
     */
    public boolean isAccountActive(int accountNumber) {
        return accountStatus.getOrDefault(accountNumber, false);
    }

    /**
     * Freezes a bank account (same as deactivate).
     * 
     * @param accountNumber The account number to freeze
     * @return true if the account was found and frozen, false otherwise
     */
    public boolean freezeAccount(int accountNumber) {
        return deactivateAccount(accountNumber);
    }

    /**
     * Unfreezes a bank account (same as activate).
     * 
     * @param accountNumber The account number to unfreeze
     * @return true if the account was found and unfrozen, false otherwise
     */
    public boolean unfreezeAccount(int accountNumber) {
        return activateAccount(accountNumber);
    }

    /**
     * Deactivates a bank account.
     * 
     * @param accountNumber The account number to deactivate
     * @return true if the account was found and deactivated, false otherwise
     */
    public boolean deactivateAccount(int accountNumber) {
        if (accountStatus.containsKey(accountNumber)) {
            accountStatus.put(accountNumber, false);
            return true;
        }
        return false;
    }

    /**
     * Activates a bank account.
     * 
     * @param accountNumber The account number to activate
     * @return true if the account was found and activated, false otherwise
     */
    public boolean activateAccount(int accountNumber) {
        if (accountStatus.containsKey(accountNumber)) {
            accountStatus.put(accountNumber, true);
            return true;
        }
        return false;
    }

    public HashMap<Integer, BankAccount> getBankAccounts() {
        return new HashMap<>(bankAccounts); // Return a copy to prevent direct modification
    }
}