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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class SettingsGuestController implements Initializable {
    @FXML RadioButton light, dark;
    @FXML Pane pane;
    @FXML ToggleGroup themeGroup;

    public void goToOverview(ActionEvent actionEvent) {
        System.out.println("OVERVIEW GUEST");
        try{
            SceneManager.loadScene("../view/guest-overview.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToMovies(ActionEvent actionEvent) {
        System.out.println("MOVIES GUEST");
        try{
            SceneManager.loadScene("../view/guest-movies.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToPurchases(ActionEvent actionEvent) {
        System.out.println("PURCHASES GUEST");
        try{
            SceneManager.loadScene("../view/guest-purchases.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToAccount(ActionEvent actionEvent) {
        System.out.println("SETTINGS GUEST");
        try{
            SceneManager.loadScene("../view/guest-settings.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void signout() {
        System.out.println("EXIT");
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

            light.setSelected(true);
        }else if(Me.getTheme() == 1){
            pane.getStylesheets().remove("css/LightTheme.css");
            pane.getStylesheets().add("css/DarkTheme.css");

            dark.setSelected(true);
        }

        // theme change
        themeGroup = new ToggleGroup();
        light.setToggleGroup(themeGroup);
        dark.setToggleGroup(themeGroup);

        themeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                RadioButton btn = (RadioButton) themeGroup.getSelectedToggle();
                System.out.println(btn.getText());

                    switch (btn.getText()) {
                        case ("Light"):
                            // update theme choice in Me for current connection
                            Me.setTheme(0);

                            pane.getStylesheets().remove("css/DarkTheme.css");
                            pane.getStylesheets().add("css/LightTheme.css");
                            break;

                        case ("Dark"):
                            // update theme choice in Me for current connection
                            Me.setTheme(1);

                            pane.getStylesheets().remove("css/LightTheme.css");
                            pane.getStylesheets().add("css/DarkTheme.css");
                            break;

                    }

                }

        });

    }
}
