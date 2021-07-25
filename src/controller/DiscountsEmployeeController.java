package controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Controller of the DISCOUNT scene of the EMPLOYEE application
 */
public class DiscountsEmployeeController implements Initializable {
    // credentials
    private final String url = "jdbc:mysql://localhost:3306/popcornmovie";
    private final String user = "root";
    private final String password = "";

    // Javafx elements
    @FXML
    ImageView picture;
    @FXML
    Label firstNameAndLastName;
    @FXML
    Pane pane;
    @FXML
    VBox pnItems;
    @FXML
    Button minus, modify;

    // class attributes
    private boolean minusSelected;
    private boolean modifySelected;
    //private HashMap<Integer, Discount> discounts;

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

    /***
     * enables to then click a discount to delete it
     */
    @FXML
    public void selectMinus() {
        if (minusSelected) {
            minusSelected = false;
            minus.getStyleClass().remove("minusSelected");
            minus.getStyleClass().add("minusNotSelected");
        } else {
            minusSelected = true;
            modifySelected = false;
            minus.getStyleClass().remove("minusNotSelected");
            minus.getStyleClass().add("minusSelected");
            modify.getStyleClass().remove("modifySelected");
            modify.getStyleClass().add("modifyNotSelected");
        }
    }

    /**
     * enables to then click a discount to modify it
     */
    @FXML
    public void selectModify() {
        if (modifySelected) {
            modifySelected = false;
            modify.getStyleClass().remove("modifySelected");
            modify.getStyleClass().add("modifyNotSelected");
        } else {
            modifySelected = true;
            minusSelected = false;
            modify.getStyleClass().remove("modifyNotSelected");
            modify.getStyleClass().add("modifySelected");
            minus.getStyleClass().remove("minusSelected");
            minus.getStyleClass().add("minusNotSelected");
        }
    }

    /***
     * displays an alert, if the user is sure of his or her choice, then deletes the discount from the DB
     * and updates the scene
     */
    public void deleteDiscount(Discount d) {

        int idDiscountDell = d.getId();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this discount ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            // connect to DB
            Connection connection = null;
            try {
                // create a connection to the database
                connection = DriverManager.getConnection(url, user, password);

                // statement
                Statement stmt = connection.createStatement();

                // delete movie
                stmt.executeUpdate("DELETE FROM Discounts WHERE Id = " + idDiscountDell);

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        Cinema.refresh();
        displayDiscounts();
    }

    /**
     * function that displays all the discounts in a scrollable area
     */
    private void displayDiscounts() {
        pnItems.getChildren().clear();

        ArrayList<Discount> discounts = Cinema.getDiscounts();

        for (Discount d : discounts) {
            System.out.println(d.getName());
        }

        GridPane gp;
        for (int i = 0; i < discounts.size(); i++) {
            gp = new GridPane();
            // set constraints
            ColumnConstraints cc1 = new ColumnConstraints(425);
            ColumnConstraints cc2 = new ColumnConstraints(160);
            ColumnConstraints cc3 = new ColumnConstraints(130);
            ColumnConstraints cc4 = new ColumnConstraints(100);

            gp.getColumnConstraints().addAll(cc1, cc2, cc3, cc4);

            // retrieve data
            Discount d = discounts.get(i);
            String name = d.getName();
            String amount = String.valueOf(d.getAmount());
            String unit = String.valueOf(d.getUnit());
            String status = d.getStatus();

            // make each row clickable

            gp.getStyleClass().add("buttonDiscount");
            gp.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    System.out.println(name + " : clicked");
                    if (minusSelected) {
                        deleteDiscount(d);
                    } else if (modifySelected) {
                        modifyDiscount(d);
                    }
                }
            });

            // display data
            Label discountName = new Label(name);
            discountName.setFont(new Font(25));
            gp.add(discountName, 0, 0);

            Label discountAmount = new Label(amount);
            discountAmount.setFont(new Font(25));
            gp.add(discountAmount, 1, 0);

            Label discountUnit = new Label(unit);
            discountUnit.setFont(new Font(25));
            gp.add(discountUnit, 2, 0);

            Label discountStatus = new Label(status);
            discountStatus.setFont(new Font(25));
            gp.add(discountStatus, 3, 0);

            pnItems.getChildren().add(gp);
        }

    }

    /**
     * function that loads the NEW DISCOUNT scene of EMPLOYEE application
     */
    @FXML
    private void plusDiscount() {
        System.out.println("NEW DISCOUNT");
        try {
            SceneManager.loadScene("../view/employee-new-discount.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * function that loads the MODIFY DISCOUNT scene of EMPLOYEE application
     */
    private void modifyDiscount(Discount d) {
        System.out.println("MODIFY DISCOUNT");
        Me.setModifyingDiscount(d);
        try {
            SceneManager.loadScene("../view/employee-modify-discount.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // NAVIGATION

    /***
     * function that loads the OVERVIEW scene of EMPLOYEE application
     * scene that displays the 2 most attractive movies of the moment
     * and the most interesting discount that is currently active
     */
    public void goToOverview() {
        System.out.println("OVERVIEW EMPLOYEE");
        try {
            SceneManager.loadScene("../view/employee-overview.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the MOVIES scene of EMPLOYEE application
     * displays the list of movies available depending on their genre
     * add or remove a movie
     */
    public void goToMovies() {
        System.out.println("MOVIES EMPLOYEE");
        try {
            SceneManager.loadScene("../view/employee-movies.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the DISCOUNTS scene of EMPLOYEE application
     * add, remove or modify a discount
     */
    public void goToDiscounts() {
        System.out.println("DISCOUNTS EMPLOYEE");
        try {
            SceneManager.loadScene("../view/employee-discounts.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the RECORDS scene of EMPLOYEE application
     * displays the list of all purchases, 2 possibilities : for employees or customers
     * sorts them dynamically depending on chosen criterium (email, title, nbr of tickets, date)
     */
    public void goToRecords() {
        System.out.println("RECORDS EMPLOYEE");
        try {
            SceneManager.loadScene("../view/employee-records.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the STATISTICS scene of EMPLOYEE application
     * displays 2 charts that summarize the statistics of the PopCorn Movies cinema
     * a line chart that shows the nbr of tickets bought during the last year (the label ticks ie the months are dynamically generated depending on the current month)
     * a pie chart that show the distribution of the genres of movies seen
     */
    public void goToStatistics() {
        System.out.println("STATISTICS EMPLOYEE");
        try {
            SceneManager.loadScene("../view/employee-statistics.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the PURCHASES scene of EMPLOYEE application
     * displays the total number of tickets bought, the number of tickets bought in the last 12 months and in the current month
     * displays all purchases of the user in a scrollable area
     */
    public void goToPurchases() {
        System.out.println("PURCHASES EMPLOYEE");
        try {
            SceneManager.loadScene("../view/employee-purchases.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the ACCOUNT scene of EMPLOYEE application
     * displays the date of creation of the account
     * lets the user select the appropriate category for the account (regular, senior or child)
     * lets the user select the theme of his choice (light or dark)
     * lets the user add a picture or change the current one
     * lets the user delete the account
     */
    public void goToAccount() {
        System.out.println("ACCOUNT EMPLOYEE");
        try {
            SceneManager.loadScene("../view/employee-account.fxml", 1400, 800);
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
    public void exit() {
        System.exit(0);
    }

    /***
     * first method called for initialization
     * loads user picture
     * sets chosen theme
     * displays the discounts
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        minusSelected = false;

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

        displayDiscounts();
    }
}
