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

public class OverviewEmployeeController implements Initializable {

    @FXML
    ImageView picture, movie1, movie2;
    @FXML
    Label firstNameAndLastName, discountAlert;
    @FXML
    Pane pane;

    // credentials
    private final String url = "jdbc:mysql://localhost:3306/popcornmovie";
    private final String user = "root";
    private final String password = "";

    private void loadPicture() throws Exception {
        File img = new File("picture.jpg");
        FileOutputStream ostreamImage = new FileOutputStream(img);

        try {
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
                ps.close();
            }
        } finally {
            ostreamImage.close();
        }
    }

    public void goToOverview(ActionEvent actionEvent) {
        System.out.println("OVERVIEW EMPLOYEE");
        try {
            SceneManager.loadScene("../view/employee-overview.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void goToMovies(ActionEvent actionEvent) {
        System.out.println("MOVIES EMPLOYEE");
        try {
            SceneManager.loadScene("../view/employee-movies.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void goToDiscounts(ActionEvent actionEvent) {
        System.out.println("DISCOUNTS EMPLOYEE");
        try {
            SceneManager.loadScene("../view/employee-discounts.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void goToRecords(ActionEvent actionEvent) {
        System.out.println("RECORDS EMPLOYEE");
        try {
            SceneManager.loadScene("../view/employee-records.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void goToStatistics(ActionEvent actionEvent) {
        System.out.println("STATISTICS EMPLOYEE");
        try {
            SceneManager.loadScene("../view/employee-statistics.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void goToPurchases(ActionEvent actionEvent) {
        System.out.println("PURCHASES EMPLOYEE");
        try {
            SceneManager.loadScene("../view/employee-purchases.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void goToAccount(ActionEvent actionEvent) {
        System.out.println("ACCOUNT EMPLOYEE");
        try {
            SceneManager.loadScene("../view/employee-account.fxml", 1400, 800);
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

    public void exit(ActionEvent actionEvent) {
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
