package bankingapp;

import java.util.Map;

public class CompoundInterest implements Runnable {

    private final BankAccountDatabase bankAccounts;
    private final long intervalMillis;

    public CompoundInterest(BankAccountDatabase bankAccounts, long intervalMillis) {
        this.bankAccounts = bankAccounts;
        this.intervalMillis = intervalMillis;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(intervalMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            for (Map.Entry<Integer, BankAccount> entry : bankAccounts.getBankAccounts().entrySet()) {
                int accountNumber = entry.getKey();
                BankAccount account = entry.getValue();

                if (bankAccounts.isAccountActive(accountNumber)
                        && account.getAccountType() == AccountType.SAVINGS) {

                    double interest = account.getCurrentBalance() * 0.2;
                    account.deposit(interest);
                    System.out.println("Interest of " + interest + " added to account " + accountNumber);
                }
            }
        }
    }
}

