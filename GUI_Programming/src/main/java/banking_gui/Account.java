package banking_gui;

/**
 * This class represents a general banking account. It is abstract because
 * specific types
 * of accounts (e.g., Checking, Savings) will derive from this class.
 * Each account has a holder's profile and a balance.
 * 
 * @author Altay Ozkan
 */
public abstract class Account implements Comparable<Account> {

    // The profile of the account holder.
    protected Profile holder;

    // The current balance of the account.
    protected double balance;

    /**
     * Constructor for Account class.
     * 
     * @param holder  The profile of the account holder.
     * @param balance The initial balance of the account.
     */
    public Account(Profile holder, double balance) {
        this.holder = holder;
        this.balance = balance;
    }

    /**
     * Gets the holder's profile.
     * 
     * @return The profile of the account holder.
     */
    public Profile getHolder() {
        return holder;
    }

    /**
     * Gets the holder's profile.
     * 
     * @param amount The amount the balance will be changed to.
     * 
     * @return The new balance of the account.
     */
    public void setBalance(double amount) {
        balance = amount;
    }

    /**
     * Gets the current balance of the account.
     * 
     * @return The balance of the account.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Calculates and returns the monthly interest for the account.
     * 
     * @return The monthly interest amount.
     */
    public abstract double monthlyInterest();

    /**
     * Calculates and returns the monthly fee for the account.
     * 
     * @return The monthly fee amount.
     */
    public abstract double monthlyFee();

    /**
     * Compares this account with another account. The comparison is first based
     * on the balance, and then on the holder's profile.
     * 
     * @param otherAccount The other account to compare with.
     * @return A negative integer, zero, or a positive integer as this account
     *         is less than, equal to, or greater than the specified account.
     */
    @Override
    public int compareTo(Account otherAccount) {
        if (this.balance != otherAccount.balance) {
            return Double.compare(this.balance, otherAccount.balance);
        }
        return this.holder.compareTo(otherAccount.holder);
    }
}
