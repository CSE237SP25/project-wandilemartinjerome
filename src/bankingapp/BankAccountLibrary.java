package bankingapp;
import java.util.HashMap;
public class BankAccountLibrary {
	
	private HashMap<BankAccount, Integer> BankAccountLibrary = new HashMap<>();
	
	public int BankAccountNumber(BankAccount info) {
		int hashcode = info.hashCode();
		return hashcode;
	}
	
	public void AddAcount(BankAccount info) {
		int hash = BankAccountNumber(info);
		if(!BankAccountLibrary.containsKey(hash)) {
			BankAccountLibrary.put(info,hash);
		}
	}
	
	public boolean findAcCount( int hash) {
		return BankAccountLibrary.containsKey(hash);
	}
	
	public void deletAccount(BankAccount info, int hash) {
		if(BankAccountLibrary.containsKey(hash)) {
			BankAccountLibrary.put(info,hash);
		}
	}
}
