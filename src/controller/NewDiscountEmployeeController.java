package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import model.Me;
import model.SceneManager;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class NewDiscountEmployeeController implements Initializable {
    @FXML ImageView picture;
    @FXML Label firstNameAndLastName;
    @FXML Pane pane;
    @FXML TextField newDiscountName, newDiscountAmount, newDiscountStatus;
    @FXML ToggleGroup categoryGroup;
    @FXML RadioButton percent, livreStearling;

    // credentials
    private final String url       = "jdbc:mysql://localhost:3306/popcornmovie";
    private final String user      = "root";
    private final String password  = "";

    private void loadPicture() throws Exception{
        File img = new File("picture.jpg");
        FileOutputStream ostreamImage = new FileOutputStream(img);

        try{
            // create a connection to the database
            Connection connection = DriverManager.getConnection(url, user, password);
            // prepared statement
            PreparedStatement ps = connection.prepareStatement("SELECT picture FROM pictures WHERE IdLogins=?");

            try{
                ps.setInt(1, Me.getId());
                ResultSet rs = ps.executeQuery();

                try{
                    if(rs.next()){
                        InputStream istreamImage = rs.getBinaryStream("picture");

                        byte[] buffer = new byte[1024];
                        int length = 0;

                        while((length = istreamImage.read(buffer)) != -1){
                            ostreamImage.write(buffer, 0, length);  // save image locally
                        }

                        // set image
                        Image image = new Image(img.toURI().toString());
                        picture.setImage(image);
                    }
                }
                finally{
                    rs.close();
                }
            }
            finally{
                ps.close();
            }
        }
        finally{
            ostreamImage.close();
        }
    }

    @FXML private void addDiscount(){
        System.out.println("ADD DISCOUNT");


        try {
            Connection connection = null;

            // create a connection to the database
            connection = DriverManager.getConnection(url, user, password);

            // statement
            Statement stmt=connection.createStatement();
            ResultSet rs;

            String choosenUnit = "";
            if (percent.isSelected()) {
                choosenUnit = "%";
            } else if (livreStearling.isSelected()){
                choosenUnit = "£";
            }

            rs = stmt.executeQuery("SELECT MAX(`Id`) FROM `Discounts`");

            int maxId = 0;
            while(rs.next()){
                maxId = rs.getInt(1);
            }
            int nextId = maxId+1;
            String sqlINSERTStatement = "INSERT INTO `Discounts` (`Id`, `Name`, `Amount`, `Unit`,`Status`) VALUES (" + nextId + ", '"  + newDiscountName.getText() + "', '" + newDiscountAmount.getText() + "', '" + choosenUnit + "', '" + newDiscountStatus.getText() + "');";
            stmt.executeUpdate(sqlINSERTStatement);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void goToOverview(ActionEvent actionEvent) {
        System.out.println("OVERVIEW EMPLOYEE");
        try{
            SceneManager.loadScene("../view/employee-overview.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToMovies(ActionEvent actionEvent) {
        System.out.println("MOVIES EMPLOYEE");
        try{
            SceneManager.loadScene("../view/employee-movies.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToDiscounts(ActionEvent actionEvent) {
        System.out.println("DISCOUNTS EMPLOYEE");
        try{
            SceneManager.loadScene("../view/employee-discounts.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToRecords(ActionEvent actionEvent) {
        System.out.println("RECORDS EMPLOYEE");
        try{
            SceneManager.loadScene("../view/employee-records.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToStatistics(ActionEvent actionEvent) {
        System.out.println("STATISTICS EMPLOYEE");
        try{
            SceneManager.loadScene("../view/employee-statistics.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToPurchases(ActionEvent actionEvent) {
        System.out.println("PURCHASES EMPLOYEE");
        try{
            SceneManager.loadScene("../view/employee-purchases.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToAccount(ActionEvent actionEvent) {
        System.out.println("ACCOUNT EMPLOYEE");
        try{
            SceneManager.loadScene("../view/employee-account.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void signout(ActionEvent actionEvent) {
        System.out.println("SIGN OUT");
        try{
            SceneManager.loadScene("../view/login.fxml", 700,400);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void exit(ActionEvent actionEvent) { System.exit(0); }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // load picture
        try {
            loadPicture();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // theme
        if(Me.getTheme() == 0){
            pane.getStylesheets().remove("css/DarkTheme.css");
            pane.getStylesheets().add("css/LightTheme.css");
        }else if(Me.getTheme() == 1){
            pane.getStylesheets().remove("css/LightTheme.css");
            pane.getStylesheets().add("css/DarkTheme.css");
        }

        firstNameAndLastName.setText(Me.getFirstName() + " " + Me.getLastName());

        categoryGroup = new ToggleGroup();
        percent.setToggleGroup(categoryGroup);
        livreStearling.setToggleGroup(categoryGroup);

    }
}