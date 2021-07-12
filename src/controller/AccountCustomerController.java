package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import main.PopCornMovie;
import model.Me;
import model.SceneManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AccountCustomerController implements Initializable {

    @FXML ImageView picture;
    @FXML Label firstNameAndLastName;

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

    @FXML
    private void exit(){
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
    }
}
