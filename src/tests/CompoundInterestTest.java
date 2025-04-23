package tests;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import bankingapp.*;

public class CompoundInterestTest  {

    @Test
    public void testCompoundInterestAppliedToSavings() throws InterruptedException {
        CompoundInterest.resetTestInterestFlag();
        // Create the database and account
        BankAccountDatabase db = new BankAccountDatabase();
        BankAccount account = new BankAccount(1000.0, AccountType.SAVINGS);
        int accNumber = db.generateBankAccountNumber(account);
        db.addBankAccount(account);

        // Start compound interest thread with shorter interval (simulate 2 sec)
        CompoundInterest interestTask = new CompoundInterest(db, 2000); // Pass 2s for fast test
        Thread thread = new Thread(interestTask);
        thread.start();

        // Wait slightly longer than the interest interval
        Thread.sleep(3000);

        // Stop the thread safely
        thread.interrupt();
        thread.join(); // Wait for it to stop

        // Check balance (20% of 1000 = 200, new balance = 1200)
        double expected = 1200.0;
        double actual = db.getBankAccount(accNumber).getCurrentBalance();
 
        assertEquals(expected, actual, 0.001);
    }
}