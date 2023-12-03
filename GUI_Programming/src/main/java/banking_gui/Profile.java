package banking_gui;

/**
 * Represents an individual's profile with their name and date of birth.
 * This class implements the Comparable interface to allow for sorting profiles
 * based on certain criteria.
 *
 * @author Altay Ozkan
 */
public class Profile implements Comparable<Profile> {

    // The first name of the individual
    private String fname;

    // The last name of the individual
    private String lname;

    // The date of birth of the individual
    private Date dob;

    /**
     * Constructor to initialize a profile with a first name, last name, and date of
     * birth.
     *
     * @param fname The first name of the individual.
     * @param lname The last name of the individual.
     * @param dob   The date of birth of the individual.
     */
    public Profile(String fname, String lname, Date dob) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
    }

    /**
     * Retrieves the first name of the individual.
     *
     * @return The first name.
     */
    public String getFname() {
        return fname;
    }

    /**
     * Retrieves the last name of the individual.
     *
     * @return The last name.
     */
    public String getLname() {
        return lname;
    }

    /**
     * Retrieves the date of birth of the individual.
     *
     * @return The date of birth.
     */
    public Date getDob() {
        return dob;
    }

    /**
     * Compares two profiles based on last name, first name, and then date of birth.
     * This method is used for sorting purposes.
     *
     * @param otherProfile The other profile to compare with.
     * @return A negative integer, zero, or a positive integer as this profile is
     *         less than,
     *         equal to, or greater than the specified profile.
     */
    @Override
    public int compareTo(Profile otherProfile) {
        int lastNameComparison = this.lname.compareTo(otherProfile.lname);
        if (lastNameComparison != 0) {
            return lastNameComparison;
        }

        int firstNameComparison = this.fname.compareTo(otherProfile.fname);
        if (firstNameComparison != 0) {
            return firstNameComparison;
        }

        return this.dob.compareTo(otherProfile.dob);
    }
}