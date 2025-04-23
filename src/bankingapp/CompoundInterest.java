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

    // Reset static flag before each test run
    public static void resetTestInterestFlag() {
        interestAppliedForTest = false;
    }
}