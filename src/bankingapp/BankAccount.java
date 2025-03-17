package bankingapp;

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
}
