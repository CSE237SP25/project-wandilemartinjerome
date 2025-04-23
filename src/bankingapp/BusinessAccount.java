package bankingapp;

/**
 * Represents a business bank account with higher transaction limits.
 * 
 * @author Martin Rivera
 * @author Wandile Hannah
 * @author Jerome Hsing
 */
public class BusinessAccount extends BankAccount {
    
    // Default maximum values for business accounts (higher than regular accounts)
    private static final double DEFAULT_BUSINESS_MAX_WITHDRAWAL = 5000.0;
    private static final double DEFAULT_BUSINESS_MAX_DEPOSIT = 50000.0;
    
    /**
     * Constructs a new business account with an initial balance of 0.
     */
    public BusinessAccount() {
        super(0, DEFAULT_BUSINESS_MAX_WITHDRAWAL, DEFAULT_BUSINESS_MAX_DEPOSIT);
    }
    
    /**
     * Constructs a new business account with the specified initial balance.
     * 
     * @param initBalance The initial balance of the account.
     */
    public BusinessAccount(double initBalance) {
        super(initBalance, DEFAULT_BUSINESS_MAX_WITHDRAWAL, DEFAULT_BUSINESS_MAX_DEPOSIT);
    }
    
    /**
     * Constructs a new business account with an initial balance of 0 and specified account type.
     * 
     * @param accountType The type of account (CHECKING, SAVINGS, etc.)
     */
    public BusinessAccount(AccountType accountType) {
        super(0, DEFAULT_BUSINESS_MAX_WITHDRAWAL, DEFAULT_BUSINESS_MAX_DEPOSIT);
        setAccountType(accountType);
    }
    
    /**
     * Constructs a new business account with the specified initial balance and account type.
     * 
     * @param initBalance The initial balance of the account.
     * @param accountType The type of account (CHECKING, SAVINGS, etc.)
     */
    public BusinessAccount(double initBalance, AccountType accountType) {
        super(initBalance, DEFAULT_BUSINESS_MAX_WITHDRAWAL, DEFAULT_BUSINESS_MAX_DEPOSIT);
        setAccountType(accountType);
    }
    
    /**
     * Constructs a new business account with the specified initial balance and custom transaction limits.
     * 
     * @param initBalance The initial balance of the account.
     * @param maxWithdrawal The maximum withdrawal limit.
     * @param maxDeposit The maximum deposit limit.
     */
    public BusinessAccount(double initBalance, double maxWithdrawal, double maxDeposit) {
        super(initBalance, maxWithdrawal, maxDeposit);
    }
    
    /**
     * Constructs a new business account with the specified initial balance, custom transaction limits, and account type.
     * 
     * @param initBalance The initial balance of the account.
     * @param maxWithdrawal The maximum withdrawal limit.
     * @param maxDeposit The maximum deposit limit.
     * @param accountType The type of account (CHECKING, SAVINGS, etc.)
     */
    public BusinessAccount(double initBalance, double maxWithdrawal, double maxDeposit, AccountType accountType) {
        super(initBalance, maxWithdrawal, maxDeposit);
        setAccountType(accountType);
    }
    
    /**
     * Returns a string representation of this account.
     * 
     * @return A string indicating this is a business account along with the current balance.
     */
    @Override
    public String toString() {
        return "Business Account (Balance: $" + String.format("%.2f", getCurrentBalance()) + ")";
    }
}