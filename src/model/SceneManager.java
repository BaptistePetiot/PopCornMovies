package model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.PopCornMovie;

import java.io.IOException;

public class SceneManager {

    public static void loadScene(String sceneName, int width, int height) throws IOException {

        FXMLLoader loader = new FXMLLoader(PopCornMovie.class.getResource(sceneName));
        PopCornMovie.setRoot(loader.load());
        Scene scene = new Scene(PopCornMovie.getRoot(), width, height);
        PopCornMovie.getStage().setScene(scene);
        PopCornMovie.getStage().show();

        PopCornMovie.getRoot().setOnMousePressed(event -> {
            PopCornMovie.setX(event.getSceneX());
            PopCornMovie.setY(event.getSceneY());
        });
        PopCornMovie.getRoot().setOnMouseDragged(event -> {
            PopCornMovie.getStage().setX(event.getScreenX() - PopCornMovie.getX());
            PopCornMovie.getStage().setY(event.getScreenY() - PopCornMovie.getY());
        });
    }
}
