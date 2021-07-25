package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.Me;
import model.SceneManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

/**
 * Controller of the PURCHASES scene of the EMPLOYEE application
 */
public class PurchasesEmployeeController implements Initializable {
    // credentials
    private final String url = "jdbc:mysql://localhost:3306/popcornmovie";
    private final String user = "root";
    private final String password = "";

    // Javafx elements
    @FXML
    ImageView picture;
    @FXML
    Label firstNameAndLastName, totalTickets, thisYear, thisMonth;
    @FXML
    Pane pane;
    @FXML
    VBox pnItems;

    // class attributes
    private String date;
    private int total, year, month;

    public PurchasesEmployeeController() {
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
     * exit the EMPLOYEE application
     */
    public void exit() {
        System.exit(0);
    }

    /***
     * first method called for initialization
     * loads user picture
     * sets chosen theme
     * retrieves and displays the purchases from the DB
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

            rs = stmt.executeQuery("SELECT p.Date, p.Title, p.Price, m.Genre, m.Director, m.Title FROM `Purchases` as p,`Movies` as m WHERE p.IdMovies = m.Id AND IdLogins = " + Me.getId());
            while (rs.next()) {
                String cinemaDate = rs.getString("Date");
                String title = rs.getString("Title");
                int price = rs.getInt("Price");
                String genre = rs.getString("Genre");
                String director = rs.getString("Director");

                GridPane gp = new GridPane();
                ColumnConstraints cc1 = new ColumnConstraints(420);
                ColumnConstraints cc2 = new ColumnConstraints(180);
                ColumnConstraints cc3 = new ColumnConstraints(210);
                ColumnConstraints cc4 = new ColumnConstraints(53);
                ColumnConstraints cc5 = new ColumnConstraints(223);
                gp.getColumnConstraints().addAll(cc1, cc2, cc3, cc4, cc5);

                Label purchaseTitle = new Label(title);
                purchaseTitle.setFont(new Font(25));
                gp.add(purchaseTitle, 0, 0);

                Label purchaseGenre = new Label(genre);
                purchaseGenre.setFont(new Font(25));
                gp.add(purchaseGenre, 1, 0);

                Label purchaseDirector = new Label(director);
                purchaseDirector.setFont(new Font(25));
                gp.add(purchaseDirector, 2, 0);

                Label purchasePrice = new Label(Integer.toString(price));
                purchasePrice.setFont(new Font(25));
                gp.add(purchasePrice, 3, 0);

                Label purchaseDate = new Label(cinemaDate);
                purchaseDate.setFont(new Font(25));
                gp.add(purchaseDate, 4, 0);

                pnItems.getChildren().add(gp);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
