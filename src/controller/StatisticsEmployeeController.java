package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import model.Me;
import model.SceneManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.Observable;
import java.util.ResourceBundle;

public class StatisticsEmployeeController implements Initializable {

    @FXML ImageView picture;
    @FXML Label firstNameAndLastName;
    @FXML Pane pane;
    @FXML PieChart pieChart;
    @FXML LineChart lineChart;

    // credentials
    private final String url       = "jdbc:mysql://localhost:3306/popcornmovie";
    private final String user      = "root";
    private final String password  = "";

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

        } catch (SQLException e) {
            e.printStackTrace();
        }




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
    }
}
