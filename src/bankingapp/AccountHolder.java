package bankingapp;
import java.util.ArrayList;
import java.util.HashMap;
public class AccountHolder {
	String lastname;
	
	String birthday;
	
	int ssn;
	
    int BankCode;
	
	private ArrayList<Integer> BankAccounts;

	public AccountHolder(){
		this.lastname = null;
		this.birthday = null;
		this.ssn = 0 ;
		this.BankAccounts = new ArrayList<Integer>();
		this.BankCode = 0;
	}
	
	public void addBankAccount(AccountHolder info, int BankAccountCode  ) {
		if(!info.BankAccounts.contains(BankAccountCode)) {
			info.BankAccounts.add(BankAccountCode);
		}
	}
	
	public void removeBankAccount(AccountHolder info, int BankAccountCode  ) {
		if(info.BankAccounts.contains(BankAccountCode)) {
			info.BankAccounts.remove(BankAccountCode);
		}
	}
	
	public void listBankAccounts(AccountHolder info){
		int i =1;
		for(Integer list: info.BankAccounts) {
			System.out.printf("Saving Account" + i, list);
			i++;
		}
	}
	
	public boolean findBankAccount(AccountHolder info, int BankAccountCode) {
		return info.BankAccounts.contains(BankAccountCode);
	}
	
	public String getLastname() {
		return this.lastname;
	}

}
