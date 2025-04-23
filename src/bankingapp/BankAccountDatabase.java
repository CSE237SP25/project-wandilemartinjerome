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

    public boolean hasAccountHolder(int accountNumber) {
        return userAccounts.containsKey(accountNumber);
    }

    public void removeAccountHolder(int accountNumber) {
        if (userAccounts.containsKey(accountNumber)) {
            userAccounts.remove(accountNumber);
            accountStatus.remove(accountNumber);
        }
    }

    public boolean freezeAccount(int accountNumber) {
        if (accountStatus.containsKey(accountNumber)) {
            accountStatus.put(accountNumber, false);
            return true;
        }
        return false;
    }

    public boolean unfreezeAccount(int accountNumber) {
        if (accountStatus.containsKey(accountNumber)) {
            accountStatus.put(accountNumber, true);
            return true;
        }
        return false;
    }

    public boolean isAccountActive(int accountNumber) {
        if (accountStatus.containsKey(accountNumber)) {
            return accountStatus.get(accountNumber);
        }
        return false; // Account not found
    }

    public int getAccountCount() {
        return userAccounts.size();
    }

    public int getActiveAccountCount() {
        int count = 0;
        for (Boolean status : accountStatus.values()) {
            if (status) {
                count++;
            }
        }
        return count;
    }

    public int getFrozenAccountCount() {
        return accountStatus.size() - getActiveAccountCount();
    }

    public Collection<AccountHolder> getAccountHolders() {
        return userAccounts.values();
    }
    
    public HashMap<Integer, BankAccount> getBankAccounts(){
        return bankAccounts;
    }
}