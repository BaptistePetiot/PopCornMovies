package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * Controller of the LOGIN scene
 * Handles sign ups and logins for employee, customers and guests
 * @author Baptiste Petiot
 */
public class ControllerLogin {
    // class attributes
    private double x, y;
    private boolean isEmployee;
    private String date;

    // Javafx elements
    @FXML
    Button buttonMember, buttonGuest, buttonSignup, buttonExit, buttonCustomer, buttonEmployee, buttonBack, buttonLogin, buttonContinueAsGuest;
    @FXML
    AnchorPane anchorPane;
    @FXML
    TextField emailLogin, emailSignup, lastName, firstName;
    @FXML
    PasswordField passwordLogin, passwordSignup;
    @FXML
    Label firstNameAndLastName;

    // credentials
    private final String url = "jdbc:mysql://localhost:3306/popcornmovie";
    private final String user = "root";
    private final String password = "";

    public ControllerLogin(){
        // handle current date
        this.isEmployee = false;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date(System.currentTimeMillis());
        this.date = formatter.format(now);
    }

    /***
     * allows to use ENTER key to log in
     * @param keyEvent : KeyEvent
     * @throws IOException : input output exception
     */
    @FXML
    public void handleEnterLogin(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            login();
        }
    }

    /***
     * allows to use ENTER key to sign up
     * @param keyEvent : KeyEvent
     */
    @FXML
    public void handleEnterSignup(KeyEvent keyEvent)  {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            signup();
        }
    }

    /***
     * verifies that email and password are correct and if so logs in
     */
    @FXML
    private void login() {
        // connect to DB
        Connection connection = null;
        boolean emailOK = false, passwordOK = false;

        try {
            // create a connection to the database
            connection = DriverManager.getConnection(url, user, password);

            // statement
            Statement stmt = connection.createStatement();
            ResultSet rs;

            // check email
            String email = emailLogin.getText();
            String password = passwordLogin.getText();
            String clearPassword = "", cipherPassword = "", ivString = "";
            if (email.contains("@") && email.contains(".")) { // check email format
                rs = stmt.executeQuery("SELECT `Email` FROM logins");
                while (rs.next()) {
                    String s = rs.getString(1);
                    if (s.equals(email)) {    //check email is in DB
                        emailOK = true;
                    }
                }
            }
            if (!emailOK) {
                System.out.println("Email invalid or unknown");
            } else {
                // check password
                // retrieve cipherPassword + key from DB
                rs = stmt.executeQuery("SELECT `HashPassword`, `KeyPassword`, `IV` FROM logins WHERE Email = '" + email + "';");
                String keyPassword = "";
                while (rs.next()) {
                    cipherPassword = rs.getString("HashPassword");
                    keyPassword = rs.getString("KeyPassword");
                    ivString = rs.getString("IV");
                }


                // CRYPTO
                // decrypt
                byte[] ivArray = PasswordEncrypterDecrypter.string2Array(ivString);
                IvParameterSpec iv = new IvParameterSpec(ivArray);
                SecretKey key = PasswordEncrypterDecrypter.string2SecretKey(keyPassword);
                clearPassword = PasswordEncrypterDecrypter.decrypt(cipherPassword, key, iv);

                if (clearPassword.equals(password)) {
                    passwordOK = true;
                }
            }
            if (!passwordOK) {
                System.out.println("Password incorrect or unregistered");
            }

            if (emailOK && passwordOK) {
                // detect whether the user connecting is a customer or an employee
                boolean isCustomer = true;
                int nbrRows = 0;
                rs = stmt.executeQuery("SELECT * FROM Customers WHERE IdLogins = (SELECT `Id` FROM logins WHERE Email = '" + email + "'AND HashPassword = '" + cipherPassword + "');");

                int customerId = 0;
                String customerLastName, customerFirstName;
                while (rs.next()) {
                    customerId = rs.getInt("IdLogins");
                    customerLastName = rs.getString("LastName");
                    customerFirstName = rs.getString("FirstName");
                    Me.setId(customerId);
                    Me.setFirstName(customerFirstName);
                    Me.setLastName(customerLastName);
                    Me.setEmail(email);
                    nbrRows++;
                }
                // remember preferred theme
                rs = stmt.executeQuery("SELECT themeNbr FROM Theme WHERE IdLogins = " + customerId + ";");
                int themeNbr = 0;
                while (rs.next()) {
                    themeNbr = rs.getInt("themeNbr");
                }

                // remember category
                rs = stmt.executeQuery("SELECT categoryNbr FROM Category WHERE IdLogins = " + customerId + ";");
                int categoryNbr = 0;
                while (rs.next()) {
                    categoryNbr = rs.getInt("categoryNbr");
                }

                if (nbrRows == 0) {
                    rs = stmt.executeQuery("SELECT * FROM Employees WHERE IdLogins = (SELECT `Id` FROM logins WHERE Email = '" + email + "'AND HashPassword = '" + cipherPassword + "');");
                    int employeeId = 0;
                    String employeeLastName, employeeFirstName;
                    while (rs.next()) {
                        employeeId = rs.getInt("IdLogins");
                        employeeLastName = rs.getString("LastName");
                        employeeFirstName = rs.getString("FirstName");
                        Me.setId(employeeId);
                        Me.setFirstName(employeeFirstName);
                        Me.setLastName(employeeLastName);
                        Me.setEmail(email);
                        nbrRows++;
                    }
                    isCustomer = false;

                    // remember preferred theme
                    rs = stmt.executeQuery("SELECT themeNbr FROM Theme WHERE IdLogins = " + employeeId + ";");
                    while (rs.next()) {
                        themeNbr = rs.getInt("themeNbr");
                    }

                    // remember category
                    rs = stmt.executeQuery("SELECT categoryNbr FROM Category WHERE IdLogins = " + employeeId + ";");
                    while (rs.next()) {
                        categoryNbr = rs.getInt("categoryNbr");
                    }
                }

                Me.setTheme(themeNbr);
                Me.setCategory(categoryNbr);

                if (isCustomer) {
                    goToCustomerApp();

                } else {
                    goToEmployeeApp();
                }
            }

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Problem");
            alert.setHeaderText("Cannot perform the sql request");
            alert.setContentText("Please warn the developers that they might have made a mistake with their request.");
            alert.showAndWait();

            System.out.println(e.getMessage());

        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Problem");
            alert.setHeaderText("Problem with encryption");
            alert.setContentText("Please check there aren't any issue with the chosen algorithm, the generated key, the padding or the block size.");
            alert.showAndWait();

            e.printStackTrace();
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Problem");
            alert.setHeaderText("Cannot log in");
            alert.setContentText("Please be sure that you have typed in your correct credentials.");
            alert.showAndWait();

            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Problem");
                alert.setHeaderText("Cannot connect to the DB");
                alert.setContentText("Please check that the DB is up and running, restart the services if needed");
                alert.showAndWait();

                System.out.println(e.getMessage());
            }
        }
    }

    /***
     * check if email is not already in the DB and if not, allows the user to sign up
     */
    @FXML
    private void signup() {
        // connect to DB
        Connection connection = null;
        boolean emailOK = false;

        try {
            // create a connection to the database
            connection = DriverManager.getConnection(url, user, password);

            // statement
            Statement stmt = connection.createStatement();
            ResultSet rs;

            // check email
            String email = emailSignup.getText();
            if (email.contains("@") && email.contains(".")) { // check email format
                rs = stmt.executeQuery("SELECT `Email` FROM logins");
                emailOK = true;
                while (rs.next()) {
                    String s = rs.getString("Email");
                    if (s.equals(email)) {    //check email is not already in DB
                        emailOK = false;

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Problem");
                        alert.setHeaderText("Cannot sign up");
                        alert.setContentText("Email address already in the DB");
                        alert.showAndWait();
                        break;
                    }
                }
            }
            if (!emailOK) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Problem");
                alert.setHeaderText("Cannot sign up");
                alert.setContentText("Please enter a valid email address");
                alert.showAndWait();

                System.out.println("Email invalid or already registered");
            }

            if (emailOK) {
                // getting Id Max
                rs = stmt.executeQuery("SELECT MAX(`Id`) FROM `Logins`");

                int maxId = 0;
                while (rs.next()) {
                    maxId = rs.getInt(1);
                }
                int nextId = maxId + 1;

                // CRYPTO

                //generate key
                SecretKey key = PasswordEncrypterDecrypter.generateKey();
                byte[] iv = PasswordEncrypterDecrypter.generateIv();
                IvParameterSpec ivParameterSpec = PasswordEncrypterDecrypter.generateIvParameterSpec(iv);

                //encrypt
                String cipherPassword = PasswordEncrypterDecrypter.encrypt(passwordSignup.getText(), key, ivParameterSpec);
                // save cipherPassword + key + iv to the DB
                String keyPassword = PasswordEncrypterDecrypter.secretKey2String(key);
                String ivString = PasswordEncrypterDecrypter.array2String(iv);

                // inserting row in logins table
                String sqlINSERTStatement = "INSERT INTO `logins` (`Id`, `Email`, `HashPassword`, `KeyPassword`, `IV`) VALUES ('" + nextId + "', '" + emailSignup.getText() + "', '" + cipherPassword + "', '" + keyPassword + "', '" + ivString + "');";
                stmt.executeUpdate(sqlINSERTStatement);

                if (isEmployee) {
                    // inserting row in employee table
                    sqlINSERTStatement = "INSERT INTO `Employees` (`IdLogins`, `LastName`, `Firstname`, `DateOfCreation`) VALUES (" + nextId + ", '" + lastName.getText() + "', '" + firstName.getText() + "', '" + date + "');";
                    stmt.executeUpdate(sqlINSERTStatement);

                    // inserting row in theme table
                    sqlINSERTStatement = "INSERT INTO `Theme` (`IdLogins`, `themeNbr`) VALUES (" + nextId + ", '0');";
                    stmt.executeUpdate(sqlINSERTStatement);

                    // inserting row in category table
                    sqlINSERTStatement = "INSERT INTO `Category` (`IdLogins`, `categoryNbr`) VALUES (" + nextId + ", '0');";
                    stmt.executeUpdate(sqlINSERTStatement);

                    // updating the array lists
                    Cinema.refresh();

                    // send email
                    MailSender.sendMail(emailSignup.getText(), firstName.getText(), true);
                } else {
                    // inserting row in customer table
                    sqlINSERTStatement = "INSERT INTO `Customers` (`IdLogins`, `LastName`, `Firstname`, `DateOfCreation`) VALUES (" + nextId + ", '" + lastName.getText() + "', '" + firstName.getText() + "', '" + date + "');";
                    stmt.executeUpdate(sqlINSERTStatement);

                    // inserting row in theme table
                    sqlINSERTStatement = "INSERT INTO `Theme` (`IdLogins`, `themeNbr`) VALUES (" + nextId + ", '0');";
                    stmt.executeUpdate(sqlINSERTStatement);

                    // inserting row in category table
                    sqlINSERTStatement = "INSERT INTO `Category` (`IdLogins`, `categoryNbr`) VALUES (" + nextId + ", '0');";
                    stmt.executeUpdate(sqlINSERTStatement);

                    // updating the array lists
                    Cinema.refresh();

                    // send email
                    MailSender.sendMail(emailSignup.getText(), firstName.getText(), false);
                }

                System.out.println("Correctly registered");
                goBackToMember();
            }

        } catch (SQLException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Problem");
            alert.setHeaderText("Cannot perform the sql request");
            alert.setContentText("Please warn the developers that they might have made a mistake.");
            alert.showAndWait();

            System.out.println(e.getMessage());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Problem");
            alert.setHeaderText("Cannot signup");
            alert.setContentText("Please be sure that you are not already in our DB and that your email is a real one.");
            alert.showAndWait();

            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Problem");
                alert.setHeaderText("Cannot connect to the DB");
                alert.setContentText("Please check that the DB is up and running, restart the services if needed");
                alert.showAndWait();

                System.out.println(e.getMessage());
            }
        }

    }

    /***
     * change border to frame customer
     */
    @FXML
    private void changeToCustomer() {
        buttonEmployee.setStyle("-fx-background-color: #9C2B27; -fx-border-color: transparent; -fx-cursor: hand;");
        buttonCustomer.setStyle("-fx-background-color: #9C2B27; -fx-border-color: #ffffff; -fx-cursor: hand;");
        this.isEmployee = false;
    }

    /***
     * change border to frame employee
     */
    @FXML
    private void changeToEmployee() {
        buttonCustomer.setStyle("-fx-background-color: #9C2B27; -fx-border-color: transparent; -fx-cursor: hand;");
        buttonEmployee.setStyle("-fx-background-color: #9C2B27; -fx-border-color: #ffffff; -fx-cursor: hand;");
        this.isEmployee = true;
    }

    // NAVIGATION

    /***
     * function that loads the GUEST scene
     */
    @FXML
    protected void goToGuest() {
        System.out.println("GUEST");
        try {
            SceneManager.loadScene("../view/guest.fxml", 700, 400);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the MEMBER scene
     */
    @FXML
    protected void goToMember() {
        System.out.println("MEMBER");
        try {
            SceneManager.loadScene("../view/login.fxml", 700, 400);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that goes back to the previous scene : MEMBER
     */
    @FXML
    private void goBackToMember() {
        goToMember();
    }

    /***
     * function that loads the SIGNUP scene
     */
    @FXML
    protected void goToSignup() {
        System.out.println("SIGNUP");
        try {
            SceneManager.loadScene("../view/signup.fxml", 700, 400);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the GUEST application and directly goes to the OVERVIEW scene
     */
    @FXML
    protected void goToCustomerApp() {
        System.out.println("GO TO CUSTOMER APP");
        System.out.println(Me.getId() + " / " + Me.getLastName() + " / " + Me.getFirstName());

        try {
            SceneManager.loadScene("../view/customer-overview.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the EMPLOYEE application and directly goes to the OVERVIEW scene
     */
    @FXML
    protected void goToEmployeeApp() {
        System.out.println("GO TO EMPLOYEE APP");
        System.out.println(Me.getId() + " / " + Me.getLastName() + " / " + Me.getFirstName());

        try {
            SceneManager.loadScene("../view/employee-overview.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the GUEST application and directly goes to the OVERVIEW scene
     */
    @FXML
    public void continueAsGuest() {
        System.out.println("GO TO GUEST APP");
        try {
            SceneManager.loadScene("../view/guest-overview.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * exit the software
     */
    @FXML
    private void exit() {
        System.exit(0);
    }


}
