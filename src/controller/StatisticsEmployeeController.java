package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import model.Me;
import model.Month;
import model.SceneManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Observable;
import java.util.ResourceBundle;

public class StatisticsEmployeeController implements Initializable {

    @FXML ImageView picture;
    @FXML Label firstNameAndLastName;
    @FXML Pane pane;
    @FXML PieChart pieChart;
    //@FXML LineChart lineChart;

    // credentials
    private final String url       = "jdbc:mysql://localhost:3306/popcornmovie";
    private final String user      = "root";
    private final String password  = "";

    private Month month;

    public StatisticsEmployeeController(){
        //SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date(System.currentTimeMillis());
        //this.month = formatter.format(now);

        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

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
        System.out.println("OVERVIEW EMPLOYEE");
        try{
            SceneManager.loadScene("../view/employee-overview.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToMovies(ActionEvent actionEvent) {
        System.out.println("MOVIES EMPLOYEE");
        try{
            SceneManager.loadScene("../view/employee-movies.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToDiscounts(ActionEvent actionEvent) {
        System.out.println("DISCOUNTS EMPLOYEE");
        try{
            SceneManager.loadScene("../view/employee-discounts.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToRecords(ActionEvent actionEvent) {
        System.out.println("RECORDS EMPLOYEE");
        try{
            SceneManager.loadScene("../view/employee-records.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToStatistics(ActionEvent actionEvent) {
        System.out.println("STATISTICS EMPLOYEE");
        try{
            SceneManager.loadScene("../view/employee-statistics.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToPurchases(ActionEvent actionEvent) {
        System.out.println("PURCHASES EMPLOYEE");
        try{
            SceneManager.loadScene("../view/employee-purchases.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToAccount(ActionEvent actionEvent) {
        System.out.println("ACCOUNT EMPLOYEE");
        try{
            SceneManager.loadScene("../view/employee-account.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void signout(ActionEvent actionEvent) {
        System.out.println("SIGN OUT");
        try{
            SceneManager.loadScene("../view/login.fxml", 700,400);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void exit(ActionEvent actionEvent) { System.exit(0);}

    private void applyCustomColorSequence(ObservableList<PieChart.Data> pieChartData, String... pieColors) {
        int i = 0;
        for (PieChart.Data data : pieChartData) {
            data.getNode().setStyle("-fx-pie-color: " + pieColors[i % pieColors.length] + ";");
            i++;
        }
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
        if(Me.getTheme() == 0){
            pane.getStylesheets().remove("css/DarkTheme.css");
            pane.getStylesheets().add("css/LightTheme.css");
        }else if(Me.getTheme() == 1){
            pane.getStylesheets().remove("css/LightTheme.css");
            pane.getStylesheets().add("css/DarkTheme.css");
        }

        firstNameAndLastName.setText(Me.getFirstName() + " " + Me.getLastName());

        // DISPLAY STATS
        // pie chart

        // retrieve nbr of tickets bought for each genre
        int nbrAction=0, nbrAdventure=0, nbrFantasy=0, nbrDocumentary=0, nbrSciFi=0, nbrHorror=0, nbrAnimation=0, nbrThriller=0, nbrComedy=0, nbrDrama=0;

        // create a connection to the database
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);

            // statement
            Statement stmt=connection.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets), m.Genre FROM Purchases as p, Movies as m WHERE p.IdMovies = m.Id AND m.Genre = 'Action'");
            while(rs.next()){ nbrAction = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets), m.Genre FROM Purchases as p, Movies as m WHERE p.IdMovies = m.Id AND m.Genre = 'Adventure'");
            while(rs.next()){ nbrAdventure = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets), m.Genre FROM Purchases as p, Movies as m WHERE p.IdMovies = m.Id AND m.Genre = 'Fantasy'");
            while(rs.next()){ nbrFantasy = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets), m.Genre FROM Purchases as p, Movies as m WHERE p.IdMovies = m.Id AND m.Genre = 'Documentary'");
            while(rs.next()){ nbrDocumentary = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets), m.Genre FROM Purchases as p, Movies as m WHERE p.IdMovies = m.Id AND m.Genre = 'Science Fiction'");
            while(rs.next()){ nbrSciFi = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets), m.Genre FROM Purchases as p, Movies as m WHERE p.IdMovies = m.Id AND m.Genre = 'Horror'");
            while(rs.next()){ nbrHorror = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets), m.Genre FROM Purchases as p, Movies as m WHERE p.IdMovies = m.Id AND m.Genre = 'Animation'");
            while(rs.next()){ nbrAnimation = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets), m.Genre FROM Purchases as p, Movies as m WHERE p.IdMovies = m.Id AND m.Genre = 'Thriller'");
            while(rs.next()){ nbrThriller = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets), m.Genre FROM Purchases as p, Movies as m WHERE p.IdMovies = m.Id AND m.Genre = 'Comedy'");
            while(rs.next()){ nbrComedy = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets), m.Genre FROM Purchases as p, Movies as m WHERE p.IdMovies = m.Id AND m.Genre = 'Drama'");
            while(rs.next()){ nbrDrama = rs.getInt(1); }

            PieChart.Data sliceAction = new PieChart.Data("Action",nbrAction);
            PieChart.Data sliceAdventure = new PieChart.Data("Adventure", nbrAdventure);
            PieChart.Data sliceFantasy = new PieChart.Data("Fantasy",nbrFantasy);
            PieChart.Data sliceDocumentary = new PieChart.Data("Documentary", nbrDocumentary);
            PieChart.Data sliceSciFi = new PieChart.Data("SciFi",nbrSciFi);
            PieChart.Data sliceHorror = new PieChart.Data("Horror", nbrHorror);
            PieChart.Data sliceAnimation = new PieChart.Data("Animation",nbrAnimation);
            PieChart.Data sliceThriller = new PieChart.Data("Thriller", nbrThriller);
            PieChart.Data sliceComedy = new PieChart.Data("Comedy",nbrComedy);
            PieChart.Data sliceDrama = new PieChart.Data("Drama", nbrDrama);

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    sliceAction,
                    sliceAdventure,
                    sliceFantasy,
                    sliceDocumentary,
                    sliceSciFi,
                    sliceHorror,
                    sliceAnimation,
                    sliceThriller,
                    sliceComedy,
                    sliceDrama
            );

            pieChart.getData().addAll(sliceAction, sliceAdventure, sliceFantasy, sliceDocumentary, sliceSciFi, sliceHorror, sliceAnimation, sliceThriller, sliceComedy, sliceDrama);
            pieChart.setStartAngle(90);
            pieChart.setLegendVisible(false);

            applyCustomColorSequence(
                    pieChartData,
                    "#FDAC53",
                    "#9BB7D4",
                    "#B55A30",
                    "#F5DF4D",
                    "#0072B5",
                    "#A0DAA9",
                    "#E9897E",
                    "#00A170",
                    "#926AA6",
                    "#D2386C"
            );

            // line chart
            //instanciating new line chart
            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Month");
            yAxis.setLabel("Nbr of tickets bought");
            final LineChart<String,Number> lineChart = new LineChart<String,Number>(xAxis,yAxis);
            lineChart.setLayoutX(329.0);
            lineChart.setLayoutY(196.0);

            //x ticks
            String one = month.previous2letters(11);
            String two = month.previous2letters(10);
            String three = month.previous2letters(9);
            String four = month.previous2letters(8);
            String five = month.previous2letters(7);
            String six = month.previous2letters(6);
            String seven = month.previous2letters(5);
            String eight = month.previous2letters(4);
            String nine = month.previous2letters(3);
            String ten = month.previous2letters(2);
            String eleven = month.previous2letters(1);
            String twelve = month.current2letters();

            // employees
            int m11_e = 0, m10_e = 0, m9_e = 0, m8_e = 0, m7_e = 0, m6_e = 0, m5_e = 0, m4_e = 0, m3_e = 0, m2_e = 0, m1_e = 0, mCurrent_e = 0;

            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets) FROM Purchases as p WHERE MONTH(p.Date) = " + month.current() + " AND p.IdLogins IN (SELECT IdLogins FROM Employees)");
            while(rs.next()){ mCurrent_e = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets) FROM Purchases as p WHERE MONTH(p.Date) = " + month.previous(1) + " AND p.IdLogins IN (SELECT IdLogins FROM Employees)");
            while(rs.next()){ m1_e = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets) FROM Purchases as p WHERE MONTH(p.Date) = " + month.previous(2) + " AND p.IdLogins IN (SELECT IdLogins FROM Employees)");
            while(rs.next()){ m2_e = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets) FROM Purchases as p WHERE MONTH(p.Date) = " + month.previous(3) + " AND p.IdLogins IN (SELECT IdLogins FROM Employees)");
            while(rs.next()){ m3_e = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets) FROM Purchases as p WHERE MONTH(p.Date) = " + month.previous(4) + " AND p.IdLogins IN (SELECT IdLogins FROM Employees)");
            while(rs.next()){ m4_e = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets) FROM Purchases as p WHERE MONTH(p.Date) = " + month.previous(5) + " AND p.IdLogins IN (SELECT IdLogins FROM Employees)");
            while(rs.next()){ m5_e = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets) FROM Purchases as p WHERE MONTH(p.Date) = " + month.previous(6) + " AND p.IdLogins IN (SELECT IdLogins FROM Employees)");
            while(rs.next()){ m6_e = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets) FROM Purchases as p WHERE MONTH(p.Date) = " + month.previous(7) + " AND p.IdLogins IN (SELECT IdLogins FROM Employees)");
            while(rs.next()){ m7_e = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets) FROM Purchases as p WHERE MONTH(p.Date) = " + month.previous(8) + " AND p.IdLogins IN (SELECT IdLogins FROM Employees)");
            while(rs.next()){ m8_e = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets) FROM Purchases as p WHERE MONTH(p.Date) = " + month.previous(9) + " AND p.IdLogins IN (SELECT IdLogins FROM Employees)");
            while(rs.next()){ m9_e = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets) FROM Purchases as p WHERE MONTH(p.Date) = " + month.previous(10) + " AND p.IdLogins IN (SELECT IdLogins FROM Employees)");
            while(rs.next()){ m10_e = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets) FROM Purchases as p WHERE MONTH(p.Date) = " + month.previous(11) + " AND p.IdLogins IN (SELECT IdLogins FROM Employees)");
            while(rs.next()){ m11_e = rs.getInt(1); }

            //defining employees series
            XYChart.Series seriesEmployees = new XYChart.Series();
            seriesEmployees.setName("Employees");

            seriesEmployees.getData().add(new XYChart.Data(one, m11_e));
            seriesEmployees.getData().add(new XYChart.Data(two, m10_e));
            seriesEmployees.getData().add(new XYChart.Data(three, m9_e));
            seriesEmployees.getData().add(new XYChart.Data(four, m8_e));
            seriesEmployees.getData().add(new XYChart.Data(five, m7_e));
            seriesEmployees.getData().add(new XYChart.Data(six, m6_e));
            seriesEmployees.getData().add(new XYChart.Data(seven, m5_e));
            seriesEmployees.getData().add(new XYChart.Data(eight, m4_e));
            seriesEmployees.getData().add(new XYChart.Data(nine, m3_e));
            seriesEmployees.getData().add(new XYChart.Data(ten, m2_e));
            seriesEmployees.getData().add(new XYChart.Data(eleven, m1_e));
            seriesEmployees.getData().add(new XYChart.Data(twelve, mCurrent_e));

            // customers

            int m11_c = 0, m10_c = 0, m9_c = 0, m8_c = 0, m7_c = 0, m6_c = 0, m5_c = 0, m4_c = 0, m3_c = 0, m2_c = 0, m1_c = 0, mCurrent_c = 0;

            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets) FROM Purchases as p WHERE MONTH(p.Date) = " + month.current() + " AND p.IdLogins IN (SELECT IdLogins FROM Customers)");
            while(rs.next()){ mCurrent_c = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets) FROM Purchases as p WHERE MONTH(p.Date) = " + month.previous(1) + " AND p.IdLogins IN (SELECT IdLogins FROM Customers)");
            while(rs.next()){ m1_c = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets) FROM Purchases as p WHERE MONTH(p.Date) = " + month.previous(2) + " AND p.IdLogins IN (SELECT IdLogins FROM Customers)");
            while(rs.next()){ m2_c = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets) FROM Purchases as p WHERE MONTH(p.Date) = " + month.previous(3) + " AND p.IdLogins IN (SELECT IdLogins FROM Customers)");
            while(rs.next()){ m3_c = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets) FROM Purchases as p WHERE MONTH(p.Date) = " + month.previous(4) + " AND p.IdLogins IN (SELECT IdLogins FROM Customers)");
            while(rs.next()){ m4_c = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets) FROM Purchases as p WHERE MONTH(p.Date) = " + month.previous(5) + " AND p.IdLogins IN (SELECT IdLogins FROM Customers)");
            while(rs.next()){ m5_c = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets) FROM Purchases as p WHERE MONTH(p.Date) = " + month.previous(6) + " AND p.IdLogins IN (SELECT IdLogins FROM Customers)");
            while(rs.next()){ m6_c = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets) FROM Purchases as p WHERE MONTH(p.Date) = " + month.previous(7) + " AND p.IdLogins IN (SELECT IdLogins FROM Customers)");
            while(rs.next()){ m7_c = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets) FROM Purchases as p WHERE MONTH(p.Date) = " + month.previous(8) + " AND p.IdLogins IN (SELECT IdLogins FROM Customers)");
            while(rs.next()){ m8_c = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets) FROM Purchases as p WHERE MONTH(p.Date) = " + month.previous(9) + " AND p.IdLogins IN (SELECT IdLogins FROM Customers)");
            while(rs.next()){ m9_c = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets) FROM Purchases as p WHERE MONTH(p.Date) = " + month.previous(10) + " AND p.IdLogins IN (SELECT IdLogins FROM Customers)");
            while(rs.next()){ m10_c = rs.getInt(1); }
            rs = stmt.executeQuery("SELECT SUM(p.NbrTickets) FROM Purchases as p WHERE MONTH(p.Date) = " + month.previous(11) + " AND p.IdLogins IN (SELECT IdLogins FROM Customers)");
            while(rs.next()){ m11_c = rs.getInt(1); }

            // defining customers series
            XYChart.Series seriesCustomers = new XYChart.Series();
            seriesCustomers.setName("Customers");

            seriesCustomers.getData().add(new XYChart.Data(one, m11_c));
            seriesCustomers.getData().add(new XYChart.Data(two, m10_c));
            seriesCustomers.getData().add(new XYChart.Data(three, m9_c));
            seriesCustomers.getData().add(new XYChart.Data(four, m8_c));
            seriesCustomers.getData().add(new XYChart.Data(five, m7_c));
            seriesCustomers.getData().add(new XYChart.Data(six, m6_c));
            seriesCustomers.getData().add(new XYChart.Data(seven, m5_c));
            seriesCustomers.getData().add(new XYChart.Data(eight, m4_c));
            seriesCustomers.getData().add(new XYChart.Data(nine, m3_c));
            seriesCustomers.getData().add(new XYChart.Data(ten, m2_c));
            seriesCustomers.getData().add(new XYChart.Data(eleven, m1_c));
            seriesCustomers.getData().add(new XYChart.Data(twelve, mCurrent_c));

            lineChart.getData().addAll(seriesEmployees, seriesCustomers);

            pane.getChildren().add(lineChart);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
