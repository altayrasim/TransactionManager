package banking_gui;

/**
 * Represents a college checking account, which is a type of checking account.
 * This account is associated with a specific campus and has specific interest
 * and fee rules.
 *
 * @author Altay Ozkan
 */
public class CollegeChecking extends Checking {

    // Enum indicating the campus to which this account is associated.
    private Campus campus;

    /**
     * Constructor for the CollegeChecking account.
     *
     * @param holder  The profile of the account holder.
     * @param balance The initial balance of the account.
     * @param code    An integer code representing the campus (0 for NEW_BRUNSWICK,
     *                1 for NEWARK, 2 for CAMDEN).
     */
    public CollegeChecking(Profile holder, double balance, int code) {
        super(holder, balance);
        switch (code) {
            case 0 -> this.campus = Campus.NEW_BRUNSWICK;
            case 1 -> this.campus = Campus.NEWARK;
            case 2 -> this.campus = Campus.CAMDEN;
        }
    }

    /**
     * Calculates the monthly interest for the college checking account.
     *
     * @return The monthly interest amount.
     */
    @Override
    public double monthlyInterest() {
        return balance * (Constants.CHECKING_ANNUAL_INTEREST_RATE / Constants.MONTHS_COUNT);
    }

    /**
     * Retrieves the monthly fee for the college checking account. For college
     * checking accounts,
     * there is no monthly fee.
     *
     * @return The monthly fee amount, which is 0 for college checking accounts.
     */
    @Override
    public double monthlyFee() {
        return 0;
    }

    /**
     * Gets the campus associated with this college checking account.
     *
     * @return The campus enum value.
     */
    public Campus getCampus() {
        return campus;
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