package banking_gui;

/**
 * Represents a Money Market savings account.
 * This type of account has specific withdrawal limits, interest rates, and fees
 * based on balance and number of withdrawals.
 *
 * @author Jeffery Sypytkowski
 * @author Altay Ozkan
 */
public class MoneyMarket extends Savings {

    // Counter to keep track of the number of withdrawals made.
    private int withdrawal;

    /**
     * Constructor for the MoneyMarket account.
     *
     * @param holder  The profile of the account holder.
     * @param balance The initial balance of the account.
     * @param code    A specific code related to the account (its exact meaning is
     *                not detailed in the provided code).
     */
    public MoneyMarket(Profile holder, double balance, int code) {
        super(holder, balance, code);
        this.withdrawal = 0;
        this.isLoyal = true; // By default, set the account as loyal.
    }

    /**
     * Calculates the monthly interest for the money market account.
     *
     * @return The monthly interest amount.
     */
    @Override
    public double monthlyInterest() {
        double interestRate = Constants.MM_INTEREST;
        if (isLoyal) {
            interestRate += Constants.LOYAL_BONUS;
        }
        return balance * (interestRate / Constants.MONTHS_COUNT); // Monthly interest
    }

    /**
     * Determines the monthly fee for the money market account.
     * The fee is based on the account balance and the number of withdrawals.
     *
     * @return The monthly fee amount.
     */
    @Override
    public double monthlyFee() {
        if (balance >= Constants.MIN_BALANCE_LOYAL && withdrawal <= Constants.WITHDRAW_LIMIT) {
            return 0;
        } else if (withdrawal > Constants.WITHDRAW_LIMIT && balance >= Constants.MIN_BALANCE_LOYAL) {
            return Constants.FEE_WITHDRAW_OVER_LIMIT;
        } else if (withdrawal > Constants.WITHDRAW_LIMIT && balance < Constants.MIN_BALANCE_LOYAL) {
            return Constants.MM_PLUS_FEE;
        }
        return Constants.SAVINGS_FEE;
    }

    /**
     * 
     * Resets the withdrawals value of the account to 0.
     */
    public void resetWithdrawals() {
        this.withdrawal = 0;
    }

    /**
     * Increments the withdrawal counter for the account.
     */
    public void incrementWithdrawals() {
        this.withdrawal++;
    }

    /**
     * Retrieves the number of withdrawals made from the account.
     *
     * @return The current withdrawal count.
     */
    public int getWithdrawals() {
        return this.withdrawal;
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