package bankapp;

import java.util.ArrayList;

public class Acountholder{
	private final String first_name;
	
	private final String lastname;
	
	private final int birthday;
	
	private final int ssn;
	
	private final int BankCode;
	
	private ArrayList<Integer> BankAcounts;

	public Acountholder(){
		this.first_name = null;
		this.lastname = null;
		this.birthday = 0;
		this.ssn = 0 ;
		this.BankAcounts = new ArrayList<Integer>();
		this.BankCode = 0;
	}
	
	public void addBankacount(Acountholder info, int BankAcountCode  ) {
		if(!info.BankAcounts.contains(BankAcountCode)) {
			info.BankAcounts.add(BankCode);
		}
	}
	
	public void removeBankacount(Acountholder info, int BankAcountCode  ) {
		if(info.BankAcounts.contains(BankAcountCode)) {
			info.BankAcounts.remove(BankCode);
		}
	}
	
	public void listBankAcounts(Acountholder info){
		int i =1;
		for(Integer list: info.BankAcounts) {
			System.out.printf("Saving Account" + i, list);
			i++;
		}
	}
	
	public boolean findBankAccount(Acountholder info, int BankAcountCode) {
		return info.BankAcounts.contains(BankAcountCode);
	}
	
	
}