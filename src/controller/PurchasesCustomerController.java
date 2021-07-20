package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import main.PopCornMovie;
import model.Me;
import model.SceneManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class PurchasesCustomerController implements Initializable {
    @FXML
    ImageView picture;
    @FXML
    Label firstNameAndLastName, thisMonth, thisYear, totalTickets;
    @FXML
    Pane pane;
    @FXML
    VBox pnItems;

    private int total, year, month;

    // credentials
    private final String url = "jdbc:mysql://localhost:3306/popcornmovie";
    private final String user = "root";
    private final String password = "";

    private String date;

    public PurchasesCustomerController() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date(System.currentTimeMillis());
        this.date = formatter.format(now);
    }

    private void loadPicture() throws Exception{
        File img = new File("picture.jpg");
        FileOutputStream ostreamImage = new FileOutputStream(img);

        try{
            // create a connection to the database
            Connection connection = DriverManager.getConnection(url, user, password);
            // prepared statement
            PreparedStatement ps = connection.prepareStatement("SELECT picture FROM pictures WHERE IdLogins=?");

            try{
                ps.setInt(1,Me.getId());
                ResultSet rs = ps.executeQuery();

                try{
                    if(rs.next()){
                        InputStream istreamImage = rs.getBinaryStream("picture");

                        byte[] buffer = new byte[1024];
                        int length = 0;

                        while((length = istreamImage.read(buffer)) != -1){
                            ostreamImage.write(buffer, 0, length);  // save image locally
                        }

                        // set image
                        Image image = new Image(img.toURI().toString());
                        picture.setImage(image);
                    }
                }
                finally{
                    rs.close();
                }
            }
            finally{
                ps.close();
            }
        }
        finally{
            ostreamImage.close();
        }
    }

    public void goToOverview(ActionEvent actionEvent) {
        System.out.println("OVERVIEW CUSTOMER");
        try {
            SceneManager.loadScene("../view/customer-overview.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void goToMovies(ActionEvent actionEvent) {
        System.out.println("MOVIES CUSTOMER");
        try {
            SceneManager.loadScene("../view/customer-movies.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void goToPurchases(ActionEvent actionEvent) {
        System.out.println("PURCHASES CUSTOMER");
        try {
            SceneManager.loadScene("../view/customer-purchases.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void goToAccount(ActionEvent actionEvent) {
        System.out.println("ACCOUNT CUSTOMER");
        try {
            SceneManager.loadScene("../view/customer-account.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void signout(ActionEvent actionEvent) {
        System.out.println("SIGN OUT");
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

        // Retrieve purchases from DB

        // connect to DB
        Connection connection = null;

        try {
            // create a connection to the database
            connection = DriverManager.getConnection(url, user, password);

            // statement
            Statement stmt = connection.createStatement();
            ResultSet rs;

            // retrieve total number of tickets bought
            rs = stmt.executeQuery("SELECT SUM(NbrTickets) FROM `purchases` WHERE Idlogins = " + Me.getId());
            while (rs.next()) {
                total = rs.getInt(1);
            }
            totalTickets.setText(String.valueOf(total));

            // retrieve the number of tickets bought this year
            rs = stmt.executeQuery("SELECT SUM(NbrTickets) FROM `purchases` WHERE Idlogins = " + Me.getId() + " AND YEAR(Date) = " + date.split("-")[0]);
            while (rs.next()) {
                year = rs.getInt(1);
            }
            thisYear.setText(String.valueOf(year));

            // retrieve the number of tickets bought this month
            rs = stmt.executeQuery("SELECT SUM(NbrTickets) FROM `purchases` WHERE Idlogins = " + Me.getId() + " AND YEAR(Date) = " + date.split("-")[0] + " AND MONTH(Date) = " + date.split("-")[1]);
            while (rs.next()) {
                month = rs.getInt(1);
            }
            thisMonth.setText(String.valueOf(month));

            // retrieve purchases
            pnItems.getChildren().clear();
            // TODO!!!
            rs = stmt.executeQuery("SELECT p.Date, p.Title, p.Price, m.Genre, m.Director FROM `Purchases` AS p, `Movies` AS m WHERE p.Title=m.Title;");
            //rs = stmt.executeQuery("SELECT Purchases.Date, Purchases.Title, Purchases.Price FROM Purchases WHERE Idlogins = " + Me.getId());
            //rs = stmt.executeQuery("SELECT Purchases.Date, Purchases.Title, Purchases.Price, Movies.Genre, Movies.Director FROM `Purchases` INNER JOIN `Movies` ON Movies.Title = Purchases.Title WHERE Idlogins = " + Me.getId());
            while (rs.next()) {
                String cinemaDate = rs.getString("Date");
                String title = rs.getString("Title");
                int price = rs.getInt("Price");
                String genre = rs.getString("Genre");
                String director = rs.getString("Director");

                // avoid title string overflow
                if(title.toCharArray().length > 35){
                    StringBuilder sb = new StringBuilder();
                    for(int i = 0; i < title.toCharArray().length; i++){
                        if(i < 32){
                            sb.append(title.toCharArray()[i]);
                        }else if(i == 32){
                            sb.append("...");
                        }
                    }
                    title = sb.toString();
                }
                // compute title right padding
                int rightPadding = (int) Math.round(2500.0/title.toCharArray().length);
                //int rightPadding = (int) (350.0 - title.toCharArray().length)/3;
                System.out.println(rightPadding);

                HBox hbox = new HBox();
                //HBox.setMargin(hbox, new Insets(0, 0, 0, 75.0));
                hbox.setAlignment(Pos.CENTER_RIGHT);

                Label purchaseTitle = new Label(title);
                purchaseTitle.setFont(new Font(25));
                purchaseTitle.setPadding(new Insets(0, rightPadding, 0, 0));
                Label purchaseGenre = new Label("genre");
                purchaseGenre.setFont(new Font(25));
                purchaseGenre.setPadding(new Insets(0, 75, 0, 0));
                Label purchaseDirector = new Label("director");
                purchaseDirector.setFont(new Font(25));
                purchaseDirector.setPadding(new Insets(0, 80, 0, 0));
                Label purchasePrice = new Label(Integer.toString(price));
                purchasePrice.setFont(new Font(25));
                purchasePrice.setPadding(new Insets(0, 50, 0, 0));
                Label purchaseDate = new Label(cinemaDate);
                purchaseDate.setFont(new Font(25));
                purchaseDate.setPadding(new Insets(0, 5, 0, 0));

                hbox.getChildren().addAll(purchaseTitle, purchaseGenre, purchaseDirector, purchasePrice, purchaseDate);
                pnItems.getChildren().add(hbox);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
