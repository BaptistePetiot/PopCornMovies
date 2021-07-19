package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.Cinema;
import model.Me;
import model.SceneManager;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;

public class ControllerLogin{
    //private Cinema cinema;

    //private FXMLLoader loaderMember = new FXMLLoader(getClass().getResource("view/login.fxml"));
    //private FXMLLoader loaderGuest = new FXMLLoader(getClass().getResource("view/guest.fxml"));
    //private FXMLLoader loaderSignup = new FXMLLoader(getClass().getResource("view/signup.fxml"));
    //private FXMLLoader loaderBaseCustomer = new FXMLLoader(getClass().getResource("view/base_customer.fxml"));
    private double x, y;
    private boolean isEmployee;
    private String date;

    @FXML Button buttonMember, buttonGuest, buttonSignup, buttonExit, buttonCustomer, buttonEmployee, buttonBack, buttonLogin;
    @FXML AnchorPane anchorPane;
    @FXML TextField emailLogin, emailSignup, lastName, firstName;
    @FXML PasswordField passwordLogin, passwordSignup;
    @FXML Label firstNameAndLastName;

    // credentials
    private final String url       = "jdbc:mysql://localhost:3306/popcornmovie";
    private final String user      = "root";
    private final String password  = "";

    public ControllerLogin() {
        this.isEmployee = false;
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date(System.currentTimeMillis());
        this.date = formatter.format(now);
    }

    @FXML
    public void handleEnterLogin(KeyEvent keyEvent) throws IOException {
        if( keyEvent.getCode() == KeyCode.ENTER ) {
            login();
        }
    }

