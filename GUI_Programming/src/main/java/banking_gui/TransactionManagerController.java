package banking_gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Manages user transactions by processing input.
 * Supports transactions such as opening accounts, closing accounts,
 * depositing money, withdrawing money, and printing account details.
 *
 *  @author Altay Ozkan
 *   @author Jeffery Sypytkowski
 */

public class TransactionManagerController {

    @FXML
    private TextArea outputArea;
    @FXML
    private TextField firstName1;
    @FXML
    private TextField lastName1;
    @FXML
    private DatePicker dob1;
    @FXML
    private TextField firstName2;
    @FXML
    private TextField lastName2;
    @FXML
    private DatePicker dob2;
    @FXML
    private RadioButton checkingButton1;
    @FXML
    private RadioButton collegeCheckingButton1;
    @FXML
    private RadioButton savingsButton1;
    @FXML
    private RadioButton moneyMarketButton1;
    @FXML
    private RadioButton checkingButton2;
    @FXML
    private RadioButton collegeCheckingButton2;
    @FXML
    private RadioButton savingsButton2;
    @FXML
    private RadioButton moneyMarketButton2;
    @FXML
    private RadioButton nbButton;
    @FXML
    private RadioButton newarkButton;
    @FXML
    private RadioButton camdenButton;
    @FXML
    private RadioButton loyaltyButton;
    @FXML
    private TextField initialBalance;
    @FXML
    private TextField depWitAmount;
    private AccountDatabase accountDatabase;

