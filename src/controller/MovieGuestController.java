package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class MovieGuestController implements Initializable, Consts {
    @FXML ImageView ivMovie;
    @FXML Label labelTitle, labelGenre, labelDirector, labelCast, labelPlot, price, duration;
    @FXML Pane pane;
    @FXML TextField tfNbrTickets;

    private int cost = 0;
    private String date;
    private int nbrTickets;

    // credentials
    private final String url       = "jdbc:mysql://localhost:3306/popcornmovie";
    private final String user      = "root";
    private final String password  = "";

    public MovieGuestController(){
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date(System.currentTimeMillis());
        this.date = formatter.format(now);
    }

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

    public void goToSettings(ActionEvent actionEvent) {
        System.out.println("SETTINGS GUEST");
        try{
            SceneManager.loadScene("../view/guest-settings.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void signout(ActionEvent actionEvent) {
        System.out.println("EXIT");
        try{
            SceneManager.loadScene("../view/login.fxml", 700,400);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToPayment(ActionEvent actionEvent) throws Exception {
        if(tfNbrTickets.getText() == null || tfNbrTickets.getText().trim().isEmpty()){
            System.out.println("The field is empty!");
        }else{
            computePrice();
            registerPurchase();

            // send tickets by email
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Email");
            dialog.setHeaderText("Enter your email to receive your tickets, don't worry it won't be stored in the database.");
            dialog.setContentText("Email:");
            Optional<String> result = dialog.showAndWait();

            result.ifPresent(email -> {
                Me.setEmail(email);
            });

            MailSender.sendTickets(Me.getEmail(), Me.getFirstName(), Me.getLookingAtMovie().getTitle(), nbrTickets);

            System.out.println("PAYMENT GUEST");
            try{
                SceneManager.loadScene("../view/guest-payment.fxml", 959,262);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    private void registerPurchase() {
        // connect to DB
        Connection connection = null;

        try{
            // create a connection to the database
            connection = DriverManager.getConnection(url, user, password);

            // statement
            Statement stmt = connection.createStatement();
            ResultSet rs;

            // retrieve highest id
            rs = stmt.executeQuery("SELECT MAX(Id) FROM `purchases`");
            int maxId = 0;
            while(rs.next()){
                maxId = rs.getInt(1);
            }
            int purchaseId = maxId + 1;

            // add purchase to DB
            stmt.executeUpdate("INSERT INTO `purchases` (`Id`, `IdLogins`, `IdMovies`, `Title`, `NbrTickets`, `Date`, `Price`) VALUES ('  " + purchaseId + "', '0', ' "+ Me.getLookingAtMovie().getId() +  "', ' "+ Me.getLookingAtMovie().getTitle() + "', '" + nbrTickets + "', '" + date + "', '" + cost + "');");

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    // NO DISCOUNTS AS GUEST
    private void computePrice(){
        nbrTickets = Integer.parseInt(tfNbrTickets.getText());

        cost = nbrTickets * Consts.NORMAL_PRICE;
    }

    public void displayPrice(KeyEvent keyEvent) {
        if( keyEvent.getCode() == KeyCode.ENTER ) {
            computePrice();
            price.setText(String.valueOf(cost));
        }
    }

    @FXML
    private void exit(){
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        price.setText("Press enter to see the computer price");

        // theme
        if(Me.getTheme() == 0){
            pane.getStylesheets().remove("css/DarkTheme.css");
            pane.getStylesheets().add("css/LightTheme.css");
        }else if(Me.getTheme() == 1){
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
        /*String[] sentences = m.getPlot().split(".");
        StringBuilder plot = new StringBuilder();
        for(String s : sentences){
            plot.append(s).append("\n");
        }
        labelPlot.setText(plot.toString());
        System.out.println(plot.toString());*/
        labelPlot.setText(m.getPlot());

        ivMovie.setImage(new Image(m.getImageURL()));

        // initialize price label to indicate that the user has to press enter to see the calculated price
        price.setText("Press enter to see the calculated price");
    }
}
