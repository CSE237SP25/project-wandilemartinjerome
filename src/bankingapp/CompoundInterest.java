package bankingapp;

import java.util.Map;

/**
 * Applies compound interest to savings accounts at regular intervals.
 */
public class CompoundInterest implements Runnable {
    private final BankAccountDatabase bankAccounts;
    private final long intervalMillis;
    private static final double INTEREST_RATE = 0.20; // 20% interest rate
    private static boolean interestAppliedForTest = false;

    public CompoundInterest(BankAccountDatabase bankAccounts, long intervalMillis) {
        this.bankAccounts = bankAccounts;
        this.intervalMillis = intervalMillis;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (System.getProperty("test.mode") != null && !interestAppliedForTest) {
                    applyInterestToSavingsAccounts();
                    interestAppliedForTest = true;
                    break; // Exit after first application in test mode
                } else if (System.getProperty("test.mode") == null) {
                    applyInterestToSavingsAccounts();
                }
                Thread.sleep(intervalMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Applies compound interest to all active savings accounts.
     */
    private void applyInterestToSavingsAccounts() {
        Map<Integer, BankAccount> accounts = bankAccounts.getBankAccounts();
        for (Map.Entry<Integer, BankAccount> entry : accounts.entrySet()) {
            Integer accountNumber = entry.getKey();
            BankAccount account = entry.getValue();
            if (account != null && account.getAccountType() == AccountType.SAVINGS 
                && bankAccounts.isAccountActive(accountNumber)) {
                double balance = account.getCurrentBalance();
                double interest = balance * INTEREST_RATE;
                account.deposit(interest);
                System.out.printf("Applied %.2f%% interest ($%.2f) to account %d. New balance: $%.2f%n",
                    INTEREST_RATE * 100, interest, accountNumber, account.getCurrentBalance());
            }
        }
    }

    /**
     * Resets the test interest flag. Only used for testing.
     */
    public static void resetTestInterestFlag() {
        interestAppliedForTest = false;
    }

    /**
     * Gets the interest rate as a percentage.
     * @return The interest rate as a percentage (e.g., 20.0 for 20%)
     */
    public static double getInterestRate() {
        return INTEREST_RATE * 100;
    }
}
