package bankingapp;
import java.util.HashMap;
public class BankAccountLibrary {
	
	private HashMap<Integer, BankAccount> BankAccountLibrary = new HashMap<>();
	
	public int BankAccountNumber(BankAccount info) {
		int hashcode = info.hashCode();
		return hashcode;
	}
	
	public void addAccount(BankAccount info) {
		int hash = BankAccountNumber(info);
		if(!BankAccountLibrary.containsKey(hash)) {
			BankAccountLibrary.put(hash, info);
		}
	}
	
	public boolean findAccount(int hash) {
		return BankAccountLibrary.containsKey(hash);
	}
	
	public void deleteAccount(BankAccount info, int hash) {
		if(BankAccountLibrary.containsKey(hash)) {
			BankAccountLibrary.remove(hash);
		}
	}
	
	public BankAccount returnBankaccount(int hash) {
		if(BankAccountLibrary.containsKey(hash)) {
			return BankAccountLibrary.get(hash);
		};
		return null;
	}
}
