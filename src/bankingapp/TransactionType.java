package bankingapp;

/**
 * Enum for transaction types.
 */
public enum TransactionType {
    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal"),
    TRANSFER("Transfer"),
    LIMIT_CHANGE("Limit Change"),
    FAILED("Failed Transaction"),
    ADMIN("Administrative Action"),
    SCHEDULED("Scheduled");

    private final String displayName;

    private TransactionType(String displayName) {
        this.displayName = displayName;
    }
   
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
