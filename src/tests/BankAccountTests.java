package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import bankingapp.BankAccount;

public class BankAccountTests {
    
    @Test
    public void testNewAccountHasZeroBalance() {
        BankAccount account = new BankAccount();
        assertEquals(0.0, account.getCurrentBalance(), 0.001);
    }
    
    @Test
    public void testAccountWithInitialBalance() {
        BankAccount account = new BankAccount();
        assertEquals(100.0, accountWithInitialBalance.getCurrentBalance(), 0.001);
    }
    
    @Test
    public void testDeposit() {
        BankAccount account = new BankAccount();
        account.deposit(50.0);
        assertEquals(50.0, account.getCurrentBalance(), 0.001);
        
        account.deposit(25.0);
        assertEquals(75.0, account.getCurrentBalance(), 0.001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDeposit() {
        BankAccount account = new BankAccount();
        account.deposit(-50.0);
    }
    
    @Test
    public void testWithdraw() {
        BankAccount account = new BankAccount(100);
        account.withdraw(50.0);
        assertEquals(50.0, account.getCurrentBalance(), 0.001);
        
        BankAccount account.withdraw(25.0);
        assertEquals(25.0, account.getCurrentBalance(), 0.001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeWithdraw() {
        BankAccount account = new BankAccount(100);
        account.withdraw(-50.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInsufficientFunds() {
        BankAccount account = new BankAccount(100);
        account.withdraw(150.0);
    }

	// Tests for deposit functionality
	@Test
	public void testSimpleDeposit() {
    BankAccount account = new BankAccount(100);
		account.deposit(25);
		assertEquals(account.getCurrentBalance(), 25.0, 0.005);
	}
	
	@Test
	public void testNegativeDeposit() {
		try {
      BankAccount account = new BankAccount(100);
			account.deposit(-25);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(e != null);
		}
	}

	// Tests for balance functionality
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

	// Tests for withdrawal functionality
	@Test
	public void testSuccessfulWithdrawal() {
		BankAccount account = new BankAccount(100.0);
		boolean result = account.withdraw(50.0);
		assertTrue(result);
		assertEquals(50.0, account.getCurrentBalance(), 0.005);
	}

	@Test
	public void testInsufficientFundsWithdrawal() {
		BankAccount account = new BankAccount(25.0);
		boolean result = account.withdraw(50.0);
		assertFalse(result);
		assertEquals(25.0, account.getCurrentBalance(), 0.005);
	}

	@Test
	public void testNegativeWithdrawal() {
		BankAccount account = new BankAccount(100.0);
		try {
			account.withdraw(-25.0);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals(100.0, account.getCurrentBalance(), 0.005);
		}
	}

	// Tests for transfer functionality
	@Test
	public void testSuccessfulTransfer() {
		BankAccount sourceAccount = new BankAccount(100.0);
		BankAccount destinationAccount = new BankAccount(50.0);
		boolean result = sourceAccount.transfer(destinationAccount, 25.0);
		assertTrue(result);
		assertEquals(75.0, sourceAccount.getCurrentBalance(), 0.005);
		assertEquals(75.0, destinationAccount.getCurrentBalance(), 0.005);
	}

	@Test
	public void testInsufficientFundsTransfer() {
		BankAccount sourceAccount = new BankAccount(20.0);
		BankAccount destinationAccount = new BankAccount(50.0);
		boolean result = sourceAccount.transfer(destinationAccount, 25.0);
		assertFalse(result);
		assertEquals(20.0, sourceAccount.getCurrentBalance(), 0.005);
		assertEquals(50.0, destinationAccount.getCurrentBalance(), 0.005);
	}

	@Test
	public void testNegativeTransfer() {
		BankAccount sourceAccount = new BankAccount(100.0);
		BankAccount destinationAccount = new BankAccount(50.0);
		try {
			sourceAccount.transfer(destinationAccount, -25.0);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals(100.0, sourceAccount.getCurrentBalance(), 0.005);
			assertEquals(50.0, destinationAccount.getCurrentBalance(), 0.005);
		}
	}

	@Test
	public void testNullDestinationTransfer() {
		BankAccount sourceAccount = new BankAccount(100.0);
		try {
			sourceAccount.transfer(null, 25.0);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals(100.0, sourceAccount.getCurrentBalance(), 0.005);
		}
	}

	// Tests for maximum withdrawal and deposit limits functionality

	@Test
	public void testDefaultWithdrawalLimit() {
		BankAccount account = new BankAccount(2000.0);
		assertEquals(1000.0, account.getMaxWithdrawalLimit(), 0.005);
	}

	@Test
	public void testDefaultDepositLimit() {
		BankAccount account = new BankAccount();
		assertEquals(10000.0, account.getMaxDepositLimit(), 0.005);
	}

	@Test
	public void testCustomLimitsConstructor() {
		BankAccount account = new BankAccount(500.0, 2000.0, 15000.0);
		assertEquals(2000.0, account.getMaxWithdrawalLimit(), 0.005);
		assertEquals(15000.0, account.getMaxDepositLimit(), 0.005);
	}

	@Test
	public void testSetWithdrawalLimit() {
		BankAccount account = new BankAccount();
		account.setMaxWithdrawalLimit(2500.0);
		assertEquals(2500.0, account.getMaxWithdrawalLimit(), 0.005);
	}

	@Test
	public void testSetDepositLimit() {
		BankAccount account = new BankAccount();
		account.setMaxDepositLimit(20000.0);
		assertEquals(20000.0, account.getMaxDepositLimit(), 0.005);
	}

	@Test 
	public void testNegativeWithdrawalLimit() {
		BankAccount account = new BankAccount();
		try {
			account.setMaxWithdrawalLimit(-500.0);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals(1000.0, account.getMaxWithdrawalLimit(), 0.005);
		}
	}

	@Test
	public void testNegativeDepositLimit() {
		BankAccount account = new BankAccount();
		try {
			account.setMaxDepositLimit(-1000.0);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals(10000.0, account.getMaxDepositLimit(), 0.005);
		}
	}

	@Test
	public void testExceedWithdrawalLimit() {
		BankAccount account = new BankAccount(2000.0);
		try {
			account.withdraw(1500.0);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals(2000.0, account.getCurrentBalance(), 0.005);
		}
	}

	@Test
	public void testExceedDepositLimit() {
		BankAccount account = new BankAccount(1000.0);
		try {
			account.deposit(15000.0);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals(1000.0, account.getCurrentBalance(), 0.005);
		}
	}

	@Test
	public void testTransferExceedingWithdrawalLimit() {
		BankAccount sourceAccount = new BankAccount(2000.0);
		BankAccount destinationAccount = new BankAccount(500.0);
		try {
			sourceAccount.transfer(destinationAccount, 1500.0);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals(2000.0, sourceAccount.getCurrentBalance(), 0.005);
			assertEquals(500.0, destinationAccount.getCurrentBalance(), 0.005);
		}
	}
}

