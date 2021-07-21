package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import main.PopCornMovie;
import model.Me;
import model.SceneManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AccountEmployeeController implements Initializable {

    @FXML ImageView picture;
    @FXML Label firstNameAndLastName;
    @FXML RadioButton regular, child, senior, light, dark;
    @FXML Pane pane;
    @FXML ToggleGroup categoryGroup, themeGroup;

    // credentials
    private final String url       = "jdbc:mysql://localhost:3306/popcornmovie";
    private final String user      = "root";
    private final String password  = "";

    @FXML
    protected void addPicture(){
        System.out.println("ADD PICTURE" );
        // DIALOG TO CHOOSE PICTURE
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"));
        File file = fileChooser.showOpenDialog(PopCornMovie.getStage());

        // SAVE PICTURE
        try{
            BufferedImage bImage = ImageIO.read(file);
            File img = new File("picture.jpg");
            ImageIO.write(bImage, "jpg", img);

            InputStream is = new FileInputStream(img);

            // create a connection to the database
            Connection connection = DriverManager.getConnection(url, user, password);

            // CHECK IF THE USER ALREADY HAS A PROFILE IMAGE
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT picture FROM Pictures WHERE IdLogins = " + Me.getId());
            if(rs.next()){
                System.out.println("already has an image");
                // change image
                PreparedStatement ps = connection.prepareStatement("UPDATE `Pictures` SET picture=? WHERE IdLogins=?;");
                ps.setBlob(1, is);
                ps.setInt(2, Me.getId());
                ps.executeUpdate();
            }else{
                System.out.println("no image yet");
                // add new image
                PreparedStatement ps = connection.prepareStatement("INSERT INTO `Pictures` (`IdLogins`, `picture`) VALUES (?,?);");
                ps.setInt(1, Me.getId());
                ps.setBlob(2, is);
                ps.executeUpdate();
            }

            img.delete();   // TODO: does not seem to work

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        try{
            loadPicture();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    private void loadPicture() throws Exception{
        File img = new File("picture.jpg");
        FileOutputStream ostreamImage = new FileOutputStream(img);

        try{
            // create a connection to the database
            Connection connection = DriverManager.getConnection(url, user, password);
            // prepared statement
            PreparedStatement ps = connection.prepareStatement("SELECT picture FROM pictures WHERE IdLogins=?");

            try{
                ps.setInt(1,Me.getId());
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
                        img.delete();   // TODO: does not seem to work
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

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // load picture
        try {
            loadPicture();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // theme
        if (Me.getTheme() == 0) {
            pane.getStylesheets().remove("css/DarkTheme.css");
            pane.getStylesheets().add("css/LightTheme.css");

            light.setSelected(true);
        } else if (Me.getTheme() == 1) {
            pane.getStylesheets().remove("css/LightTheme.css");
            pane.getStylesheets().add("css/DarkTheme.css");

            dark.setSelected(true);
        }

        // category
        if (Me.getCategory() == 0) {
            regular.setSelected(true);
        } else if (Me.getCategory() == 1) {
            senior.setSelected(true);
        } else if (Me.getCategory() == 2) {
            child.setSelected(true);
        }

        firstNameAndLastName.setText(Me.getFirstName() + " " + Me.getLastName());

        // category change
        categoryGroup = new ToggleGroup();
        regular.setToggleGroup(categoryGroup);
        senior.setToggleGroup(categoryGroup);
        child.setToggleGroup(categoryGroup);

        categoryGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                RadioButton btn = (RadioButton) categoryGroup.getSelectedToggle();
                System.out.println(btn.getText());

                // connect to DB
                Connection connection = null;
                try {
                    // create a connection to the database
                    connection = DriverManager.getConnection(url, user, password);

                    // statement
                    Statement stmt = connection.createStatement();

                    switch (btn.getText()) {
                        case ("Regular"):
                            // update theme choice in DB for next connection
                            stmt.executeUpdate("UPDATE category SET categoryNbr=0  WHERE IdLogins = " + Me.getId());
                            // update theme choice in Me for current connection
                            Me.setCategory(0);
                            break;

                        case ("Senior"):
                            // update theme choice in DB for next connection
                            stmt.executeUpdate("UPDATE category SET categoryNbr=1  WHERE IdLogins = " + Me.getId());
                            // update theme choice in Me for current connection
                            Me.setCategory(1);
                            break;

                        case ("Child"):
                            // update theme choice in DB for next connection
                            stmt.executeUpdate("UPDATE category SET categoryNbr=2  WHERE IdLogins = " + Me.getId());
                            // update theme choice in Me for current connection
                            Me.setCategory(2);
                            break;
                    }

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        });

        // theme change
        themeGroup = new ToggleGroup();
        light.setToggleGroup(themeGroup);
        dark.setToggleGroup(themeGroup);

        themeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                RadioButton btn = (RadioButton) themeGroup.getSelectedToggle();
                System.out.println(btn.getText());

                // connect to DB
                Connection connection = null;
                try {
                    // create a connection to the database
                    connection = DriverManager.getConnection(url, user, password);

                    // statement
                    Statement stmt = connection.createStatement();

                    switch (btn.getText()) {
                        case ("Light"):
                            // update theme choice in DB for next connection
                            stmt.executeUpdate("UPDATE theme SET themeNbr=0  WHERE IdLogins = " + Me.getId());
                            // update theme choice in Me for current connection
                            Me.setTheme(0);

                            pane.getStylesheets().remove("css/DarkTheme.css");
                            pane.getStylesheets().add("css/LightTheme.css");
                            break;

                        case ("Dark"):
                            // update theme choice in DB for next connection
                            stmt.executeUpdate("UPDATE theme SET themeNbr=1  WHERE IdLogins = " + Me.getId());
                            // update theme choice in Me for current connection
                            Me.setTheme(1);

                            pane.getStylesheets().remove("css/LightTheme.css");
                            pane.getStylesheets().add("css/DarkTheme.css");
                            break;

                    }

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }
}
