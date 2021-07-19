package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

    public void signout() {
        System.out.println("SIGN OUT");
        try{
            SceneManager.loadScene("../view/login.fxml", 700,400);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void delete(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete your account ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            // connect to DB
            Connection connection = null;
            try {
                // create a connection to the database
                connection = DriverManager.getConnection(url, user, password);

                // statement
                Statement stmt = connection.createStatement();

                // delete account in logins but nowhere else therefore account is deleted but history is kept for the company
                stmt.executeUpdate("DELETE FROM logins WHERE Id = " + Me.getId());
                signout();

            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
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

            light.setSelected(true);
        }else if(Me.getTheme() == 1){
            pane.getStylesheets().remove("css/LightTheme.css");
            pane.getStylesheets().add("css/DarkTheme.css");

            dark.setSelected(true);
        }

        // category
        if(Me.getCategory() == 0){
            regular.setSelected(true);
        }else if(Me.getCategory() == 1){
            senior.setSelected(true);
        }else if(Me.getCategory() == 2){
            child.setSelected(true);
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

                }catch (SQLException e){
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

                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }
            }
        });

    }


}
