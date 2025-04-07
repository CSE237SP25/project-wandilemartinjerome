package tests;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertEquals;
import bankingapp.BankAccount;
import bankingapp.Transaction;
import bankingapp.TransactionType;

public class TransactionHistoryTest {
    @Test
    public void testTransactionHistory() {
        BankAccount account = new BankAccount(100);
        account.deposit(50);
        account.withdraw(25);
    
        List<Transaction> history = account.getTransactionHistory();
        assertEquals(3, history.size()); // Initial deposit + deposit + withdrawal
        
        // Check the first transaction (initial deposit)
        Transaction initialDeposit = history.get(0);
        assertEquals(TransactionType.DEPOSIT, initialDeposit.getType());
        assertEquals(100, initialDeposit.getAmount(), 0.001);
        assertEquals("Initial deposit", initialDeposit.getDescription());

        // Check the second transaction (deposit)
        Transaction deposit = history.get(1);
        assertEquals(TransactionType.DEPOSIT, deposit.getType());
        assertEquals(50, deposit.getAmount(), 0.001);

        // Check the third transaction (withdrawal)
        Transaction withdrawal = history.get(2);
        assertEquals(TransactionType.WITHDRAWAL, withdrawal.getType());
        assertEquals(25, withdrawal.getAmount(), 0.001);
    }
}