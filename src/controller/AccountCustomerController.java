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
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AccountCustomerController implements Initializable {

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
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG", "*.png"));
        File file = fileChooser.showOpenDialog(PopCornMovie.getStage());

        // SAVE PICTURE
        try{
            BufferedImage bImage = ImageIO.read(file);
            ImageIO.write(bImage, "png", new File("picture_" + Me.getId() + ".png"));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        // RETRIEVE PICTURE AND SET IT IN ACCOUNT ImageView
        try{
            file = new File("picture" + Me.getId() + ".png");
            Image image = new Image(file.toURI().toString());
            picture.setImage(image);                            //TODO : make it work!
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToOverview(ActionEvent actionEvent) {
        System.out.println("OVERVIEW CUSTOMER");
        try{
            SceneManager.loadScene("../view/customer-overview.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToMovies(ActionEvent actionEvent) {
        System.out.println("MOVIES CUSTOMER");
        try{
            SceneManager.loadScene("../view/customer-movies.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToPurchases(ActionEvent actionEvent) {
        System.out.println("PURCHASES CUSTOMER");
        try{
            SceneManager.loadScene("../view/customer-purchases.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToAccount(ActionEvent actionEvent) {
        System.out.println("ACCOUNT CUSTOMER");
        try{
            SceneManager.loadScene("../view/customer-account.fxml", 1400,800);
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

    @FXML
    private void exit(){
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // theme
        if(Me.getTheme() == 0){
            pane.getStylesheets().remove("css/DarkTheme.css");
            pane.getStylesheets().add("css/LightTheme.css");
        }else if(Me.getTheme() == 1){
            pane.getStylesheets().remove("css/LightTheme.css");
            pane.getStylesheets().add("css/DarkTheme.css");
        }

        // picture
        File file = new File("picture" + Me.getId() + ".png");
        if(file.exists()){
            System.out.println("image exists");
            Image image = new Image(file.toURI().toString());
            picture.setImage(image);
        }else{
            System.out.println("image does not exist");
            picture.setImage(new Image(new File("imgs/circle.png").toURI().toString()));
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
                switch (btn.getText()){
                    case("Regular"):
                        Me.setCategory(0);
                        break;

                    case("Senior"):
                        Me.setCategory(1);
                        break;

                    case("Child"):
                        Me.setCategory(2);
                        break;
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
                    ResultSet rs;

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
                            stmt.executeUpdate("UPDATE theme SET themeNbr=0  WHERE IdLogins = " + Me.getId());
                            // update theme choice in Me for current connection
                            Me.setTheme(1);

                            pane.getStylesheets().remove("css/LightTheme.css");
                            pane.getStylesheets().add("css/DarkTheme.css");
                            break;

                    }

                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }
            }
        });

    }
}
