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
		if(!bankAccounts.containsKey(hash)) {
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

	public void addAccountHolder(AccountHolder holder) {
		int hash = generateAccountNumber(holder);
		if(!userAccounts.containsKey(hash)) {
			userAccounts.put(hash, holder);
			accountStatus.put(hash, true); // Set account as active by default
		}
	}
	
	public boolean hasAccountHolder(int accountNumber) {
		return userAccounts.containsKey(accountNumber);
	}
	
	public void removeAccountHolder(int accountNumber) {
		if(userAccounts.containsKey(accountNumber)) {
			userAccounts.remove(accountNumber);
			accountStatus.remove(accountNumber);
		}
	}
	
	/**
	 * Freezes an account by setting its status to inactive
	 * 
	 * @param accountNumber The account number to freeze
	 * @return true if the account was found and frozen, false otherwise
	 */
	public boolean freezeAccount(int accountNumber) {
		if (accountStatus.containsKey(accountNumber)) {
			accountStatus.put(accountNumber, false);
			return true;
		}
		return false;
	}
	
	/**
	 * Unfreezes an account by setting its status to active
	 * 
	 * @param accountNumber The account number to unfreeze
	 * @return true if the account was found and unfrozen, false otherwise
	 */
	public boolean unfreezeAccount(int accountNumber) {
		if (accountStatus.containsKey(accountNumber)) {
			accountStatus.put(accountNumber, true);
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if an account is active (not frozen)
	 * 
	 * @param accountNumber The account number to check
	 * @return true if the account is active, false if it's frozen or not found
	 */
	public boolean isAccountActive(int accountNumber) {
		if (accountStatus.containsKey(accountNumber)) {
			return accountStatus.get(accountNumber);
		}
		return false; // Account not found
	}
	
	/**
	 * Gets the number of accounts in the system
	 * 
	 * @return The number of accounts
	 */
	public int getAccountCount() {
		return userAccounts.size();
	}
	
	/**
	 * Gets the number of active accounts in the system
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
	 * Gets the number of frozen accounts in the system
	 * 
	 * @return The number of frozen accounts
	 */
	public int getFrozenAccountCount() {
		return accountStatus.size() - getActiveAccountCount();
	}
	
	/**
	 * Gets all account holders in the system
	 * 
	 * @return A collection of all account holders
	 */
	public Collection<AccountHolder> getAccountHolders() {
		return userAccounts.values();
	}
}