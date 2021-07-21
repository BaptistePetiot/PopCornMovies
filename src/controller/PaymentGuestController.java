package controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import model.Me;
import model.SceneManager;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

public class PaymentGuestController implements Initializable {

    @FXML ProgressBar pb;
    @FXML Label label, thx;
    @FXML Pane pane;
    @FXML Button btn;

    @FXML
    private void goBackToMovies(){
        System.out.println("MOVIES GUEST");
        try{
            SceneManager.loadScene("../view/guest-movies.fxml", 1400,800);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
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

        btn.setVisible(false);
        thx.setVisible(false);

        //animate();
        startProcess();
    }

    private void startProcess() {

        // Create a background Task
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                // Set the total number of steps in our process
                int steps = 1000;

                // Simulate a long running task
                for (int i = 0; i < steps; i++) {

                    Thread.sleep(3); // Pause briefly

                    // Update our progress and message properties
                    updateProgress(i, steps);
                    updateMessage(String.valueOf(i));
                }
                return null;
            }
        };

        // This method allows us to handle any Exceptions thrown by the task
        task.setOnFailed(wse -> {
            wse.getSource().getException().printStackTrace();
        });

        // If the task completed successfully, perform other updates here
        task.setOnSucceeded(wse -> {
            label.setVisible(false);
            thx.setVisible(true);
            btn.setVisible(true);
        });

        // Before starting our task, we need to bind our UI values to the properties on the task
        pb.progressProperty().bind(task.progressProperty());

        // Now, start the task on a background thread
        new Thread(task).start();
    }

}