    @FXML
    private void login() throws IOException{
        // connect to DB
        Connection connection = null;
        boolean emailOK = false, passwordOK = false;

        try {
            // create a connection to the database
            connection = DriverManager.getConnection(url, user, password);

            // statement
            Statement stmt=connection.createStatement();
            ResultSet rs;

            // check email
            String email = emailLogin.getText();
            String clearPassword = passwordLogin.getText();
            if(email.contains("@") && email.contains(".")){ // check email format
                rs=stmt.executeQuery("SELECT `Email` FROM logins");
                while(rs.next()){
                    String s = rs.getString(1);
                    if(s.equals(email)){    //check email is in DB
                        emailOK = true;
                    }
                }
            }
            if(!emailOK){
                System.out.println("Email invalid or unknown");
            }else{
                // check password
                rs=stmt.executeQuery("SELECT `Password` FROM logins WHERE Email = '" + email + "';");
                while(rs.next()){
                    String s = rs.getString(1);
                    if(s.equals(clearPassword)){
                        passwordOK = true;
                    }
                }
            }
            if(!passwordOK){
                System.out.println("Password incorrect or unregistered");
            }

            if(emailOK && passwordOK){
                // detect whether the user connecting is a customer or an employee
                boolean isCustomer = true;
                int nbrRows = 0;
                rs = stmt.executeQuery("SELECT * FROM Customers WHERE IdLogins = (SELECT `Id` FROM logins WHERE Email = '" + email + "'AND Password = '" + clearPassword + "');");

                int customerId = 0;
                String customerLastName, customerFirstName;
                while(rs.next()){
                    customerId = rs.getInt("IdLogins");
                    customerLastName = rs.getString("LastName");
                    customerFirstName = rs.getString("FirstName");
                    Me.setId(customerId);
                    Me.setFirstName(customerFirstName);
                    Me.setLastName(customerLastName);
                    nbrRows++;
                }
                // remember preferred theme
                rs = stmt.executeQuery("SELECT themeNbr FROM Theme WHERE IdLogins = " + customerId + ";");
                int themeNbr = 0;
                while(rs.next()){
                    themeNbr = rs.getInt("themeNbr");
                }

                // remember category
                rs = stmt.executeQuery("SELECT categoryNbr FROM Category WHERE IdLogins = " + customerId + ";");
                int categoryNbr = 0;
                while(rs.next()){
                    categoryNbr = rs.getInt("categoryNbr");
                }

                if(nbrRows == 0){
                    rs = stmt.executeQuery("SELECT * FROM Employees WHERE IdLogins = (SELECT `Id` FROM logins WHERE Email = '" + email + "'AND Password = '" + clearPassword + "');");
                    int employeeId = 0;
                    String employeeLastName, employeeFirstName;
                    while(rs.next()){
                        employeeId = rs.getInt("IdLogins");
                        employeeLastName = rs.getString("LastName");
                        employeeFirstName = rs.getString("FirstName");
                        Me.setId(employeeId);
                        Me.setFirstName(employeeFirstName);
                        Me.setLastName(employeeLastName);
                        nbrRows++;
                    }
                    isCustomer = false;
                    
                    // remember preferred theme
                    rs = stmt.executeQuery("SELECT themeNbr FROM Theme WHERE IdLogins = " + employeeId + ";");
                    while(rs.next()){
                        themeNbr = rs.getInt("themeNbr");
                    }

                    // remember category
                    rs = stmt.executeQuery("SELECT categoryNbr FROM Category WHERE IdLogins = " + employeeId + ";");
                    while(rs.next()){
                        categoryNbr = rs.getInt("categoryNbr");
                    }
                }
                
                Me.setTheme(themeNbr);
                Me.setCategory(categoryNbr);

                if(isCustomer){
                    goToCustomerApp();

                }else{
                    goToEmployeeApp();
                }
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
            // add popup
        } finally {
            try{
                if(connection != null)
                    connection.close();
            }catch(SQLException ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    @FXML
    public void handleEnterSignup(KeyEvent keyEvent){
        if( keyEvent.getCode() == KeyCode.ENTER ) {
            signup();
        }
    }

    @FXML private void signup(){
        // connect to DB
        Connection connection = null;
        boolean emailOK = false;

        try {
            // create a connection to the database
            connection = DriverManager.getConnection(url, user, password);

            // statement
            Statement stmt=connection.createStatement();
            ResultSet rs;

            // check email
            String email = emailSignup.getText();
            if(email.contains("@") && email.contains(".")){ // check email format
                rs=stmt.executeQuery("SELECT `Email` FROM logins");
                emailOK = true;
                while(rs.next()){
                    String s = rs.getString(1);
                    if(s.equals(email)){    //check email is not already in DB
                        emailOK = false;
                        break;
                    }
                }
            }
            if(!emailOK){
                System.out.println("Email invalid or already registered");
            }

            if(emailOK){
                // getting Id Max
                rs = stmt.executeQuery("SELECT MAX(`Id`) FROM `Logins`");

                int maxId = 0;
                while(rs.next()){
                    maxId = rs.getInt(1);
                }

                // inserting row in login table
                String sqlINSERTStatement = "INSERT INTO `logins` (`Id`, `Email`, `Password`) VALUES (" + maxId+1 + ", '"  + emailSignup.getText() + "', '" + passwordSignup.getText() + "');";
                stmt.executeUpdate(sqlINSERTStatement);

                if(isEmployee){
                    // inserting row in employee table
                    sqlINSERTStatement = "INSERT INTO `Employees` (`IdLogins`, `LastName`, `Firstname`, `DateOfCreation`) VALUES (" + maxId+1 + ", '"  + lastName.getText() + "', '" + firstName.getText() + "', '" + date + "');";
                    stmt.executeUpdate(sqlINSERTStatement);

                    // inserting row in theme table
                    sqlINSERTStatement = "INSERT INTO `Theme` (`IdLogins`, `themeNbr`) VALUES (" + maxId+1 + ", '0');";
                    stmt.executeUpdate(sqlINSERTStatement);

                    // inserting row in category table
                    sqlINSERTStatement = "INSERT INTO `Category` (`IdLogins`, `categoryNbr`) VALUES (" + maxId+1 + ", '0');";
                    stmt.executeUpdate(sqlINSERTStatement);

                    // updating the array lists
                    Cinema.refresh();
                }else{
                    // inserting row in customer table
                    sqlINSERTStatement = "INSERT INTO `Customers` (`IdLogins`, `LastName`, `Firstname`, `DateOfCreation`) VALUES (" + maxId+1 + ", '"  + lastName.getText() + "', '" + firstName.getText() + "', '" + date + "');";
                    stmt.executeUpdate(sqlINSERTStatement);

                    // inserting row in theme table
                    sqlINSERTStatement = "INSERT INTO `Theme` (`IdLogins`, `themeNbr`) VALUES (" + maxId+1 + ", '0');";
                    stmt.executeUpdate(sqlINSERTStatement);

                    // inserting row in category table
                    sqlINSERTStatement = "INSERT INTO `Category` (`IdLogins`, `categoryNbr`) VALUES (" + maxId+1 + ", '0');";
                    stmt.executeUpdate(sqlINSERTStatement);

                    // updating the array lists
                    Cinema.refresh();
                }

                System.out.println("Correctly registered");
                goBackToMember();
            }

        } catch(SQLException | IOException e) {
            System.out.println(e.getMessage());
            // add popup
        } finally {
            try{
                if(connection != null)
                    connection.close();
            }catch(SQLException ex){
                System.out.println(ex.getMessage());
            }
        }


    }

    @FXML
    private void changeToCustomer(){
        buttonEmployee.setStyle("-fx-background-color: #9C2B27; -fx-border-color: transparent; -fx-cursor: hand;");
        buttonCustomer.setStyle("-fx-background-color: #9C2B27; -fx-border-color: #ffffff; -fx-cursor: hand;");
        this.isEmployee = false;
    }

    @FXML
    private void changeToEmployee(){
        buttonCustomer.setStyle("-fx-background-color: #9C2B27; -fx-border-color: transparent; -fx-cursor: hand;");
        buttonEmployee.setStyle("-fx-background-color: #9C2B27; -fx-border-color: #ffffff; -fx-cursor: hand;");
        this.isEmployee = true;
    }

    @FXML
    protected void goToGuest() throws IOException {
        //super.goToGuest();
        System.out.println("GUEST");
        try{
            SceneManager.loadScene("../view/guest.fxml", 700, 400);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    protected void goToMember() throws IOException {
        //super.goToMember();
        System.out.println("MEMBER");
        try{
            SceneManager.loadScene("../view/login.fxml", 700, 400);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void goBackToMember() throws IOException {
        //super.goToMember();
        goToMember();
    }

    @FXML
    protected void goToSignup() throws IOException {
        //super.goToSignup();
        System.out.println("SIGNUP");
        try{
            SceneManager.loadScene("../view/signup.fxml", 700, 400);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void exit(){
        System.exit(0);
    }

    protected void goToCustomerApp() {
        System.out.println("GO TO CUSTOMER APP");
        System.out.println(Me.getId() + " / " + Me.getLastName() + " / " + Me.getFirstName());

        try{
            SceneManager.loadScene("../view/customer-overview.fxml", 1400, 800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    protected void goToEmployeeApp() {
        System.out.println("GO TO EMPLOYEE APP");
        System.out.println(Me.getId() + " / " + Me.getLastName() + " / " + Me.getFirstName());

        try{
            SceneManager.loadScene("../view/employee-overview.fxml", 1400, 800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
