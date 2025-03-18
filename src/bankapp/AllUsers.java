package bankapp;

import java.util.HashMap;

public class AllUsers{
	private HashMap<Acountholder, Integer> Useraccounts = new HashMap<>();
	
	public int AccountNumber(Acountholder info) {
		int hashcode = info.hashCode(); 
		return hashcode;
	}
	
	public void AddAcount(Acountholder info) {
		int hash = AccountNumber(info);
		if(!Useraccounts.containsKey(hash)) {
			Useraccounts.put(info,hash);
		}
	}
	
	public boolean findAcount( int hash) {
		return Useraccounts.containsKey(hash);
	}
	
	public void deletAccount(Acountholder info, int hash) {
		if(Useraccounts.containsKey(hash)) {
			Useraccounts.put(info,hash);
		}
	}
}