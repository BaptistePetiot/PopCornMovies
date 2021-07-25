package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

/**
 * PopCorn Movie application
 * @author Baptiste Petiot
 */
public class PopCornMovie extends Application {

    private static Parent root;
    private static Stage stage;
    private static double x, y;

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/login.fxml")));

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

    /**
     * allows to set the root of the app
     * @param root : Parent
     */
    public static void setRoot(Parent root) {
        PopCornMovie.root = root;
    }

    /**
     * allows to get the root of the app
     * @return root : Parent
     */
    public static Parent getRoot() {
        return root;
    }

    /**
     * allows to get the Stage of the app
     * @return stage : Stage
     */
    public static Stage getStage() {
        return stage;
    }

    /**
     * allows to move the window on the screen
     * @param x : double
     */
    public static void setX(double x) {
        PopCornMovie.x = x;
    }

    /**
     * allows to get the x position of the window on the screen
     * @return x : double
     */
    public static double getX() {
        return x;
    }

    /**
     * allows to move the window on the screen
     * @param y : double
     */
    public static void setY(double y) {
        PopCornMovie.y = y;
    }

    /**
     * allows to get the y position of the window on the screen
     * @return y : double
     */
    public static double getY() {
        return y;
    }

    /**
     * main method that launches the program
     * @param args : String[]
     */
    public static void main(String[] args) {
        launch(args);
    }
}
