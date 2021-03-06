package controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import model.Me;
import model.SceneManager;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller of the PAYMENT scene of the EMPLOYEE application
 * @author Baptiste Petiot
 */
public class PaymentEmployeeController implements Initializable {
    //Javafx elements
    @FXML
    ProgressBar pb;
    @FXML
    Label label, thx;
    @FXML
    Pane pane;
    @FXML
    Button btn;

    // NAVIGATION

    /***
     * function that loads the MOVIES scene of EMPLOYEE application
     * displays the list of movies available depending on their genre
     * add or remove a movie
     */
    @FXML
    private void goBackToMovies() {
        System.out.println("MOVIES EMPLOYEE");
        try {
            SceneManager.loadScene("../view/employee-movies.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * first method called for initialization
     * sets chosen theme
     * hide final button and final text
     * start the animation process
     *
     * @param location : URL
     * @param resources : ResourceBundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // theme
        if (Me.getTheme() == 0) {
            pane.getStylesheets().remove("css/DarkTheme.css");
            pane.getStylesheets().add("css/LightTheme.css");
        } else if (Me.getTheme() == 1) {
            pane.getStylesheets().remove("css/LightTheme.css");
            pane.getStylesheets().add("css/DarkTheme.css");
        }

        btn.setVisible(false);
        thx.setVisible(false);

        startProcess();
    }

    /***
     * animates the loading bar
     * simulates authorisation process from bank
     * sets final button and final text to visible once animation has ended
     */
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
