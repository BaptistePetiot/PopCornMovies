package controller;

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

/**
 * Controller of the OVERVIEW scene of the CUSTOMER application
 * @author Baptiste Petiot
 */
public class OverviewCustomerController implements Initializable {
    // credentials
    private final String url = "jdbc:mysql://localhost:3306/popcornmovie";
    private final String user = "root";
    private final String password = "";

    // Javafx elements
    @FXML
    ImageView picture, movie1, movie2;
    @FXML
    Label firstNameAndLastName, discountAlert;
    @FXML
    Pane pane;

    /***
     * loads the user picture in the dedicated ImageView
     */
    private void loadPicture() {
        try {
            File img = new File("picture.jpg");
            FileOutputStream ostreamImage = new FileOutputStream(img);

            // create a connection to the database
            Connection connection = DriverManager.getConnection(url, user, password);
            // prepared statement
            PreparedStatement ps = connection.prepareStatement("SELECT picture FROM pictures WHERE IdLogins=?");

            try {
                ps.setInt(1, Me.getId());
                ResultSet rs = ps.executeQuery();

                try {
                    if (rs.next()) {
                        InputStream istreamImage = rs.getBinaryStream("picture");

                        byte[] buffer = new byte[1024];
                        int length = 0;

                        while ((length = istreamImage.read(buffer)) != -1) {
                            ostreamImage.write(buffer, 0, length);  // save image locally
                        }

                        // set image
                        Image image = new Image(img.toURI().toString());
                        picture.setImage(image);
                    }
                } finally {
                    rs.close();
                }
            } finally {
                ostreamImage.close();
                ps.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // NAVIGATION

    /***
     * function that loads the OVERVIEW scene of CUSTOMER application
     * scene that displays the 2 most attractive movies of the moment
     * and the most interesting discount that is currently active
     */
    public void goToOverview() {
        System.out.println("OVERVIEW CUSTOMER");
        try {
            SceneManager.loadScene("../view/customer-overview.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the MOVIES scene of CUSTOMER application
     * displays the list of movies available depending on their genre
     */
    public void goToMovies() {
        System.out.println("MOVIES CUSTOMER");
        try {
            SceneManager.loadScene("../view/customer-movies.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the PURCHASES scene of CUSTOMER application
     * displays the total number of tickets bought, the number of tickets bought in the last 12 months and in the current month
     * displays all purchases of the user in a scrollable area
     */
    public void goToPurchases() {
        System.out.println("PURCHASES CUSTOMER");
        try {
            SceneManager.loadScene("../view/customer-purchases.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the ACCOUNT scene of CUSTOMER application
     * displays the date of creation of the account
     * lets the user select the appropriate category for the account (regular, senior or child)
     * lets the user select the theme of his choice (light or dark)
     * lets the user add a picture or change the current one
     * lets the user delete the account
     *//***
     * function that signs the user out and loads the LOGIN scene
     */
    public void goToAccount() {
        System.out.println("ACCOUNT CUSTOMER");
        try {
            SceneManager.loadScene("../view/customer-account.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that signs the user out and loads the LOGIN scene
     */
    public void signout() {
        System.out.println("SIGN OUT");
        try {
            SceneManager.loadScene("../view/login.fxml", 700, 400);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * exit the CUSTOMER application
     */
    @FXML
    private void exit() {
        System.exit(0);
    }

    /***
     * first method called for initialization
     * loads user picture
     * sets chosen theme
     * retrieves and displays the 2 most seen movies
     * retrieves and displays the active discount that has the highest amount
     *
     * @param location : URL
     * @param resources : ResourceBundle
     */
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
        } else if (Me.getTheme() == 1) {
            pane.getStylesheets().remove("css/LightTheme.css");
            pane.getStylesheets().add("css/DarkTheme.css");
        }

        firstNameAndLastName.setText(Me.getFirstName() + " " + Me.getLastName());

        // 2 MOST SEEN MOVIES
        try {
            // create a connection to the database
            Connection connection = DriverManager.getConnection(url, user, password);
            // statement
            Statement stmt = connection.createStatement();

            // get the 1st most seen movie
            ResultSet rs = stmt.executeQuery("SELECT IdMovies, SUM(NbrTickets) AS s FROM Purchases GROUP BY IdMovies ORDER BY s DESC");
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

            // DISCOUNT ALERT
            rs = stmt.executeQuery("SELECT Name, Amount, Unit FROM Discounts WHERE Status = 'Active' ORDER BY Amount DESC");
            String name = "", amount = "", unit = "";
            if (rs.next()) {
                name = rs.getString("Name");
                amount = rs.getString("Amount");
                unit = rs.getString("Unit");

                discountAlert.setVisible(true);
                discountAlert.setText("-" + amount + " " + unit + " " + name + " discount");
            } else {
                discountAlert.setVisible(false);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
