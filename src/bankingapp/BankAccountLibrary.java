package bankingapp;
import java.util.HashMap;

/**
 * Manages a library of bank accounts.
 * 
 * @author Martin Rivera
 * @author Wandile Hannah
 * @author Jerome Hsing
 */
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
	
	public double getAccountBalance(int hash) {
		if (BankAccountLibrary.containsKey(hash)) {
			return BankAccountLibrary.get(hash).getCurrentBalance();
		}
		return -1; // Return -1 if account not found
	}
}
