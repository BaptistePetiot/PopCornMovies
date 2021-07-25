package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import model.Cinema;
import model.Me;
import model.Movie;
import model.SceneManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OverviewGuestController implements Initializable {

    @FXML
    ImageView movie1, movie2;
    @FXML
    Pane pane;

    // credentials
    private final String url = "jdbc:mysql://localhost:3306/popcornmovie";
    private final String user = "root";
    private final String password = "";

    public void goToOverview(ActionEvent actionEvent) {
        System.out.println("OVERVIEW GUEST");
        try {
            SceneManager.loadScene("../view/guest-overview.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void goToMovies(ActionEvent actionEvent) {
        System.out.println("MOVIES GUEST");
        try {
            SceneManager.loadScene("../view/guest-movies.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void goToSettings(ActionEvent actionEvent) {
        System.out.println("SETTINGS GUEST");
        try {
            SceneManager.loadScene("../view/guest-settings.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void signout(ActionEvent actionEvent) {
        System.out.println("EXIT");
        try {
            SceneManager.loadScene("../view/login.fxml", 700, 400);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void exit() {
        System.exit(0);
    }

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
