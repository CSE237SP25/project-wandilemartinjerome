package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import bankingapp.BankAccount;

public class BankAccountTests {

	@Test
	public void testViewEmptyBalance() {
		BankAccount account = new BankAccount();
		
		assertEquals(account.getCurrentBalance(), 0, 0.005);
	}
	
	@Test
	public void testViewSomeBalance() {
		BankAccount account = new BankAccount(25);
		
		assertEquals(account.getCurrentBalance(), 25, 0.005);
	}
}