    /**
     * Initializes the TransactionManagerController.
     */
    public void initialize() {
        accountDatabase = new AccountDatabase();
        ToggleGroup group1 = new ToggleGroup();
        ToggleGroup group2 = new ToggleGroup();
        ToggleGroup group3 = new ToggleGroup();
        checkingButton1.setToggleGroup(group1);
        collegeCheckingButton1.setToggleGroup(group1);
        savingsButton1.setToggleGroup(group1);
        moneyMarketButton1.setToggleGroup(group1);
        nbButton.setToggleGroup(group2);
        newarkButton.setToggleGroup(group2);
        camdenButton.setToggleGroup(group2);
        checkingButton2.setToggleGroup(group3);
        collegeCheckingButton2.setToggleGroup(group3);
        savingsButton2.setToggleGroup(group3);
        moneyMarketButton2.setToggleGroup(group3);

        group1.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (collegeCheckingButton1.isSelected()) {
                nbButton.setDisable(false);
                newarkButton.setDisable(false);
                camdenButton.setDisable(false);
            } else {
                nbButton.setDisable(true);
                newarkButton.setDisable(true);
                camdenButton.setDisable(true);
            }
            if (savingsButton1.isSelected()) {
                loyaltyButton.setDisable(false);
            } else {
                loyaltyButton.setDisable(true);
            }
        });
    }

    /**
     * Opens a new account when Open button is clicked.
     *
     */
    @FXML
    protected void onOpenClick() {
        try {
            String type;
             if (firstName1.getText().isEmpty() || lastName1.getText().isEmpty() || dob1.getValue() == null || initialBalance.getText().isEmpty()) {
                outputArea.appendText("Missing data for opening an account.\n");
                return;
            }
            Profile profile = new Profile(firstName1.getText(), lastName1.getText(), new Date(dob1.getValue().toString()));
            if (!dateCheck(profile.getDob()))
                return;
            double balance = Double.parseDouble(initialBalance.getText());
            if (balance <= 0) {
                outputArea.appendText("Initial deposit cannot be 0 or negative.\n");
                return;
            }
            if (checkingButton1.isSelected()) {
                type = "C";
            } else if (collegeCheckingButton1.isSelected()) {
                type = "CC";
            } else if (savingsButton1.isSelected()) {
                type = "S";
            } else if (moneyMarketButton1.isSelected()) {
                type = "MM";
            } else{
                outputArea.appendText("Must select account type.\n");
                return;
            }
            Account buffer = createTemp(type, profile);
            boolean validAcct = switch (type) {
                case "CC" ->  createCC(buffer, profile, balance);
                case "S" ->  createS(buffer, profile, balance);
                case "MM" ->  createMM(buffer, profile, balance);
                default ->  createC(buffer, profile, balance);
            };
            if (validAcct)
                outputArea.appendText(profile.getFname() + " " + profile.getLname() + " " + profile.getDob() + "(" + type + ") opened.\n");
        } catch (NumberFormatException e) {
            outputArea.appendText("Not a valid amount.\n");
        }
    }

    /**
     * Creates a MoneyMarket account to add to the database.
     *
     * @param buffer  Account created in order to add to the database.
     * @param profile Profile created in new account being added.
     * @param balance Balance of new account being added.
     * @return true if a valid account is created, false otherwise.
     */
    protected boolean createMM(Account buffer, Profile profile, double balance) {
        if (profile.getDob().getAge() < 16) {
            outputArea.appendText("DOB invalid: " + profile.getDob() + " under 16.\n");
            return false;
        }
        if (balance >= 2000) {
            if (!accountDatabase.contains(buffer)) {
                accountDatabase.open(new MoneyMarket(profile, balance, 0));
                return true;
            } else {
                outputArea.appendText(profile.getFname() + " " + profile.getLname() + " " + profile.getDob()
                        + "(MM) is already in the database.\n");
                return false;
            }
        } else {
            outputArea.appendText("Minimum of $2000 to open a Money Market account.\n");
            return false;
        }
    }

    /**
     * Creates a CollegeChecking account to add to the database.
     *
     * @param buffer  Account created in order to add to the database.
     * @param profile Profile created in new account being added.
     * @param balance Balance of new account being added.
     * @return true if a valid account is created, false otherwise.
     */
    private boolean createCC(Account buffer, Profile profile, double balance) {
        if (profile.getDob().getAge() < 16) {
            outputArea.appendText("DOB invalid: " + profile.getDob() + " under 16.\n");
            return false;
        }
        if (profile.getDob().getAge() >= 24) {
            outputArea.appendText("DOB invalid: " + profile.getDob() + " over 24.\n");
            return false;
        }
        if (nbButton.isSelected() || newarkButton.isSelected() || camdenButton.isSelected()) {
            int code = -1;
            if (nbButton.isSelected()) {
                code = 0;
            } else if (newarkButton.isSelected()) {
                code = 1;
            }
            else if (camdenButton.isSelected()) {
                code = 2;
            }
            if (!accountDatabase.contains(buffer)) {
                accountDatabase.open(new CollegeChecking(profile, balance, code));
                return true;
            } else {
                outputArea.appendText(profile.getFname() + " " + profile.getLname() + " " + profile.getDob()
                        + "(CC) is already in the database.\n");
                return false;
            }
        } else {
            outputArea.appendText("Invalid campus code.\n");
            return false;
        }
    }

    /**
     * Creates a Savings account to add to the database.
     *
     * @param buffer  Account created in order to add to the database.
     * @param profile Profile created in new account being added.
     * @param balance Balance of new account being added.
     * @return true if a valid account is created, false otherwise.
     */
    private boolean createS(Account buffer, Profile profile, double balance) {
        if (profile.getDob().getAge() < 16) {
            outputArea.appendText("DOB invalid: " + profile.getDob() + " under 16.\n");
            return false;
        }
        if (!accountDatabase.contains(buffer)) {
            int code = 0;
            if (loyaltyButton.isSelected()) {
                code = 1;
            }
            accountDatabase.open(new Savings(profile, balance, code));
            return true;
        } else {
            outputArea.appendText(profile.getFname() + " " + profile.getLname() + " " + profile.getDob()
                    + "(S) is already in the database.\n");
            return false;
        }
    }

    /**
     * Creates a Checking account to add to the database.
     *
     * @param buffer  Account created in order to add to the database.
     * @param profile Profile created in new account being added.
     * @param balance Balance of new account being added.
     * @return true if a valid account is created, false otherwise.
     */
    private boolean createC(Account buffer, Profile profile, double balance) {
        if (profile.getDob().getAge() < 16) {
            outputArea.appendText("DOB invalid: " + profile.getDob() + " under 16.\n");
            return false;
        }
        if (!accountDatabase.contains(buffer)) {
            accountDatabase.open(new Checking(profile, balance));
            return true;
        } else {
            outputArea.appendText(profile.getFname() + " " + profile.getLname() + " " + profile.getDob()
                    + "(C) is already in the database.\n");
            return false;
        }
    }

    /**
     * Validates the given date, ensuring it's not a future date.
     *
     * @param date the date to be validated.
     * @return true if the date is valid and not in the future, false otherwise.
     */
    protected boolean dateCheck(Date date) {
        Date todaysDate = new Date();
        if (!date.isValid()) {
            outputArea.appendText("DOB invalid: " + date + " not a valid calendar date!\n");
            return false;
        }
        if ((date.getYear() > todaysDate.getYear())
                || (date.getYear() == todaysDate.getYear() && date.getMonth() > todaysDate.getMonth())
                || (date.getYear() == todaysDate.getYear() && date.getMonth() == todaysDate.getMonth()
                && date.getDay() >= todaysDate.getDay())) {
            outputArea.appendText("DOB invalid: " + date + " cannot be today or a future day.\n");
            return false;
        }
        return true;
    }

    /**
     * Creates a buffer account object.
     *
     * @param type    The type of account.
     * @param profile The profile associated with the account.
     * @return A new Account object.
     */
    protected Account createTemp(String type, Profile profile) {
        switch (type) {
            case "CC":
                return new CollegeChecking(profile, 0, 0);
            case "S":
                return new Savings(profile, 0, 0);
            case "MM":
                return new MoneyMarket(profile, 0, 0);
            default:
                return new Checking(profile, 0);
        }
    }

    /**
     * Closes an existing account when Close button is clicked.
     *
     */
    @FXML
    protected void onCloseClick() {
            if (firstName1.getText().isEmpty() || lastName1.getText().isEmpty() || dob1.getValue() == null) {
                outputArea.appendText("Missing data for closing an account.\n");
                return;
            }
            String type;
            Profile profile = new Profile(firstName1.getText(), lastName1.getText(),
                    new Date(dob1.getValue().toString()));
            if (!dateCheck(profile.getDob()))
                return;
            if (checkingButton1.isSelected()) {
                type = "C";
            } else if (collegeCheckingButton1.isSelected()) {
                type = "CC";
            } else if (savingsButton1.isSelected()) {
                type = "S";
            } else if (moneyMarketButton1.isSelected()) {
                type = "MM";
            } else{
                outputArea.appendText("Must select account type.\n");
                return;
            }
            Account account = createTemp(type, profile);
            if (accountDatabase.contains(account)) {
                if (accountDatabase.getAccount(account) != null && account.getClass() == accountDatabase.getAccount(account).getClass()) {
                    if (accountDatabase.close(account)) {
                        outputArea.appendText(account.getHolder().getFname() + " " + account.getHolder().getLname() + " "
                                + account.getHolder().getDob() + "(" + type + ") has been closed.\n");
                    } else {
                        outputArea.appendText("Error closing account.\n");
                    }
                } else {
                    outputArea.appendText(account.getHolder().getFname() + " " + account.getHolder().getLname() + " "
                            + account.getHolder().getDob() + "(" + type + ") is not in the database.\n");
                }
            } else {
                outputArea.appendText(account.getHolder().getFname() + " " + account.getHolder().getLname() + " "
                        + account.getHolder().getDob() + "(" + type + ") is not in the database.\n");
            }
    }

    /**
     * Clears Output Area when Clear button is clicked.
     */
    @FXML
    protected void onClearClick() {
        outputArea.clear();
    }

    /**
     * Deposits into existing account when Deposit button is clicked.
     *
     */
    @FXML
    protected void onDepositClick() {
        if (firstName2.getText().isEmpty() || lastName2.getText().isEmpty() || dob2.getValue() == null || depWitAmount.getText().isEmpty()) {
            outputArea.appendText("Missing data for depositing to an account.\n");
            return;
        }
        try {
            String type;
            Profile profile = new Profile(firstName2.getText(), lastName2.getText(),
                    new Date(dob2.getValue().toString()));
            double amount = Double.parseDouble(depWitAmount.getText());
            if (amount <= 0) {
                outputArea.appendText("Deposit - amount cannot be 0 or negative.\n");
                return;
            }
            if (checkingButton2.isSelected()) {
                type = "C";
            } else if (collegeCheckingButton2.isSelected()) {
                type = "CC";
            } else if (savingsButton2.isSelected()) {
                type = "S";
            } else if (moneyMarketButton2.isSelected()) {
                type = "MM";
            } else{
                outputArea.appendText("Must select account type.\n");
                return;
            }
            Account account = createUpdateBalance(type, profile, amount);
            accountDatabase.deposit(account);
            if (account.getBalance() == Constants.ACCOUNT_FOUND) {
                outputArea.appendText(account.getHolder().getFname() + " " + account.getHolder().getLname() + " "
                        + account.getHolder().getDob() + "(" + type + ") Deposit - balance updated.\n");
            } else {
                outputArea.appendText(account.getHolder().getFname() + " " + account.getHolder().getLname() + " "
                        + account.getHolder().getDob() + "(" + type + ") is not in the database.\n");
            }
        } catch (NumberFormatException e) {
            outputArea.appendText("Not a valid amount.\n");
        }
    }

    /**
     * Creates a temporary account object for balance updates.
     *
     * @param type    the type of the account (e.g., "CC", "S", "MM").
     * @param profile the profile of the account holder.
     * @param balance the balance to be set for the account.
     * @return the newly created account object with the specified balance.
     */
    private Account createUpdateBalance(String type, Profile profile, double balance) {
        switch (type) {
            case "CC":
                return new CollegeChecking(profile, balance, 0);
            case "S":
                return new Savings(profile, balance, 0);
            case "MM":
                return new MoneyMarket(profile, balance, 0);
            default:
                return new Checking(profile, balance);
        }
    }

    /**
     * Withdraws from existing account when Withdraw button is clicked.
     *
     */
    @FXML
    protected void onWithdrawClick() {
        if (firstName2.getText().isEmpty() || lastName2.getText().isEmpty() || dob2.getValue() == null || depWitAmount.getText().isEmpty()) {
            outputArea.appendText("Missing data for withdrawing from an account.\n");
            return;
        }
        try {
            String type;
            Profile profile = new Profile(firstName2.getText(), lastName2.getText(), new Date(dob2.getValue().toString()));
            double amount = Double.parseDouble(depWitAmount.getText());
            if (amount <= 0) {
                outputArea.appendText("Withdraw - amount cannot be 0 or negative.\n");
                return;
            }
            if (checkingButton2.isSelected()) {
                type = "C";
            } else if (collegeCheckingButton2.isSelected()) {
                type = "CC";
            } else if (savingsButton2.isSelected()) {
                type = "S";
            } else if (moneyMarketButton2.isSelected()) {
                type = "MM";
            } else{
                outputArea.appendText("Must select account type.\n");
                return;
            }
            Account account = createUpdateBalance(type, profile, amount);
            if (accountDatabase.withdraw(account)) {
                outputArea.appendText(account.getHolder().getFname() + " " + account.getHolder().getLname() + " "
                        + account.getHolder().getDob() + "(" + type + ") Withdraw - balance updated.\n");
            } else if (account.getBalance() == Constants.NOT_FOUND) {
                outputArea.appendText(account.getHolder().getFname() + " " + account.getHolder().getLname() + " "
                        + account.getHolder().getDob() + "(" + type + ") is not in the database.\n");
            } else {
                outputArea.appendText(account.getHolder().getFname() + " " + account.getHolder().getLname() + " "
                        + account.getHolder().getDob() + "(" + type + ") Withdraw - insufficient fund.\n");
            }
        } catch (NumberFormatException e) {
            outputArea.appendText("Not a valid amount.\n");
        }
    }

    /**
     * Prints accounts from account database when Print All button is clicked.
     *
     */
    @FXML
    protected void onPrintAllClick() {
        accountDatabase.printSorted(outputArea);
    }

    /**
     * Loads accounts from text file when Load Accounts button is clicked.
     *
     */
    @FXML
    protected void onLoadAccountsClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Account Data File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            loadAccountsFromFile(selectedFile);
        } else {
            outputArea.appendText("No file was selected.\n");
        }
    }

    /**
     * Prints accounts from account database including interest and fees when Print Interest Fee button is clicked.
     *
     */
    @FXML
    protected void onPrintInterestFeeClick() {
        accountDatabase.printFeesAndInterests(outputArea);
    }

    /**
     * Prints accounts from account database with updated interest and fees when Update Accounts button is clicked.
     *
     */
    @FXML
    protected void onUpdateAccountsClick() {
        accountDatabase.printUpdatedBalances(outputArea);
    }

    /**
     * Processes each account to be opened from given text file.
     * @param file the text file with accounts to be opened.
     *
     */
    private void loadAccountsFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                addAccountToDatabase(line);
            }
            outputArea.appendText("Accounts loaded.");
        } catch (IOException e) {
            outputArea.appendText("Failed to load accounts from the file: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    /**
     * Formats given account information to be properly processed.
     * @param accountData the account information to be formatted.
     *
     */
    private void addAccountToDatabase(String accountData) {
        try {
            String[] values = accountData.split(",");
            if (values.length == 5 || values.length == 6) {
                loadAccountHelper(values);
            } else {
                outputArea.appendText("Invalid account data format: " + accountData + "\n");
            }
        } catch (Exception e) {
            outputArea.appendText("Error processing account data: " + accountData + ". Error: " + e.getMessage() + "\n");
        }
    }

    /**
     * Adds loaded and formatted accounts to the database.
     * @param values information for an account to be opened from the text file.
     *
     */
    private void loadAccountHelper(String[] values){
        try {
            Profile profile = new Profile(values[1], values[2],
                    new Date(values[3]));
            if (!dateCheck(profile.getDob())) {
                return;
            }
            double balance = Double.parseDouble(values[4]);
            if (balance <= 0) {
                outputArea.appendText("Initial deposit cannot be 0 or negative.\n");
                return;
            }
            String type = values[0];
            Account buffer = createTemp(type, profile);
            switch (type) {
                case "CC" -> createLoadedCC(buffer, profile, balance, Integer.parseInt(values[5]));
                case "S" -> createLoadedS(buffer, profile, balance, Integer.parseInt(values[5]));
                case "MM" -> createMM(buffer, profile, balance);
                default -> createC(buffer, profile, balance);
            }
        } catch (java.util.NoSuchElementException e) {
            outputArea.appendText("Missing data for opening an account.\n");
        } catch (NumberFormatException e) {
            outputArea.appendText("Not a valid amount.\n");
        }
    }

    /**
     * Creates a CollegeChecking account to add to the database from a text file.
     *
     * @param buffer  Account created in order to add to the database.
     * @param profile Profile created in new account being added.
     * @param balance Balance of new account being added.
     * @param code    Code for the campus of account being added.
     */
    private boolean createLoadedCC(Account buffer, Profile profile, double balance, int code) {
        if (profile.getDob().getAge() < 16) {
            outputArea.appendText("DOB invalid: " + profile.getDob() + " under 16.\n");
            return false;
        }
        if (profile.getDob().getAge() >= 24) {
            outputArea.appendText("DOB invalid: " + profile.getDob() + " over 24.\n");
            return false;
        }
        if (code >= 0 && code <= 2) {
            if (!accountDatabase.contains(buffer)) {
                accountDatabase.open(new CollegeChecking(profile, balance, code));
                return true;
            } else {
                outputArea.appendText(profile.getFname() + " " + profile.getLname() + " " + profile.getDob()
                        + "(CC) is already in the database.\n");
                return false;
            }
        } else {
            outputArea.appendText("Invalid campus code.\n");
            return false;
        }
    }

    /**
     * Creates a Savings account to add to the database from a text file.
     *
     * @param buffer  Account created in order to add to the database.
     * @param profile Profile created in new account being added.
     * @param balance Balance of new account being added.
     * @param code    Code for the isLoyal status of account being added.
     */
    private boolean createLoadedS(Account buffer, Profile profile, double balance, int code) {
        if (profile.getDob().getAge() < 16) {
            outputArea.appendText("DOB invalid: " + profile.getDob() + " under 16.\n");
            return false;
        }
        if(code != 0 && code != 1) {
            outputArea.appendText("Invalid loyalty code.\n");
            return false;
        }
        if (!accountDatabase.contains(buffer)) {
            accountDatabase.open(new Savings(profile, balance, code));
            return true;
        } else {
            outputArea.appendText(profile.getFname() + " " + profile.getLname() + " " + profile.getDob()
                    + "(S) is already in the database.\n");
            return false;
        }
    }
}