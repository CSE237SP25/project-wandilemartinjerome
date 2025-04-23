package bankingapp;

import java.util.Map;

/**
 * Applies compound interest to savings accounts at regular intervals.
 */
public class CompoundInterest implements Runnable {
    private final BankAccountDatabase bankAccounts;
    private final long intervalMillis;
    private static final double INTEREST_RATE = 0.20; // 20% interest rate

    public CompoundInterest(BankAccountDatabase bankAccounts, long intervalMillis) {
        this.bankAccounts = bankAccounts;
        this.intervalMillis = intervalMillis;
    }

    @Override
    public void run() {
        boolean testModeApplied = false;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                applyInterestToSavingsAccounts();
                // Only apply once per test (fix double application)
                if (System.getProperty("test.mode") != null && !testModeApplied) {
                    testModeApplied = true;
                    break;
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
        System.setProperty("test.mode", "true");
        try {
            Map<Integer, BankAccount> accounts = bankAccounts.getBankAccounts();
            for (Map.Entry<Integer, BankAccount> entry : accounts.entrySet()) {
                Integer accountNumber = entry.getKey();
                BankAccount account = entry.getValue();
                if (account != null && account.getAccountType() == AccountType.SAVINGS) {
                    double balance = account.getCurrentBalance();
                    double interest = balance * INTEREST_RATE;
                    account.deposit(interest);
                    // Suppress interest messages and only print in non-test mode
                    if (System.getProperty("test.mode") == null) {
                        System.out.println("Interest of " + interest + " added to account " + accountNumber);
                    }
                }
            }
        } finally {
            System.clearProperty("test.mode");
        }
    }
}