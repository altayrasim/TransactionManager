package banking_gui;

/**
 * Represents a checking account.
 * This type of account has a monthly interest rate and a monthly fee that can
 * be waived if the balance is above a threshold.
 *
 * @author Jeffery Sypytkowski
 */
public class Checking extends Account {

    /**
     * Constructs a new Checking account with a given profile and initial balance.
     *
     * @param holder  the profile of the account holder
     * @param balance the initial balance of the account
     */
    public Checking(Profile holder, double balance) {
        super(holder, balance);
    }

    /**
     * Calculates the monthly interest for the checking account.
     *
     * @return the monthly interest amount
     */
    @Override
    public double monthlyInterest() {
        return balance * (Constants.CHECKING_ANNUAL_INTEREST_RATE / Constants.MONTHS_COUNT);
    }

    /**
     * Calculates the monthly fee for the checking account.
     * Fee is waived if the balance is greater than or equal to the threshold.
     *
     * @return the monthly fee amount
     */
    @Override
    public double monthlyFee() {
        if (balance >= Constants.CHECKING_MIN_BALANCE_FOR_NO_FEE) {
            return 0;
        }
        return Constants.CHECKING_MONTHLY_FEE;
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
}