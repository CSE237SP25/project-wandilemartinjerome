package bankingapp;
import java.util.ArrayList;
public class AccountHolder {
	private final String lastname;
	
	private final int birthday;
	
	private final int ssn;
	
	private final int BankCode;
	
	private ArrayList<Integer> BankAccounts;

	public AccountHolder(){
		this.lastname = null;
		this.birthday = 0;
		this.ssn = 0 ;
		this.BankAccounts = new ArrayList<Integer>();
		this.BankCode = 0;
	}
	
	public void addBankacount(AccountHolder info, int BankAccountCode  ) {
		if(!info.BankAccounts.contains(BankAccountCode)) {
			info.BankAccounts.add(BankCode);
		}
	}
	
	public void removeBankacount(AccountHolder info, int BankAccountCode  ) {
		if(info.BankAccounts.contains(BankAccountCode)) {
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
	
	public boolean findBankAccount(AccountHolder info, int BankAccountCode) {
		return info.BankAccounts.contains(BankAccountCode);
	}
	

}
