package banking_gui;

/**
 * Represents a savings account.
 * This type of account has an annual interest rate, which can be increased if
 * the holder is a loyal customer.
 * The account also has a monthly fee that can be waived if the balance is above
 * a certain threshold.
 *
 * @author Jeffery Sypytkowski
 */
public class Savings extends Account {

    protected boolean isLoyal; // loyal customer status

    /**
     * Constructs a new Savings account with a given profile, initial balance, and
     * customer loyalty status.
     *
     * @param holder  the profile of the account holder
     * @param balance the initial balance of the account
     * @param code the loyalty status of the account holder
     */
    public Savings(Profile holder, double balance, int code) {
        super(holder, balance);
        switch (code) {
            case 0 -> this.isLoyal = false;
            case 1 -> this.isLoyal = true;
        }
    }

    /**
     * Calculates the monthly interest for the savings account.
     * Loyal customers receive an additional bonus to their interest rate.
     *
     * @return the monthly interest amount
     */
    @Override
    public double monthlyInterest() {
        double interestRate = Constants.SAVINGS_INTEREST;
        if (isLoyal) {
            interestRate += Constants.LOYAL_BONUS;
        }
        return balance * (interestRate / Constants.MONTHS_COUNT); // Monthly interest
    }

    /**
     * Calculates the monthly fee for the savings account.
     * Fee is waived if the balance is greater than or equal to the threshold.
     *
     * @return the monthly fee amount
     */
    @Override
    public double monthlyFee() {
        if (balance >= Constants.NO_FEE_BALANCE) {
            return 0;
        } else {
            return Constants.SAVINGS_FEE;
        }
    }

    /**
     * Compares this savings account to another object to determine equality.
     * Savings accounts are considered equal if their holders are the same.
     *
     * @param obj the object to compare with
     * @return true if the accounts are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Savings) {
            Savings account = (Savings) obj;
            return this.holder.equals(account.holder);
        }
        return false;
    }

    /**
     * Prints the class name of the account.
     * 
     * @returns class name
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    /**
     * Updates the loyalty status of the account.
     * 
     * @returns isLoyal status
     */
    public boolean isLoyal() {
        if (this instanceof MoneyMarket) {
            if (this.balance < 2000) {
                isLoyal = false;
            } else {
                isLoyal = true;
            }
        }
        return isLoyal;
    }
}