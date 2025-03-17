package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;


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

	@Test
	public void testSimpleDeposit() {
		//1. Create objects to be tested
		BankAccount account = new BankAccount();
		
		//2. Call the method being tested
		account.deposit(25);
		
		//3. Use assertions to verify results
		assertEquals(account.getCurrentBalance(), 25.0, 0.005);
	}
	
	@Test
	public void testNegativeDeposit() {
		//1. Create object to be tested
		BankAccount account = new BankAccount();

		try {
			account.deposit(-25);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(e != null);
		}
	}
	
	@Test
	public void testNegativeWithdraw() {
		BankAccount account = new BankAccount();

		try {
			account.withdraw(-25);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(e != null);
		}
	}
	
	@Test
	public void testSimpleWithdraw() {
		//1. Create objects to be tested
		BankAccount account = new BankAccount(30);
		
		//2. Call the method being tested
		account.withdraw(25);
		
		//3. Use assertions to verify results
		assertEquals(account.getCurrentBalance(), 5.0, 0.005);
	}
	
	@Test
	public void testOverdraft() {
		//1. Create objects to be tested
		BankAccount account = new BankAccount(20);
		
		try {
			account.withdraw(25);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(e != null);
		}
	}
}
