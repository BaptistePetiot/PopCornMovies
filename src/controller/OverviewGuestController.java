package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import model.Cinema;
import model.Me;
import model.Movie;
import model.SceneManager;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller of the OVERVIEW scene of the GUEST application
 */
public class OverviewGuestController implements Initializable {
    // credentials
    private final String url = "jdbc:mysql://localhost:3306/popcornmovie";
    private final String user = "root";
    private final String password = "";

    // Javafx elements
    @FXML
    ImageView movie1, movie2;
    @FXML
    Pane pane;

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
     * displays 2 most seen movies
     * displays the active discount with the higher amount
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
        } else if (Me.getTheme() == 1) {
            pane.getStylesheets().remove("css/LightTheme.css");
            pane.getStylesheets().add("css/DarkTheme.css");
        }

        // 2 MOST SEEN MOVIES
        try {
            // create a connection to the database
            Connection connection = DriverManager.getConnection(url, user, password);
            // statement
            Statement stmt = connection.createStatement();

            // get the 1st most seen movie
            ResultSet rs = stmt.executeQuery("SELECT `NbrTickets`, Title, IdMovies, COUNT(*) AS count FROM Purchases GROUP BY NbrTickets ORDER BY count DESC;");
            // SELECT * FROM `purchases` ORDER BY Title
            int id1 = 0, id2 = 0;
            int j = 0;
            while (rs.next()) {
                if (j == 0) {
                    id1 = rs.getInt("IdMovies");
                } else if (j == 1) {
                    id2 = rs.getInt("IdMovies");
                }
                j++;
            }

            Cinema.refresh();
            ArrayList<Movie> movies = Cinema.getMovies();
            for (Movie m : movies) {
                if (m.getId() == id1) {
                    Image im = new Image(m.getImageURL());
                    movie1.setImage(im);
                } else if (m.getId() == id2) {
                    Image im = new Image(m.getImageURL());
                    movie2.setImage(im);
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
