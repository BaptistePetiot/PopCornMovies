package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import model.Me;
import model.SceneManager;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller of the SETTINGS scene of the GUEST application
 * @author Baptiste Petiot
 */
public class SettingsGuestController implements Initializable {
    // Javafx elements
    @FXML
    RadioButton light, dark;
    @FXML
    Pane pane;
    @FXML
    ToggleGroup themeGroup;

    // NAVIGATION

    /***
     * function that loads the OVERVIEW scene of GUEST application
     * scene that displays the 2 most attractive movies of the moment
     * and the most interesting discount that is currently active
     */
    public void goToOverview() {
        System.out.println("OVERVIEW GUEST");
        try {
            SceneManager.loadScene("../view/guest-overview.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the MOVIES scene of GUEST application
     * displays the list of movies available depending on their genre
     */
    public void goToMovies() {
        System.out.println("MOVIES GUEST");
        try {
            SceneManager.loadScene("../view/guest-movies.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the SETTINGS scene of EMPLOYEE application
     * lets the user select the theme of his choice (light or dark)
     */
    public void goToSettings() {
        System.out.println("SETTINGS GUEST");
        try {
            SceneManager.loadScene("../view/guest-settings.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that signs the user out and loads the LOGIN scene
     */
    public void signout() {
        System.out.println("EXIT");
        try {
            SceneManager.loadScene("../view/login.fxml", 700, 400);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * exit the GUEST application
     */
    @FXML
    private void exit() {
        System.exit(0);
    }

    /***
     * first method called for initialization
     * sets chosen theme
     * displays theme choice
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
