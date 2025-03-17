package bankapp;

public class BankAccount {

	private double balance;
	
	public BankAccount() {
		this.balance = 0;
	}
	
	public double getCurrentBalance() {
		return this.balance;
	}
}