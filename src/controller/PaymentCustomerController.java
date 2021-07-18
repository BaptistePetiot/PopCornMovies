package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import model.FillProgressIndicator;

import java.net.URL;
import java.util.ResourceBundle;

public class PaymentCustomerController implements Initializable {
    private FillProgressIndicator indicator;
    private myThread mt;

    @FXML Pane pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pane.getChildren().clear();
        indicator = new FillProgressIndicator();
        indicator.makeIndeterminate();
        indicator.setLayoutX(350);
        indicator.setLayoutY(250);
        mt = new myThread(indicator, 20);
        /*mt.start();
        pane.getChildren().add(indicator);*/
    }

    class myThread extends Thread{
        FillProgressIndicator fpi;
        int progress = 0;
        int delay;

        public myThread(FillProgressIndicator fpi, int delay){
            this.fpi = fpi;
            this.delay = delay;
        }

        @Override
        public void run(){
            while(progress < 100){
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Platform.runLater(() -> {
                    fpi.setProgress(progress);
                });

                progress++;
            }
        }

        public int getProgress() {
            return progress;
        }
    }
}
