package bankingapp;

import java.util.HashMap;

public class AllUserAccount {
	private HashMap<AccountHolder, Integer> UserAccounts = new HashMap<>();
	private HashMap<Integer, Boolean> accountStatus = new HashMap<>(); // Track account status (active/frozen)
	
	public int AccountNumber(AccountHolder info) {
		int hashcode = info.hashCode();
		return hashcode;
	}
	
	public void AddAcount(AccountHolder info) {
		int hash = AccountNumber(info);
		if(!UserAccounts.containsKey(hash)) {
			UserAccounts.put(info, hash);
			accountStatus.put(hash, true); // Set account as active by default
		}
	}
	
	public boolean findAcount(int hash) {
		return UserAccounts.containsKey(hash);
	}
	
	public void deletAccount(AccountHolder info, int hash) {
		if(UserAccounts.containsKey(hash)) {
			UserAccounts.remove(info);
			accountStatus.remove(hash);
		}
	}
	
	/**
	 * Freezes an account by setting its status to inactive
	 * 
	 * @param hash The hash code of the account to freeze
	 * @return true if the account was found and frozen, false otherwise
	 */
	public boolean freezeAccount(int hash) {
		if (accountStatus.containsKey(hash)) {
			accountStatus.put(hash, false);
			return true;
		}
		return false;
	}
	
	/**
	 * Unfreezes an account by setting its status to active
	 * 
	 * @param hash The hash code of the account to unfreeze
	 * @return true if the account was found and unfrozen, false otherwise
	 */
	public boolean unfreezeAccount(int hash) {
		if (accountStatus.containsKey(hash)) {
			accountStatus.put(hash, true);
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if an account is active (not frozen)
	 * 
	 * @param hash The hash code of the account to check
	 * @return true if the account is active, false if it's frozen or not found
	 */
	public boolean isAccountActive(int hash) {
		if (accountStatus.containsKey(hash)) {
			return accountStatus.get(hash);
		}
		return false; // Account not found
	}
	
	/**
	 * Gets the number of accounts in the system
	 * 
	 * @return The number of accounts
	 */
	public int getAccountCount() {
		return UserAccounts.size();
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
}