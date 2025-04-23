package bankingapp;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Represents an account holder with personal information.
 * 
 * @author Martin Rivera
 * @author Wandile Hannah
 * @author Jerome Hsing
 */
public class AccountHolder {

	private String lastname;
	
	private String birthday;
	
	private int ssn;
	
	private int bankCode;
	
	private ArrayList<Integer> bankAccounts;
	
	private String password; // Password to protect personal information
	
	private boolean isPersonalInfoHidden; // Flag to track if personal info is hidden

	public AccountHolder(){
		this.lastname = null;
		this.birthday = null;
		this.ssn = 0;
		this.bankAccounts = new ArrayList<Integer>();
		this.bankCode = 0;
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
		this.bankCode = bankCode;
		this.bankAccounts = new ArrayList<Integer>();
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
		this.bankCode = bankCode;
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
		// If no password is set, always show the info
		if (this.password == null) {
			this.isPersonalInfoHidden = false;
			return true;
		}
		
		// If password matches, show the info
		if (this.password.equals(inputPassword)) {
			this.isPersonalInfoHidden = false;
			return true;
		}
		
		// If password doesn't match, keep the current hidden state
		return false;
	}
	
	/**
	 * Gets personal information if not hidden or if correct password is provided
	 * 
	 * @param inputPassword The password to check
	 * @return A string containing personal information, or null if hidden and wrong password
	 */
	public String getPersonalInfo(String inputPassword) {
		// If personal info is hidden, always require password
		if (isPersonalInfoHidden) {
			// If no password is set, don't allow access
			if (password == null) {
				return null;
			}
			// Only allow access if password matches
			if (!password.equals(inputPassword)) {
				return null;
			}
		}
		
		// If password is set, require it even if info is not hidden
		if (password != null && !password.equals(inputPassword)) {
			return null;
		}
		
		return formatPersonalInfo();
	}
	
	/**
	 * Checks if the provided password is valid
	 * 
	 * @param inputPassword The password to check
	 * @return true if password is valid or not required, false otherwise
	 */
	private boolean isPasswordValid(String inputPassword) {
		// If personal info is hidden, always require password
		if (isPersonalInfoHidden) {
			// If no password is set, don't allow access
			if (password == null) {
				return false;
			}
			// Only allow access if password matches
			return password.equals(inputPassword);
		}
		
		// If password is set, require it even if info is not hidden
		if (password != null) {
			return password.equals(inputPassword);
		}
		
		// No password required if info is not hidden and no password is set
		return true;
	}
	
	/**
	 * Formats the personal information into a readable string
	 * 
	 * @return Formatted string containing personal information
	 */
	private String formatPersonalInfo() {
		StringBuilder info = new StringBuilder();
		info.append("Last Name: ").append(lastname).append("\n");
		info.append("Birthday: ").append(birthday).append("\n");
		info.append("SSN: ").append(ssn).append("\n");
		info.append("Bank Code: ").append(bankCode);
		return info.toString();
	}
	
	public void addBankAccount(AccountHolder info, int bankAccountNumber) {
		if(!info.bankAccounts.contains(bankAccountNumber)) {
			info.bankAccounts.add(bankAccountNumber);
		}
	}
	
	public void removeBankAccount(AccountHolder info, int bankAccountNumber) {
		int index = info.bankAccounts.indexOf(bankAccountNumber);
		if(index != -1) {
			info.bankAccounts.remove(index);
		}
	}
	
	public void listBankAccounts(AccountHolder info){
		int i = 1;
		for(Integer accountNumber : info.bankAccounts) {
			System.out.printf("Saving Account %d: %d%n", i, accountNumber);
			i++;
		}
	}
	
	public boolean findBankAccount(AccountHolder info, int bankAccountNumber) {
		return info.bankAccounts.contains(bankAccountNumber);
	}
	
	public String getLastname() {
		return this.lastname;
	}
	

}
