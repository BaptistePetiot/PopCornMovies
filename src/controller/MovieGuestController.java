package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import model.*;

import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller of the MOVIE scene of the GUEST application
 * @author Baptiste Petiot
 */
public class MovieGuestController implements Initializable, Consts {
    // credentials
    private final String url = "jdbc:mysql://localhost:3306/popcornmovie";
    private final String user = "root";
    private final String password = "";

    // Javafx elements
    @FXML
    ImageView ivMovie;
    @FXML
    Label labelTitle, labelGenre, labelDirector, labelCast, labelPlot, price, duration;
    @FXML
    Pane pane;
    @FXML
    TextField tfNbrTickets;

    // class attributes
    private int cost = 0;
    private String date;
    private int nbrTickets;

    public MovieGuestController() {
        // handle current date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date(System.currentTimeMillis());
        this.date = formatter.format(now);
    }

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
     * function that verify that number of tickets and number of student discounts are not empty,
     * computes the price and displays it if the user presses ENTER,
     * registers the purchase,
     * sends the tickets by email
     * and loads the PAYMENT scene of GUEST application
     */
    public void goToPayment() {
            try{
                computePrice();
                registerPurchase();

                // send tickets by email
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Email");
                dialog.setHeaderText("Enter your email to receive your tickets, don't worry it won't be stored in the database.");
                dialog.setContentText("Email, (the right one or you will lose your tickets) :");
                Optional<String> result = dialog.showAndWait();

                result.ifPresent(email -> {
                    Me.setEmail(email);
                });

                MailSender.sendTickets(Me.getEmail(), Me.getFirstName(), Me.getLookingAtMovie().getTitle(), nbrTickets);

                System.out.println("PAYMENT GUEST");
                SceneManager.loadScene("../view/guest-payment.fxml", 959, 262);
            }
             catch (Exception e) {
                 System.out.println("The field is empty!");
                 Alert alert = new Alert(Alert.AlertType.INFORMATION);
                 alert.setTitle("Problem");
                 alert.setHeaderText("Cannot currently buy the tickets");
                 alert.setContentText("Please check that the field is not empty and that what you typed in is an actual number.");
                 alert.showAndWait();

                System.out.println(e.getMessage());
            }

    }

    /***
     * insert the purchase into the DB
     */
    private void registerPurchase() {
        // connect to DB
        Connection connection = null;

        try {
            // create a connection to the database
            connection = DriverManager.getConnection(url, user, password);

            // statement
            Statement stmt = connection.createStatement();
            ResultSet rs;

            // retrieve highest id
            rs = stmt.executeQuery("SELECT MAX(Id) FROM `purchases`");
            int maxId = 0;
            while (rs.next()) {
                maxId = rs.getInt(1);
            }
            int purchaseId = maxId + 1;

            // add purchase to DB
            stmt.executeUpdate("INSERT INTO `purchases` (`Id`, `IdLogins`, `IdMovies`, `Title`, `NbrTickets`, `Date`, `Price`) VALUES ('  " + purchaseId + "', '0', ' " + Me.getLookingAtMovie().getId() + "', ' " + Me.getLookingAtMovie().getTitle() + "', '" + nbrTickets + "', '" + date + "', '" + cost + "');");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * computes the full cost based on the number of normal tickets and the number of student discounts
     * NO DISCOUNTS AS GUEST
     */
    private void computePrice() {
        nbrTickets = Integer.parseInt(tfNbrTickets.getText());

        cost = nbrTickets * Consts.NORMAL_PRICE;
    }

    /***
     * displays the computed price if the user presses ENTER right after having typed in the nbr of student discounts
     * @param keyEvent
     */
    public void displayPrice(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            computePrice();
            price.setText(String.valueOf(cost));
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
     * displays movie information according to the selected movie
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        price.setText("Press enter to see the computer price");

        // theme
        if (Me.getTheme() == 0) {
            pane.getStylesheets().remove("css/DarkTheme.css");
            pane.getStylesheets().add("css/LightTheme.css");
        } else if (Me.getTheme() == 1) {
            pane.getStylesheets().remove("css/LightTheme.css");
            pane.getStylesheets().add("css/DarkTheme.css");
        }

        // change fields according to specific movie
        Movie m = Me.getLookingAtMovie();
        labelTitle.setText(m.getTitle());
        labelGenre.setText(m.getGenre());
        labelDirector.setText(m.getDirector());
        labelCast.setText(m.getCast());
        duration.setText(String.valueOf(m.getDuration()));
        labelPlot.setMaxWidth(695);
        labelPlot.setWrapText(true);
        labelPlot.setText(m.getPlot());

        ivMovie.setImage(new Image(m.getImageURL()));

        // initialize price label to indicate that the user has to press enter to see the calculated price
        price.setText("Press enter to see the calculated price");
    }
}
