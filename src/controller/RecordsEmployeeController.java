package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
import java.util.ResourceBundle;

public class RecordsEmployeeController implements Initializable {

    @FXML
    Label firstNameAndLastName;
    @FXML
    ImageView picture;
    @FXML
    Pane pane;
    @FXML
    VBox pnItems;
    @FXML
    Button buttonCustomers, buttonEmployees;

    // credentials
    private final String url = "jdbc:mysql://localhost:3306/popcornmovie";
    private final String user = "root";
    private final String password = "";

    private boolean customersSelected, orderByEmail, orderByTitle, orderByNbrTickets, orderByDate;

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

    @FXML
    private void setRecordCustomers() {
        customersSelected = true;
        buttonEmployees.getStyleClass().remove("recordSelected");
        buttonCustomers.getStyleClass().add("recordSelected");
        showRecords();
    }

    @FXML
    private void setRecordEmployees() {
        customersSelected = false;
        buttonCustomers.getStyleClass().remove("recordSelected");
        buttonEmployees.getStyleClass().add("recordSelected");
        showRecords();
    }

    public void orderByEmail(ActionEvent actionEvent) {
        orderByDate = false;
        orderByEmail = true;
        orderByNbrTickets = false;
        orderByTitle = false;
        showRecords();
    }

    public void orderByTitle(ActionEvent actionEvent) {
        orderByDate = false;
        orderByEmail = false;
        orderByNbrTickets = false;
        orderByTitle = true;
        showRecords();
    }

    public void orderByNbrTickets(ActionEvent actionEvent) {
        orderByDate = false;
        orderByEmail = false;
        orderByNbrTickets = true;
        orderByTitle = false;
        showRecords();
    }

    public void orderByDate(ActionEvent actionEvent) {
        orderByDate = true;
        orderByEmail = false;
        orderByNbrTickets = false;
        orderByTitle = false;
        showRecords();
    }

    private void showRecords() {
        // Retrieve records from DB

        // connect to DB
        Connection connection = null;

        try {
            // create a connection to the database
            connection = DriverManager.getConnection(url, user, password);

            // statement
            Statement stmt = connection.createStatement();
            ResultSet rs = null;

            // retrieve records
            pnItems.getChildren().clear();

            if (customersSelected) {
                if (orderByEmail) {
                    System.out.println("Customers : order by email addresses");
                    rs = stmt.executeQuery("SELECT p.Date, p.NbrTickets, p.Title, l.Email, c.LastName, c.FirstName FROM `Purchases` as p,`Logins` as l, `Customers` as c WHERE p.IdLogins = l.Id AND l.Id = c.IdLogins ORDER BY l.Email");
                } else if (orderByTitle) {
                    System.out.println("Customers : order by movie titles");
                    rs = stmt.executeQuery("SELECT p.Date, p.NbrTickets, p.Title, l.Email, c.LastName, c.FirstName FROM `Purchases` as p,`Logins` as l, `Customers` as c WHERE p.IdLogins = l.Id AND l.Id = c.IdLogins ORDER BY p.Title");
                } else if (orderByNbrTickets) {
                    System.out.println("Customers : order by nbr of tickets");
                    rs = stmt.executeQuery("SELECT p.Date, p.NbrTickets, p.Title, l.Email, c.LastName, c.FirstName FROM `Purchases` as p,`Logins` as l, `Customers` as c WHERE p.IdLogins = l.Id AND l.Id = c.IdLogins ORDER BY p.NbrTickets");
                } else if (orderByDate) {
                    System.out.println("Customers : order by date");
                    rs = stmt.executeQuery("SELECT p.Date, p.NbrTickets, p.Title, l.Email, c.LastName, c.FirstName FROM `Purchases` as p,`Logins` as l, `Customers` as c WHERE p.IdLogins = l.Id AND l.Id = c.IdLogins ORDER BY p.Date");
                }
            } else {
                if (orderByEmail) {
                    System.out.println("Employees : order by email addresses");
                    rs = stmt.executeQuery("SELECT p.Date, p.NbrTickets, p.Title, l.Email, e.LastName, e.FirstName FROM `Purchases` as p,`Logins` as l, `Employees` as e WHERE p.IdLogins = l.Id AND l.Id = e.IdLogins ORDER BY l.Email");
                } else if (orderByTitle) {
                    System.out.println("Employees : order by movie titles");
                    rs = stmt.executeQuery("SELECT p.Date, p.NbrTickets, p.Title, l.Email, e.LastName, e.FirstName FROM `Purchases` as p,`Logins` as l, `Employees` as e WHERE p.IdLogins = l.Id AND l.Id = e.IdLogins ORDER BY p.Title");
                } else if (orderByNbrTickets) {
                    System.out.println("Employees : order by nbr of tickets");
                    rs = stmt.executeQuery("SELECT p.Date, p.NbrTickets, p.Title, l.Email, e.LastName, e.FirstName FROM `Purchases` as p,`Logins` as l, `Employees` as e WHERE p.IdLogins = l.Id AND l.Id = e.IdLogins ORDER BY p.NbrTickets");
                } else if (orderByDate) {
                    System.out.println("Employees : order by date");
                    rs = stmt.executeQuery("SELECT p.Date, p.NbrTickets, p.Title, l.Email, e.LastName, e.FirstName FROM `Purchases` as p,`Logins` as l, `Employees` as e WHERE p.IdLogins = l.Id AND l.Id = e.IdLogins ORDER BY p.Date");
                }
            }


            while (rs.next()) {
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String email = rs.getString("Email");
                String title = rs.getString("Title");
                int nbrT = rs.getInt("NbrTickets");
                String cinemaDate = rs.getString("Date");

                GridPane gp = new GridPane();
                ColumnConstraints cc1 = new ColumnConstraints(150);
                ColumnConstraints cc2 = new ColumnConstraints(110);
                ColumnConstraints cc3 = new ColumnConstraints(240);
                ColumnConstraints cc4 = new ColumnConstraints(320);
                ColumnConstraints cc5 = new ColumnConstraints(73);
                ColumnConstraints cc6 = new ColumnConstraints(180);
                gp.getColumnConstraints().addAll(cc1, cc2, cc3, cc4, cc5, cc6);

                Label recordFirstName = new Label(firstName);
                recordFirstName.setFont(new Font(25));
                gp.add(recordFirstName, 0, 0);

                Label recordLastName = new Label(lastName);
                recordLastName.setFont(new Font(25));
                gp.add(recordLastName, 1, 0);

                Label recordEmail = new Label(email);
                recordEmail.setFont(new Font(25));
                gp.add(recordEmail, 2, 0);

                Label recordTitle = new Label(title);
                recordTitle.setFont(new Font(25));
                gp.add(recordTitle, 3, 0);

                Label recordNbrTickets = new Label(String.valueOf(nbrT));
                recordNbrTickets.setFont(new Font(25));
                gp.add(recordNbrTickets, 4, 0);

                Label recordDate = new Label(cinemaDate);
                recordDate.setFont(new Font(25));
                gp.add(recordDate, 5, 0);

                pnItems.getChildren().add(gp);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderByDate = true;
        orderByEmail = false;
        orderByNbrTickets = false;
        orderByTitle = false;

        customersSelected = true;
        setRecordCustomers();

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

        showRecords();
    }
}
