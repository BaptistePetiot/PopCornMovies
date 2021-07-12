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
/*
    public static void loadRightArea(Pane rightArea, String sceneName) throws IOException {
        rightArea.getChildren().removeAll();
        Node node = (Node) FXMLLoader.load(PopCornMovie.class.getResource(sceneName));
        rightArea.getChildren().add(node);
    }

    public static void loadPurchase(VBox pnItems, String sceneName) throws IOException {
        Node[] nodes = new Node[10];
        for(int i = 0; i < nodes.length; i++){
            pnItems.getChildren().removeAll();
            nodes[i] = (Node) FXMLLoader.load(PopCornMovie.class.getResource(sceneName));
            pnItems.getChildren().add(nodes[i]);
        }
    }

 */
}
