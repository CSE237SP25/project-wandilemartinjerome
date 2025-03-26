package bankingapp;
import java.util.ArrayList;
import java.util.HashMap;
public class AccountHolder {
	private final String first_name;
	
	private final String lastname;
	
	private final String birthday;
	
	private final int ssn;
	
	private final int BankCode;
	
	private ArrayList<Integer> BankAcounts;

	public AccountHolder(){
		this.first_name = null;
		this.lastname = null;
		this.birthday = null;
		this.ssn = 0 ;
		this.BankAcounts = new ArrayList<Integer>();
		this.BankCode = 0;
	}
	
	/**
     * Creates a new admin account with the specified details
     * 
     * @param firstName First name of the admin
     * @param lastName Last name of the admin
     * @param birthday Birthday of the admin
     * @param ssn Social Security Number of the admin
     * @param adminId Unique administrator ID
     * @param securityClearance Security clearance level
     */
    public AccountHolder(String firstName, String lastName, String birthday, int ssn, 
                        int BankCode) {
        super();
        this.first_name = firstName;
		this.lastname = lastName;
		this.birthday = birthday;
		this.ssn = ssn ;
		this.BankAcounts = new ArrayList<Integer>();
		this.BankCode = BankCode;
    }
	
	public void addBankacount(AccountHolder info, int BankAccountCode  ) {
		if(!info.BankAcounts.contains(BankAccountCode)) {
			info.BankAcounts.add(BankAccountCode);
		}
	}
	
	public void removeBankacount(AccountHolder info, int BankAccountCode  ) {
		if(info.BankAcounts.contains(BankAccountCode)) {
			info.BankAcounts.remove(Integer.valueOf(BankAccountCode));
		}
	}
	
	public void listBankAcounts(AccountHolder info){
		int i =1;
		for(Integer list: info.BankAcounts) {
			System.out.printf("Saving Account %d: %d%n", i, list);
			i++;
		}
	}
	
	public boolean findBankAccount(AccountHolder info, int BankAccountCode) {
		return info.BankAcounts.contains(BankAccountCode);
	}
	
	public int getBankAccountSize() {
		return BankAcounts.size();
	}
	
	public String getFirstName() {
		return first_name;
	}
	
	public String getLastName() {
		return lastname;
	}
	
	public String getBirthday() {
		return birthday;
	}
	
	public int getSsn() {
		return ssn;
	}
	
	public int getBankCode() {
		return BankCode;
	}
	

}
