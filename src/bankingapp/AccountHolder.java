package bankingapp;
import java.util.ArrayList;
import java.util.HashMap;
public class AccountHolder {
	private final String lastname;
	
	private final String birthday;
	
	private final int ssn;
	
	private final int BankCode;
	
	private ArrayList<Integer> BankAccounts;

	public AccountHolder(){
		this.lastname = null;
		this.birthday = null;
		this.ssn = 0 ;
		this.BankAccounts = new ArrayList<Integer>();
		this.BankCode = 0;
	}
	
	public void addBankacount(AccountHolder info, int BankAcountCode  ) {
		if(!info.BankAccounts.contains(BankAcountCode)) {
			info.BankAccounts.add(BankCode);
		}
	}
	
	public void removeBankacount(AccountHolder info, int BankAcountCode  ) {
		if(info.BankAccounts.contains(BankAcountCode)) {
			info.BankAccounts.remove(BankCode);
		}
	}
	
	public void listBankAccounts(AccountHolder info){
		int i =1;
		for(Integer list: info.BankAccounts) {
			System.out.printf("Saving Account" + i, list);
			i++;
		}
	}
	
	public boolean findBankAccount(AccountHolder info, int BankAcountCode) {
		return info.BankAccounts.contains(BankAcountCode);
	}
	

}
