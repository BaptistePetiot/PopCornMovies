package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import main.PopCornMovie;
import model.Me;
import model.SceneManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * Controller of the ACCOUNT scene of the CUSTOMER application
 * @author Baptiste Petiot
 */
public class AccountCustomerController implements Initializable {

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
    RadioButton regular, child, senior, light, dark;
    @FXML
    Pane pane;
    @FXML
    ToggleGroup categoryGroup, themeGroup;

    /**
     * function that checks if the user already has a picture in the DB, if so changes it to the new selected one,
     * otherwise insert the image into the DB
     */
    @FXML
    protected void addPicture() {
        System.out.println("ADD PICTURE");
        // DIALOG TO CHOOSE PICTURE
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"));
        File file = fileChooser.showOpenDialog(PopCornMovie.getStage());

        // SAVE PICTURE
        try {
            BufferedImage bImage = ImageIO.read(file);
            File img = new File("picture.jpg");
            ImageIO.write(bImage, "jpg", img);

            InputStream is = new FileInputStream(img);

            // create a connection to the database
            Connection connection = DriverManager.getConnection(url, user, password);

            // CHECK IF THE USER ALREADY HAS A PROFILE IMAGE
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT picture FROM Pictures WHERE IdLogins = " + Me.getId());
            if (rs.next()) {
                System.out.println("already has an image");
                // change image
                PreparedStatement ps = connection.prepareStatement("UPDATE `Pictures` SET picture=? WHERE IdLogins=?;");
                ps.setBlob(1, is);
                ps.setInt(2, Me.getId());
                ps.executeUpdate();
            } else {
                System.out.println("no image yet");
                // add new image
                PreparedStatement ps = connection.prepareStatement("INSERT INTO `Pictures` (`IdLogins`, `picture`) VALUES (?,?);");
                ps.setInt(1, Me.getId());
                ps.setBlob(2, is);
                ps.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            loadPicture();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

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

    /***
     * displays an alert, if the user is sure of his or her choice, then deletes the account from the DB
     * and updates the scene
     */
    public void delete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete your account ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            // connect to DB
            Connection connection = null;
            try {
                // create a connection to the database
                connection = DriverManager.getConnection(url, user, password);

                // statement
                Statement stmt = connection.createStatement();

                // delete account in logins but nowhere else therefore account is deleted but history is kept for the company
                stmt.executeUpdate("DELETE FROM logins WHERE Id = " + Me.getId());
                signout();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
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
     * sets chosen category
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

            light.setSelected(true);
        } else if (Me.getTheme() == 1) {
            pane.getStylesheets().remove("css/LightTheme.css");
            pane.getStylesheets().add("css/DarkTheme.css");

            dark.setSelected(true);
        }

        // category
        if (Me.getCategory() == 0) {
            regular.setSelected(true);
        } else if (Me.getCategory() == 1) {
            senior.setSelected(true);
        } else if (Me.getCategory() == 2) {
            child.setSelected(true);
        }

        firstNameAndLastName.setText(Me.getFirstName() + " " + Me.getLastName());

        // category change
        categoryGroup = new ToggleGroup();
        regular.setToggleGroup(categoryGroup);
        senior.setToggleGroup(categoryGroup);
        child.setToggleGroup(categoryGroup);

        categoryGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                RadioButton btn = (RadioButton) categoryGroup.getSelectedToggle();
                System.out.println(btn.getText());

                // connect to DB
                Connection connection = null;
                try {
                    // create a connection to the database
                    connection = DriverManager.getConnection(url, user, password);

                    // statement
                    Statement stmt = connection.createStatement();

                    switch (btn.getText()) {
                        case ("Regular"):
                            // update theme choice in DB for next connection
                            stmt.executeUpdate("UPDATE category SET categoryNbr=0  WHERE IdLogins = " + Me.getId());
                            // update theme choice in Me for current connection
                            Me.setCategory(0);
                            break;

                        case ("Senior"):
                            // update theme choice in DB for next connection
                            stmt.executeUpdate("UPDATE category SET categoryNbr=1  WHERE IdLogins = " + Me.getId());
                            // update theme choice in Me for current connection
                            Me.setCategory(1);
                            break;

                        case ("Child"):
                            // update theme choice in DB for next connection
                            stmt.executeUpdate("UPDATE category SET categoryNbr=2  WHERE IdLogins = " + Me.getId());
                            // update theme choice in Me for current connection
                            Me.setCategory(2);
                            break;
                    }

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        });

        // theme change
        themeGroup = new ToggleGroup();
        light.setToggleGroup(themeGroup);
        dark.setToggleGroup(themeGroup);

        themeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                RadioButton btn = (RadioButton) themeGroup.getSelectedToggle();
                System.out.println(btn.getText());

                // connect to DB
                Connection connection = null;
                try {
                    // create a connection to the database
                    connection = DriverManager.getConnection(url, user, password);

                    // statement
                    Statement stmt = connection.createStatement();

                    switch (btn.getText()) {
                        case ("Light"):
                            // update theme choice in DB for next connection
                            stmt.executeUpdate("UPDATE theme SET themeNbr=0  WHERE IdLogins = " + Me.getId());
                            // update theme choice in Me for current connection
                            Me.setTheme(0);

                            pane.getStylesheets().remove("css/DarkTheme.css");
                            pane.getStylesheets().add("css/LightTheme.css");
                            break;

                        case ("Dark"):
                            // update theme choice in DB for next connection
                            stmt.executeUpdate("UPDATE theme SET themeNbr=1  WHERE IdLogins = " + Me.getId());
                            // update theme choice in Me for current connection
                            Me.setTheme(1);

                            pane.getStylesheets().remove("css/LightTheme.css");
                            pane.getStylesheets().add("css/DarkTheme.css");
                            break;

                    }

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        });

    }


}
