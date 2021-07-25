package controller;

import javafx.event.ActionEvent;
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
import javafx.util.Pair;
import model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class DiscountsEmployeeController implements Initializable {
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

    // credentials
    private final String url = "jdbc:mysql://localhost:3306/popcornmovie";
    private final String user = "root";
    private final String password = "";

    private boolean minusSelected;
    private boolean modifySelected;
    private HashMap<Integer, Discount> discounts;

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

    @FXML
    private void plusDiscount() {
        System.out.println("NEW DISCOUNT");
        try {
            SceneManager.loadScene("../view/employee-new-discount.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void modifyDiscount(Discount d) {
        System.out.println("MODIFY DISCOUNT");
        Me.setModifyingDiscount(d);
        try {
            SceneManager.loadScene("../view/employee-modify-discount.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

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

    private void displayDiscounts_old() {
        // Retrieve discounts from DB
        // connect to DB
        Connection connection = null;

        try {
            // create a connection to the database
            connection = DriverManager.getConnection(url, user, password);

            // statement
            Statement stmt = connection.createStatement();
            ResultSet rs;
            pnItems.getChildren().clear();

            rs = stmt.executeQuery("SELECT Name, Amount, Unit, Status FROM `Discounts`");
            while (rs.next()) {
                String name = rs.getString("Name");
                String amount = rs.getString("Amount");
                String unit = rs.getString("Unit");
                String status = rs.getString("Status");

                GridPane gp = new GridPane();
                ColumnConstraints cc1 = new ColumnConstraints(425);
                ColumnConstraints cc2 = new ColumnConstraints(160);
                ColumnConstraints cc3 = new ColumnConstraints(130);
                ColumnConstraints cc4 = new ColumnConstraints(100);
                //ColumnConstraints cc5 = new ColumnConstraints(100);

                //gp.getColumnConstraints().addAll(cc1, cc2, cc3, cc4, cc5);
                gp.getColumnConstraints().addAll(cc1, cc2, cc3, cc4);

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

                /*RadioButton selection = new RadioButton("");
                selection.setFont(new Font(25));
                gp.add(selection,4,0);*/

                pnItems.getChildren().add(gp);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

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

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

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
