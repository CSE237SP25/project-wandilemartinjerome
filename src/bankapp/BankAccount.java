package bankapp;

public class BankAccount {

	private double balance;
	
	public BankAccount() {
		this.balance = 0;
	}
	
	// If initial balance is specified
	public BankAccount(double initBalance) {
		this.balance = initBalance;
	}
	
	public double getCurrentBalance() {
		return this.balance;
	}
  
	public void deposit(double amount) {
		if(amount < 0) {
			throw new IllegalArgumentException();
		}
		this.balance += amount;
	}
	
	/**
	 * Withdraws the specified amount from the account if possible
	 * 
	 * @param amount The amount of money to withdraw
	 * @return true if the withdrawal was successful, false otherwise
	 * @throws IllegalArgumentException if the withdrawal amount is negative
	 */
	public boolean withdraw(double amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("Withdrawal amount cannot be negative");
		}
		if (amount > this.balance) {
			return false; // Insufficient funds
		}
		this.balance -= amount;
		return true;
	}
}
