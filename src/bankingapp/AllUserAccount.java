package bankingapp;

import java.util.HashMap;

public class AllUserAccount {
	private HashMap<AccountHolder, Integer> UserAccounts = new HashMap<>();
	
	public int AccountNumber(AccountHolder info) {
		int hashcode = info.hashCode();
		return hashcode;
	}
	
	public void AddAcount(AccountHolder info) {
		int hash = AccountNumber(info);
		if(!UserAccounts.containsKey(hash)) {
			UserAccounts.put(info,hash);
		}
	}
	
	public boolean findAcount( int hash) {
		return UserAccounts.containsKey(hash);
	}
	
	public void deletAccount(AccountHolder info, int hash) {
		if(UserAccounts.containsKey(hash)) {
			UserAccounts.put(info,hash);
		}
	}
}
