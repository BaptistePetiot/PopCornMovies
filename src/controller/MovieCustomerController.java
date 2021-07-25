package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

/**
 * Controller of the MOVIE scene of the CUSTOMER application
 * @author Baptiste Petiot
 */
public class MovieCustomerController implements Initializable, Consts {
    // credentials
    private final String url = "jdbc:mysql://localhost:3306/popcornmovie";
    private final String user = "root";
    private final String password = "";

    // Javafx elements
    @FXML
    ImageView picture, ivMovie;
    @FXML
    Label firstNameAndLastName, labelTitle, labelGenre, labelDirector, labelCast, labelPlot, price, duration;
    @FXML
    Pane pane;
    @FXML
    TextField tfNbrStudentDiscounts, tfNbrTickets;

    // class attributes
    private double cost = 0;
    private String date;
    private int nbrTickets;

    public MovieCustomerController() {
        // handle current date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date(System.currentTimeMillis());
        this.date = formatter.format(now);
    }

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
     * function that verify that number of tickets and number of student discounts are not empty,
     * computes the price and displays it if the user presses ENTER,
     * registers the purchase,
     * sends the tickets by email
     * and loads the PAYMENT scene of CUSTOMER application
     */
    public void goToPayment() throws Exception {
        if (tfNbrStudentDiscounts.getText() == null || tfNbrStudentDiscounts.getText().trim().isEmpty() || tfNbrTickets.getText() == null || tfNbrTickets.getText().trim().isEmpty()) {
            System.out.println("Some field is empty!");
        } else {
            computePrice();
            registerPurchase();
            // send tickets by email
            MailSender.sendTickets(Me.getEmail(), Me.getFirstName(), Me.getLookingAtMovie().getTitle(), nbrTickets);

            System.out.println("PAYMENT CUSTOMER");
            try {
                SceneManager.loadScene("../view/customer-payment.fxml", 959, 262);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
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
            stmt.executeUpdate("INSERT INTO `purchases` (`Id`, `IdLogins`, `IdMovies`, `Title`, `NbrTickets`, `Date`, `Price`) VALUES ('  " + purchaseId + "', ' " + Me.getId() + "', ' " + Me.getLookingAtMovie().getId() + "', ' " + Me.getLookingAtMovie().getTitle() + "', '" + nbrTickets + "', '" + date + "', '" + cost + "');");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * computes the full cost based on the number of normal tickets and the number of student discounts
     */
    private void computePrice() {
        nbrTickets = Integer.parseInt(tfNbrTickets.getText());
        int nbrStudentTickets = Integer.parseInt(tfNbrStudentDiscounts.getText());
        int nbrNormalTickets = nbrTickets - nbrStudentTickets - 1;
        int personalTicket = 1;

        // takes into account the personal category of the user (for one ticket only obviously)
        double price = 0;
        if(Me.getCategory() == 0){
            price = personalTicket * Consts.NORMAL_PRICE + nbrStudentTickets * Consts.STUDENT_PRICE + nbrNormalTickets * Consts.NORMAL_PRICE;
        }else if(Me.getCategory() == 1){
            price = personalTicket * Consts.SENIOR_PRICE + nbrStudentTickets * Consts.STUDENT_PRICE + nbrNormalTickets * Consts.NORMAL_PRICE;
        }else if(Me.getCategory() == 2){
            price = personalTicket * Consts.CHILD_PRICE + nbrStudentTickets * Consts.STUDENT_PRICE + nbrNormalTickets * Consts.NORMAL_PRICE;
        }

        // takes into account the highest active discount
        try{
            // create a connection to the database
            Connection connection = DriverManager.getConnection(url, user, password);
            // statement
            Statement stmt = connection.createStatement();

            // DISCOUNT
            ResultSet rs = stmt.executeQuery("SELECT Name, Amount, Unit FROM Discounts WHERE Status = 'Active' ORDER BY Amount DESC");
            String unit = "";
            int amount = 0;
            if (rs.next()) {
                amount = rs.getInt("Amount");
                unit = rs.getString("Unit");

                if(unit.equals("%")){
                    cost = price * (100.0 - amount)/100;
                }else{
                    cost = price - amount;
                }

            }else{
                cost = price;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

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
     * displays movie information according to the selected movie
     *
     * @param location
     * @param resources
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
