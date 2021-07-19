package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import main.PopCornMovie;
import model.Me;
import model.SceneManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

    @FXML
    protected void addPicture() {
        System.out.println("ADD PICTURE");
        // DIALOG TO CHOOSE PICTURE
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG", "*.png"));
        File file = fileChooser.showOpenDialog(PopCornMovie.getStage());

        // SAVE PICTURE
        try {
            BufferedImage bImage = ImageIO.read(file);
            ImageIO.write(bImage, "png", new File("picture_" + Me.getId() + ".png"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // RETRIEVE PICTURE AND SET IT IN ACCOUNT ImageView
        try {
            file = new File("picture" + Me.getId() + ".png");
            Image image = new Image(file.toURI().toString());
            picture.setImage(image);                            //TODO : make it work!
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
        // theme
        if (Me.getTheme() == 0) {
            pane.getStylesheets().remove("css/DarkTheme.css");
            pane.getStylesheets().add("css/LightTheme.css");
        } else if (Me.getTheme() == 1) {
            pane.getStylesheets().remove("css/LightTheme.css");
            pane.getStylesheets().add("css/DarkTheme.css");
        }

        //picture
        File file = new File("picture" + Me.getId() + ".png");
        if (file.exists()) {
            System.out.println("image exists");
            Image image = new Image(file.toURI().toString());
            picture.setImage(image);
        } else {
            System.out.println("image does not exist");
            picture.setImage(new Image(new File("@../imgs/circle.png").toURI().toString()));
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
            rs = stmt.executeQuery("SELECT * FROM `purchases` WHERE Idlogins = " + Me.getId());
            while (rs.next()) {
                String cinemaDate = rs.getString("Date");
                String title = rs.getString("Title");
                int price = rs.getInt("Price");

                /*Pane p = new Pane();
                p.getChildren().add(new Label());
                pnItems.getChildren().add();*/

                try {
                    /*Node[] nodes = new Node[10];
                    for (int i = 0; i < nodes.length; i++) {
                        pnItems.getChildren().removeAll();
                        nodes[i] = (Node) FXMLLoader.load(getClass().getResource("view/item.fxml"));
                        pnItems.getChildren().add(nodes[i]);
                    }*/

                    Node node = (Node) FXMLLoader.load(getClass().getResource("view/item.fxml"));
                    pnItems.getChildren().add(node);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
