package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PopCornMovie extends Application {

    private static Parent root;
    private static Stage stage;
    private static double x, y;

    @Override
    public void start(Stage primaryStage) throws Exception{
        root = FXMLLoader.load(getClass().getResource("../view/login.fxml"));

        stage = primaryStage;
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root, 700, 400));
        stage.show();

        // allows to move the window with the cursor
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });
    }

    public static void setRoot(Parent root) {
        PopCornMovie.root = root;
    }

    public static Parent getRoot() {
        return root;
    }

    public static void setStage(Stage stage) {
        PopCornMovie.stage = stage;
    }

    public static Stage getStage() {
        return stage;
    }

    public static void setX(double x) {
        PopCornMovie.x = x;
    }

    public static double getX() {
        return x;
    }

    public static void setY(double y) {
        PopCornMovie.y = y;
    }

    public static double getY() {
        return y;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
