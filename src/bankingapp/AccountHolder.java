package bankingapp;
import java.util.ArrayList;
public class AccountHolder {
	private final String lastname;
	
	private final int birthday;
	
	private final int ssn;
	
	private final int BankCode;
	
	private ArrayList<Integer> BankAcounts;

	public AccountHolder(){
		this.lastname = null;
		this.birthday = 0;
		this.ssn = 0 ;
		this.BankAcounts = new ArrayList<Integer>();
		this.BankCode = 0;
	}
	
	public void addBankacount(AccountHolder info, int BankAcountCode  ) {
		if(!info.BankAcounts.contains(BankAcountCode)) {
			info.BankAcounts.add(BankCode);
		}
	}
	
	public void removeBankacount(AccountHolder info, int BankAcountCode  ) {
		if(info.BankAcounts.contains(BankAcountCode)) {
			info.BankAcounts.remove(BankCode);
		}
	}
	
	public void listBankAcounts(AccountHolder info){
		int i =1;
		for(Integer list: info.BankAcounts) {
			System.out.printf("Saving Account" + i, list);
			i++;
		}
	}
	
	public boolean findBankAccount(AccountHolder info, int BankAcountCode) {
		return info.BankAcounts.contains(BankAcountCode);
	}
	

}
