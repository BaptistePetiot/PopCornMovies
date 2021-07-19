package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import main.PopCornMovie;
import model.Cinema;
import model.Me;
import model.Movie;
import model.SceneManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class MovieCustomerController implements Initializable {
    @FXML ImageView picture, ivMovie;
    @FXML Label firstNameAndLastName, labelTitle, labelGenre, labelDirector, labelCast, labelPlot, price;
    @FXML Pane pane;
    @FXML TextField tfNbrStudentDiscounts, tfNbrTickets;

    private int cost = 0;
    private String date;
    private int nbrTickets;

    // credentials
    private final String url       = "jdbc:mysql://localhost:3306/popcornmovie";
    private final String user      = "root";
    private final String password  = "";

    public MovieCustomerController(){
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date(System.currentTimeMillis());
        this.date = formatter.format(now);
    }

    @FXML
    protected void addPicture(){
        System.out.println("ADD PICTURE" );
        // DIALOG TO CHOOSE PICTURE
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG", "*.png"));
        File file = fileChooser.showOpenDialog(PopCornMovie.getStage());

        // SAVE PICTURE
        try{
            BufferedImage bImage = ImageIO.read(file);
            ImageIO.write(bImage, "png", new File("picture_" + Me.getId() + ".png"));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        // RETRIEVE PICTURE AND SET IT IN ACCOUNT ImageView
        try{
            file = new File("picture" + Me.getId() + ".png");
            Image image = new Image(file.toURI().toString());
            picture.setImage(image);                            //TODO : make it work!
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToOverview(ActionEvent actionEvent) {
        System.out.println("OVERVIEW CUSTOMER");
        try{
            SceneManager.loadScene("../view/customer-overview.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToMovies(ActionEvent actionEvent) {
        System.out.println("MOVIES CUSTOMER");
        try{
            SceneManager.loadScene("../view/customer-movies.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToPurchases(ActionEvent actionEvent) {
        System.out.println("PURCHASES CUSTOMER");
        try{
            SceneManager.loadScene("../view/customer-purchases.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToAccount(ActionEvent actionEvent) {
        System.out.println("ACCOUNT CUSTOMER");
        try{
            SceneManager.loadScene("../view/customer-account.fxml", 1400,800);
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

    public void goToPayment(ActionEvent actionEvent) {
        registerPurchase();

        System.out.println("PAYMENT CUSTOMER");
        try{
            SceneManager.loadScene("../view/customer-payment.fxml", 1100,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void registerPurchase() {
        // connect to DB
        Connection connection = null;

        try{
            // create a connection to the database
            connection = DriverManager.getConnection(url, user, password);

            // statement
            Statement stmt = connection.createStatement();
            ResultSet rs;

            // retrieve highest nbr
            rs = stmt.executeQuery("SELECT MAX(Id) FROM `purchases`");
            int maxId = 0;
            while(rs.next()){
                maxId = rs.getInt(1);
            }
            int purchaseId = maxId + 1;

            // add purchase to DB
            stmt.executeUpdate("INSERT INTO `purchases` (`Id`, `IdLogins`, `Title`, `NbrTickets`, `Date`, `Price`) VALUES ('  " + purchaseId + "', ' " + Me.getId() + "', ' "+ Me.getLookingAtMovie().getTitle() + "', '" + nbrTickets + "', '" + date + "', '" + cost + "');");

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void displayPrice(KeyEvent keyEvent) {
        if( keyEvent.getCode() == KeyCode.ENTER ) {
            nbrTickets = Integer.parseInt(tfNbrTickets.getText());
            int nbrStudentTickets = Integer.parseInt(tfNbrStudentDiscounts.getText());
            int nbrNormalTickets = nbrTickets - nbrStudentTickets;

            int studentPrice = 7;
            int normalPrice = 10;

            cost = nbrStudentTickets * studentPrice + nbrNormalTickets * normalPrice;

            price.setText(String.valueOf(cost));
        }
    }

    @FXML
    private void exit(){
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // theme
        if(Me.getTheme() == 0){
            pane.getStylesheets().remove("css/DarkTheme.css");
            pane.getStylesheets().add("css/LightTheme.css");
        }else if(Me.getTheme() == 1){
            pane.getStylesheets().remove("css/LightTheme.css");
            pane.getStylesheets().add("css/DarkTheme.css");
        }

        // picture
        File file = new File("picture" + Me.getId() + ".png");
        if(file.exists()){
            System.out.println("image exists");
            Image image = new Image(file.toURI().toString());
            picture.setImage(image);
        }else{
            System.out.println("image does not exist");
            picture.setImage(new Image(new File("@../imgs/circle.png").toURI().toString()));
        }

        firstNameAndLastName.setText(Me.getFirstName() + " " + Me.getLastName());

        // change fields according to specific movie
        Movie m = Me.getLookingAtMovie();
        labelTitle.setText(m.getTitle());
        labelGenre.setText(m.getGenre());
        labelDirector.setText(m.getDirector());
        labelCast.setText(m.getCast());
        /*String[] sentences = m.getPlot().split(".");
        StringBuilder plot = new StringBuilder();
        for(String s : sentences){
            plot.append(s).append("\n");
        }
        labelPlot.setText(plot.toString());
        System.out.println(plot.toString());*/
        labelPlot.setText(m.getPlot());

        ivMovie.setImage(new Image(m.getImageURL()));
    }



}
