package bankingapp;

import java.util.HashMap;

public class AllUserAccount {
	private HashMap<AccountHolder, Integer> Useraccounts = new HashMap<>();
	
	public int AccountNumber(AccountHolder info) {
		int hashcode = info.hashCode();
		return hashcode;
	}
	
	public void AddAcount(AccountHolder info) {
		int hash = AccountNumber(info);
		if(!Useraccounts.containsKey(hash)) {
			Useraccounts.put(info,hash);
		}
	}
	
	public boolean findAcount( int hash) {
		return Useraccounts.containsKey(hash);
	}
	
	public void deletAccount(AccountHolder info, int hash) {
		if(Useraccounts.containsKey(hash)) {
			Useraccounts.put(info,hash);
		}
	}
}
