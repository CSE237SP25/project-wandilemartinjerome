package bankingapp;
import java.util.ArrayList;
public class AccountHolder {
	private final String first_name;
	
	private final String lastname;
	
	private final int birthday;
	
	private final int ssn;
	
	private final int BankCode;
	
	private ArrayList<Integer> BankAcounts;

	public AccountHolder(){
		this.first_name = null;
		this.lastname = null;
		this.birthday = 0;
		this.ssn = 0 ;
		this.BankAcounts = new ArrayList<Integer>();
		this.BankCode = 0;
	}
	
	public void addBankacount(AccountHolder info, int BankAccountCode  ) {
		if(!info.BankAcounts.contains(BankAccountCode)) {
			info.BankAcounts.add(BankAccountCode);
		}
	}
	
	public void removeBankacount(AccountHolder info, int BankAccountCode  ) {
		if(info.BankAcounts.contains(BankAccountCode)) {
			info.BankAcounts.remove(BankAccountCode);
		}
	}
	
	public void listBankAcounts(AccountHolder info){
		int i =1;
		for(Integer list: info.BankAcounts) {
			System.out.printf("Saving Account" + i, list);
			i++;
		}
	}
	
	public boolean findBankAccount(AccountHolder info, int BankAccountCode) {
		return info.BankAcounts.contains(BankAccountCode);
	}
	

}
