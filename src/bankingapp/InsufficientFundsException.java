package bankingapp;

/**
 * Custom exception for insufficient funds during a transaction.
 */
public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
