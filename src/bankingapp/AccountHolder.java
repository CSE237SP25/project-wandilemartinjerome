package bankingapp;
import java.util.ArrayList;
import java.util.HashMap;
public class AccountHolder {
	private String lastname;
	
	private String birthday;
	
	private int ssn;
	
	private int BankCode;
	
	private ArrayList<Integer> BankAccounts;
	
	private String password; // Password to protect personal information
	
	private boolean isPersonalInfoHidden; // Flag to track if personal info is hidden

	public AccountHolder(){
		this.lastname = null;
		this.birthday = null;
		this.ssn = 0;
		this.BankAccounts = new ArrayList<Integer>();
		this.BankCode = 0;
		this.password = null;
		this.isPersonalInfoHidden = false;
	}
	
	/**
	 * Creates a new account holder with personal information
	 * 
	 * @param lastname The account holder's last name
	 * @param birthday The account holder's birthday
	 * @param ssn The account holder's social security number
	 * @param bankCode The bank code
	 */
	public AccountHolder(String lastname, String birthday, int ssn, int bankCode) {
		this.lastname = lastname;
		this.birthday = birthday;
		this.ssn = ssn;
		this.BankCode = bankCode;
		this.BankAccounts = new ArrayList<Integer>();
		this.password = null;
		this.isPersonalInfoHidden = false;
	}
	
	/**
	 * Sets the account holder's personal information
	 * 
	 * @param lastname The account holder's last name
	 * @param birthday The account holder's birthday
	 * @param ssn The account holder's social security number
	 * @param bankCode The bank code
	 */
	public void setPersonalInfo(String lastname, String birthday, int ssn, int bankCode) {
		this.lastname = lastname;
		this.birthday = birthday;
		this.ssn = ssn;
		this.BankCode = bankCode;
	}
	
	/**
	 * Sets a password to protect personal information
	 * 
	 * @param password The password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Hides personal information behind password protection
	 */
	public void hidePersonalInfo() {
		this.isPersonalInfoHidden = true;
	}
	
	/**
	 * Shows personal information if correct password is provided
	 * 
	 * @param inputPassword The password to check
	 * @return true if password is correct and personal info is now visible, false otherwise
	 */
	public boolean showPersonalInfo(String inputPassword) {
		if (this.password != null && this.password.equals(inputPassword)) {
			this.isPersonalInfoHidden = false;
			return true;
		}
		this.isPersonalInfoHidden = true; // Ensure info stays hidden with wrong password
		return false;
	}
	
	/**
	 * Gets personal information if not hidden or if correct password is provided
	 * 
	 * @param inputPassword The password to check
	 * @return A string containing personal information, or null if hidden and wrong password
	 */
	public String getPersonalInfo(String inputPassword) {
		// Always require password if personal info is hidden
		if (isPersonalInfoHidden) {
			if (this.password != null && this.password.equals(inputPassword)) {
				return "Last Name: " + lastname + "\n" +
					   "Birthday: " + birthday + "\n" +
					   "SSN: " + ssn + "\n" +
					   "Bank Code: " + BankCode;
			}
			return null;
		}
		
		// If not hidden, still require password if one is set
		if (this.password != null) {
			if (this.password.equals(inputPassword)) {
				return "Last Name: " + lastname + "\n" +
					   "Birthday: " + birthday + "\n" +
					   "SSN: " + ssn + "\n" +
					   "Bank Code: " + BankCode;
			}
			return null;
		}
		
		// Only return info without password if no password is set and info is not hidden
		return "Last Name: " + lastname + "\n" +
			   "Birthday: " + birthday + "\n" +
			   "SSN: " + ssn + "\n" +
			   "Bank Code: " + BankCode;
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
	

}
