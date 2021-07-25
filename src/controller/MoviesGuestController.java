package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import model.Cinema;
import model.Me;
import model.Movie;
import model.SceneManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MoviesGuestController implements Initializable {

    @FXML
    SplitMenuButton splitMenu;
    @FXML
    GridPane gridPane;
    @FXML
    Pane pane;
    @FXML
    ScrollPane scrollPane;

    private int c, r;
    private HashMap<Pair<Integer, Integer>, Movie> allMoviesCoords, actionMoviesCoords, adventureMoviesCoords, fantasyMoviesCoords, documentaryMoviesCoords, scifiMoviesCoords, horrorMoviesCoords, animationMoviesCoords, thrillerMoviesCoords, comedyMoviesCoords, dramaMoviesCoords;

    // credentials
    private final String url = "jdbc:mysql://localhost:3306/popcornmovie";
    private final String user = "root";
    private final String password = "";

    public void goToOverview(ActionEvent actionEvent) {
        System.out.println("OVERVIEW GUEST");
        try {
            SceneManager.loadScene("../view/guest-overview.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void goToMovies(ActionEvent actionEvent) {
        System.out.println("MOVIES GUEST");
        try {
            SceneManager.loadScene("../view/guest-movies.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void goToSettings(ActionEvent actionEvent) {
        System.out.println("SETTINGS GUEST");
        try {
            SceneManager.loadScene("../view/guest-settings.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void signout(ActionEvent actionEvent) {
        System.out.println("SIGN OUT");
        try {
            SceneManager.loadScene("../view/login.fxml", 700, 400);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void exit() {
        System.exit(0);
    }

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

        allMoviesCoords = new HashMap<>();
        actionMoviesCoords = new HashMap<>();
        adventureMoviesCoords = new HashMap<>();
        fantasyMoviesCoords = new HashMap<>();
        documentaryMoviesCoords = new HashMap<>();
        scifiMoviesCoords = new HashMap<>();
        horrorMoviesCoords = new HashMap<>();
        animationMoviesCoords = new HashMap<>();
        thrillerMoviesCoords = new HashMap<>();
        comedyMoviesCoords = new HashMap<>();
        dramaMoviesCoords = new HashMap<>();
        all();

    }

    private void goToMovie(Movie m) {
        Me.setLookingAtMovie(m);
        try {
            SceneManager.loadScene("../view/guest-movie.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private void updateColumnAndRow(int i) {
        if ((i % 4) == 0 && i != 0) {
            r++;
            c = 0;
        } else {
            c++;
        }
    }

    public void all() {
        c = 0;
        r = 0;
        Cinema.refresh();

        splitMenu.setText("All");

        ArrayList<Movie> allMovies = Cinema.getMovies();

        // create new grid
        GridPane gridMovies = new GridPane();
        gridMovies.setPrefHeight(1103.0);
        gridMovies.setPrefWidth(1071.0);
        // set constraints
        for (int i = 0; i < 4; i++) {
            ColumnConstraints column = new ColumnConstraints(267);
            gridMovies.getColumnConstraints().add(column);
        }

        // add to scroll pane
        scrollPane.setContent(gridMovies);

        for (Movie m : allMovies) {
            System.out.println(m.getTitle());
        }

        for (int i = 0; i < allMovies.size(); i++) {
            Movie m = allMovies.get(i);

            Button btn = new Button();
            btn.getStyleClass().add("buttonMovie");
            btn.setOnAction(event -> {
                int x = GridPane.getColumnIndex(btn);
                int y = GridPane.getRowIndex(btn);
                System.out.println(allMoviesCoords.get(new Pair<>(x, y)).getTitle());
                goToMovie(allMoviesCoords.get(new Pair<>(x, y)));
            });

            ImageView iv = new ImageView();
            iv.setFitHeight(361.0);
            iv.setFitWidth(272.0);
            iv.setPickOnBounds(true);
            iv.setPreserveRatio(true);

            Image img = new Image(m.getImageURL());
            iv.setImage(img);

            btn.setGraphic(iv);

            gridMovies.add(btn, c, r, 1, 1);
            allMoviesCoords.put(new Pair<>(c, r), m);

            updateColumnAndRow(i);

        }
    }

    public void action(ActionEvent actionEvent) {
        c = 0;
        r = 0;
        Cinema.refresh();

        splitMenu.setText("Action");

        ArrayList<Movie> actionMovies = Cinema.getMovies();
        actionMovies.removeIf(m -> !m.getGenre().equals("Action"));

        // Clear nodes
        //gridMovies.getChildren().clear();

        // create new grid
        GridPane gridMovies = new GridPane();
        gridMovies.setPrefHeight(1103.0);
        gridMovies.setPrefWidth(1071.0);
        //gridMovies.getChildren().clear();   // Clear nodes
        // set constraints
        for (int i = 0; i < 4; i++) {
            ColumnConstraints column = new ColumnConstraints(267);
            gridMovies.getColumnConstraints().add(column);
        }

        // add to scroll pane
        scrollPane.setContent(gridMovies);

        for (Movie m : actionMovies) {
            System.out.println(m.getTitle());
        }

        for (int i = 0; i < actionMovies.size(); i++) {
            Movie m = actionMovies.get(i);

            Button btn = new Button();
            btn.getStyleClass().add("buttonMovie");
            btn.setOnAction(event -> {
                int x = GridPane.getColumnIndex(btn);
                int y = GridPane.getRowIndex(btn);
                System.out.println(actionMoviesCoords.get(new Pair<>(x, y)).getTitle());
                goToMovie(actionMoviesCoords.get(new Pair<>(x, y)));
            });

            ImageView iv = new ImageView();
            iv.setFitHeight(361.0);
            iv.setFitWidth(272.0);
            iv.setPickOnBounds(true);
            iv.setPreserveRatio(true);

            Image img = new Image(m.getImageURL());
            iv.setImage(img);

            btn.setGraphic(iv);

            gridMovies.add(btn, c, r, 1, 1);
            actionMoviesCoords.put(new Pair<>(c, r), m);

            updateColumnAndRow(i);

        }
    }

    public void adventure(ActionEvent actionEvent) {
        c = 0;
        r = 0;
        Cinema.refresh();

        splitMenu.setText("Adventure");

        ArrayList<Movie> adventureyMovies = Cinema.getMovies();
        adventureyMovies.removeIf(m -> !m.getGenre().equals("Adventure"));

        // Clear nodes
        //gridMovies.getChildren().clear();

        // create new grid
        GridPane gridMovies = new GridPane();
        gridMovies.setPrefHeight(1103.0);
        gridMovies.setPrefWidth(1071.0);
        //gridMovies.getChildren().clear();   // Clear nodes
        // set constraints
        for (int i = 0; i < 4; i++) {
            ColumnConstraints column = new ColumnConstraints(267);
            gridMovies.getColumnConstraints().add(column);
        }

        // add to scroll pane
        scrollPane.setContent(gridMovies);

        for (Movie m : adventureyMovies) {
            System.out.println(m.getTitle());
        }

        for (int i = 0; i < adventureyMovies.size(); i++) {
            Movie m = adventureyMovies.get(i);

            Button btn = new Button();
            btn.getStyleClass().add("buttonMovie");
            btn.setOnAction(event -> {
                int x = GridPane.getColumnIndex(btn);
                int y = GridPane.getRowIndex(btn);
                System.out.println(adventureMoviesCoords.get(new Pair<>(x, y)).getTitle());
                goToMovie(adventureMoviesCoords.get(new Pair<>(x, y)));
            });

            ImageView iv = new ImageView();
            iv.setFitHeight(361.0);
            iv.setFitWidth(272.0);
            iv.setPickOnBounds(true);
            iv.setPreserveRatio(true);

            Image img = new Image(m.getImageURL());
            iv.setImage(img);

            btn.setGraphic(iv);

            gridMovies.add(btn, c, r, 1, 1);
            adventureMoviesCoords.put(new Pair<>(c, r), m);

            updateColumnAndRow(i);

        }
    }

    public void fantasy(ActionEvent actionEvent) {
        c = 0;
        r = 0;
        // refresh available movies
        Cinema.refresh();

        splitMenu.setText("Fantasy");

        // retrieve the fantasy Movies
        ArrayList<Movie> fantasyMovies = Cinema.getMovies();
        fantasyMovies.removeIf(m -> !m.getGenre().equals("Fantasy"));

        // Clear nodes
        //gridMovies.getChildren().clear();

        // create new grid
        GridPane gridMovies = new GridPane();
        gridMovies.setPrefHeight(1103.0);
        gridMovies.setPrefWidth(1071.0);
        //gridMovies.getChildren().clear();   // Clear nodes
        // set constraints
        for (int i = 0; i < 4; i++) {
            ColumnConstraints column = new ColumnConstraints(267);
            gridMovies.getColumnConstraints().add(column);
        }

        // add to scroll pane
        scrollPane.setContent(gridMovies);

        for (Movie m : fantasyMovies) {
            System.out.println(m.getTitle());
        }

        for (int i = 0; i < fantasyMovies.size(); i++) {
            Movie m = fantasyMovies.get(i);

            Button btn = new Button();
            btn.getStyleClass().add("buttonMovie");
            btn.setOnAction(event -> {
                int x = GridPane.getColumnIndex(btn);
                int y = GridPane.getRowIndex(btn);
                System.out.println(fantasyMoviesCoords.get(new Pair<>(x, y)).getTitle());
                goToMovie(fantasyMoviesCoords.get(new Pair<>(x, y)));
            });

            ImageView iv = new ImageView();
            iv.setFitHeight(361.0);
            iv.setFitWidth(272.0);
            iv.setPickOnBounds(true);
            iv.setPreserveRatio(true);

            Image img = new Image(m.getImageURL());
            iv.setImage(img);

            btn.setGraphic(iv);

            gridMovies.add(btn, c, r, 1, 1);
            fantasyMoviesCoords.put(new Pair<>(c, r), m);

            updateColumnAndRow(i);

        }


    }

    public void documentary(ActionEvent actionEvent) {
        c = 0;
        r = 0;
        Cinema.refresh();

        splitMenu.setText("Documentary");

        ArrayList<Movie> documentaryMovies = Cinema.getMovies();
        documentaryMovies.removeIf(m -> !m.getGenre().equals("Documentary"));

        // Clear nodes
        //gridMovies.getChildren().clear();

        // create new grid
        GridPane gridMovies = new GridPane();
        gridMovies.setPrefHeight(1103.0);
        gridMovies.setPrefWidth(1071.0);
        //gridMovies.getChildren().clear();   // Clear nodes
        // set constraints
        for (int i = 0; i < 4; i++) {
            ColumnConstraints column = new ColumnConstraints(267);
            gridMovies.getColumnConstraints().add(column);
        }

        // add to scroll pane
        scrollPane.setContent(gridMovies);

        for (Movie m : documentaryMovies) {
            System.out.println(m.getTitle());
        }

        for (int i = 0; i < documentaryMovies.size(); i++) {
            Movie m = documentaryMovies.get(i);

            Button btn = new Button();
            btn.getStyleClass().add("buttonMovie");
            btn.setOnAction(event -> {
                int x = GridPane.getColumnIndex(btn);
                int y = GridPane.getRowIndex(btn);
                System.out.println(documentaryMoviesCoords.get(new Pair<>(x, y)).getTitle());
                goToMovie(documentaryMoviesCoords.get(new Pair<>(x, y)));
            });

            ImageView iv = new ImageView();
            iv.setFitHeight(361.0);
            iv.setFitWidth(272.0);
            iv.setPickOnBounds(true);
            iv.setPreserveRatio(true);

            Image img = new Image(m.getImageURL());
            iv.setImage(img);

            btn.setGraphic(iv);

            gridMovies.add(btn, c, r, 1, 1);
            documentaryMoviesCoords.put(new Pair<>(c, r), m);

            updateColumnAndRow(i);

        }
    }

    public void scifi(ActionEvent actionEvent) {
        c = 0;
        r = 0;
        Cinema.refresh();

        splitMenu.setText("Science Fiction");

        ArrayList<Movie> scifiMovies = Cinema.getMovies();
        scifiMovies.removeIf(m -> !m.getGenre().equals("Science Fiction"));

        // Clear nodes
        //gridMovies.getChildren().clear();

        // create new grid
        GridPane gridMovies = new GridPane();
        gridMovies.setPrefHeight(1103.0);
        gridMovies.setPrefWidth(1071.0);
        //gridMovies.getChildren().clear();   // Clear nodes
        // set constraints
        for (int i = 0; i < 4; i++) {
            ColumnConstraints column = new ColumnConstraints(267);
            gridMovies.getColumnConstraints().add(column);
        }

        // add to scroll pane
        scrollPane.setContent(gridMovies);

        for (Movie m : scifiMovies) {
            System.out.println(m.getTitle());
        }

        for (int i = 0; i < scifiMovies.size(); i++) {
            Movie m = scifiMovies.get(i);

            Button btn = new Button();
            btn.getStyleClass().add("buttonMovie");
            btn.setOnAction(event -> {
                int x = GridPane.getColumnIndex(btn);
                int y = GridPane.getRowIndex(btn);
                System.out.println(scifiMoviesCoords.get(new Pair<>(x, y)).getTitle());
                goToMovie(scifiMoviesCoords.get(new Pair<>(x, y)));
            });

            ImageView iv = new ImageView();
            iv.setFitHeight(361.0);
            iv.setFitWidth(272.0);
            iv.setPickOnBounds(true);
            iv.setPreserveRatio(true);

            Image img = new Image(m.getImageURL());
            iv.setImage(img);

            btn.setGraphic(iv);

            gridMovies.add(btn, c, r, 1, 1);
            scifiMoviesCoords.put(new Pair<>(c, r), m);

            updateColumnAndRow(i);

        }
    }

    public void horror(ActionEvent actionEvent) {
        c = 0;
        r = 0;
        Cinema.refresh();

        splitMenu.setText("Horror");

        ArrayList<Movie> horrorMovies = Cinema.getMovies();
        horrorMovies.removeIf(m -> !m.getGenre().equals("Horror"));

        // Clear nodes
        //gridMovies.getChildren().clear();

        // create new grid
        GridPane gridMovies = new GridPane();
        gridMovies.setPrefHeight(1103.0);
        gridMovies.setPrefWidth(1071.0);
        //gridMovies.getChildren().clear();   // Clear nodes
        // set constraints
        for (int i = 0; i < 4; i++) {
            ColumnConstraints column = new ColumnConstraints(267);
            gridMovies.getColumnConstraints().add(column);
        }

        // add to scroll pane
        scrollPane.setContent(gridMovies);

        for (Movie m : horrorMovies) {
            System.out.println(m.getTitle());
        }

        for (int i = 0; i < horrorMovies.size(); i++) {
            Movie m = horrorMovies.get(i);

            Button btn = new Button();
            btn.getStyleClass().add("buttonMovie");
            btn.setOnAction(event -> {
                int x = GridPane.getColumnIndex(btn);
                int y = GridPane.getRowIndex(btn);
                System.out.println(horrorMoviesCoords.get(new Pair<>(x, y)).getTitle());
                goToMovie(horrorMoviesCoords.get(new Pair<>(x, y)));
            });

            ImageView iv = new ImageView();
            iv.setFitHeight(361.0);
            iv.setFitWidth(272.0);
            iv.setPickOnBounds(true);
            iv.setPreserveRatio(true);

            Image img = new Image(m.getImageURL());
            iv.setImage(img);

            btn.setGraphic(iv);

            gridMovies.add(btn, c, r, 1, 1);
            horrorMoviesCoords.put(new Pair<>(c, r), m);

            updateColumnAndRow(i);

        }

    }

    public void animation(ActionEvent actionEvent) {
        c = 0;
        r = 0;
        Cinema.refresh();

        splitMenu.setText("Animation");

        ArrayList<Movie> animationMovies = Cinema.getMovies();
        animationMovies.removeIf(m -> !m.getGenre().equals("Animation"));

        // Clear nodes
        //gridMovies.getChildren().clear();

        // create new grid
        GridPane gridMovies = new GridPane();
        gridMovies.setPrefHeight(1103.0);
        gridMovies.setPrefWidth(1071.0);
        //gridMovies.getChildren().clear();   // Clear nodes
        // set constraints
        for (int i = 0; i < 4; i++) {
            ColumnConstraints column = new ColumnConstraints(267);
            gridMovies.getColumnConstraints().add(column);
        }

        // add to scroll pane
        scrollPane.setContent(gridMovies);

        for (Movie m : animationMovies) {
            System.out.println(m.getTitle());
        }

        for (int i = 0; i < animationMovies.size(); i++) {
            Movie m = animationMovies.get(i);

            Button btn = new Button();
            btn.getStyleClass().add("buttonMovie");
            btn.setOnAction(event -> {
                int x = GridPane.getColumnIndex(btn);
                int y = GridPane.getRowIndex(btn);
                System.out.println(animationMoviesCoords.get(new Pair<>(x, y)).getTitle());
                goToMovie(animationMoviesCoords.get(new Pair<>(x, y)));
            });

            ImageView iv = new ImageView();
            iv.setFitHeight(361.0);
            iv.setFitWidth(272.0);
            iv.setPickOnBounds(true);
            iv.setPreserveRatio(true);

            Image img = new Image(m.getImageURL());
            iv.setImage(img);

            btn.setGraphic(iv);

            gridMovies.add(btn, c, r, 1, 1);
            animationMoviesCoords.put(new Pair<>(c, r), m);

            updateColumnAndRow(i);
        }
    }

    public void thriller(ActionEvent actionEvent) {
        c = 0;
        r = 0;
        Cinema.refresh();

        splitMenu.setText("Thriller");

        ArrayList<Movie> thrillerMovies = Cinema.getMovies();
        thrillerMovies.removeIf(m -> !m.getGenre().equals("Thriller"));

        // Clear nodes
        //gridMovies.getChildren().clear();

        // create new grid
        GridPane gridMovies = new GridPane();
        gridMovies.setPrefHeight(1103.0);
        gridMovies.setPrefWidth(1071.0);
        //gridMovies.getChildren().clear();   // Clear nodes
        // set constraints
        for (int i = 0; i < 4; i++) {
            ColumnConstraints column = new ColumnConstraints(267);
            gridMovies.getColumnConstraints().add(column);
        }

        // add to scroll pane
        scrollPane.setContent(gridMovies);

        for (Movie m : thrillerMovies) {
            System.out.println(m.getTitle());
        }

        for (int i = 0; i < thrillerMovies.size(); i++) {
            Movie m = thrillerMovies.get(i);

            Button btn = new Button();
            btn.getStyleClass().add("buttonMovie");
            btn.setOnAction(event -> {
                int x = GridPane.getColumnIndex(btn);
                int y = GridPane.getRowIndex(btn);
                System.out.println(thrillerMoviesCoords.get(new Pair<>(x, y)).getTitle());
                goToMovie(thrillerMoviesCoords.get(new Pair<>(x, y)));
            });

            ImageView iv = new ImageView();
            iv.setFitHeight(361.0);
            iv.setFitWidth(272.0);
            iv.setPickOnBounds(true);
            iv.setPreserveRatio(true);

            Image img = new Image(m.getImageURL());
            iv.setImage(img);

            btn.setGraphic(iv);

            gridMovies.add(btn, c, r, 1, 1);
            thrillerMoviesCoords.put(new Pair<>(c, r), m);

            updateColumnAndRow(i);

        }
    }

    public void comedy(ActionEvent actionEvent) {
        c = 0;
        r = 0;
        Cinema.refresh();

        splitMenu.setText("Comedy");

        ArrayList<Movie> comedyMovies = Cinema.getMovies();
        comedyMovies.removeIf(m -> !m.getGenre().equals("Comedy"));

        // Clear nodes
        //gridMovies.getChildren().clear();

        // create new grid
        GridPane gridMovies = new GridPane();
        gridMovies.setPrefHeight(1103.0);
        gridMovies.setPrefWidth(1071.0);
        //gridMovies.getChildren().clear();   // Clear nodes
        // set constraints
        for (int i = 0; i < 4; i++) {
            ColumnConstraints column = new ColumnConstraints(267);
            gridMovies.getColumnConstraints().add(column);
        }

        // add to scroll pane
        scrollPane.setContent(gridMovies);

        for (Movie m : comedyMovies) {
            System.out.println(m.getTitle());
        }

        for (int i = 0; i < comedyMovies.size(); i++) {
            Movie m = comedyMovies.get(i);

            Button btn = new Button();
            btn.getStyleClass().add("buttonMovie");
            btn.setOnAction(event -> {
                int x = GridPane.getColumnIndex(btn);
                int y = GridPane.getRowIndex(btn);
                System.out.println(comedyMoviesCoords.get(new Pair<>(x, y)).getTitle());
                goToMovie(comedyMoviesCoords.get(new Pair<>(x, y)));
            });

            ImageView iv = new ImageView();
            iv.setFitHeight(361.0);
            iv.setFitWidth(272.0);
            iv.setPickOnBounds(true);
            iv.setPreserveRatio(true);

            Image img = new Image(m.getImageURL());
            iv.setImage(img);

            btn.setGraphic(iv);

            gridMovies.add(btn, c, r, 1, 1);
            comedyMoviesCoords.put(new Pair<>(c, r), m);

            updateColumnAndRow(i);
        }
    }

    public void drama(ActionEvent actionEvent) {
        c = 0;
        r = 0;
        Cinema.refresh();

        splitMenu.setText("Drama");

        ArrayList<Movie> dramaMovies = Cinema.getMovies();
        dramaMovies.removeIf(m -> !m.getGenre().equals("Drama"));

        // Clear nodes
        //gridMovies.getChildren().clear();

        // create new grid
        GridPane gridMovies = new GridPane();
        gridMovies.setPrefHeight(1103.0);
        gridMovies.setPrefWidth(1071.0);
        //gridMovies.getChildren().clear();   // Clear nodes
        // set constraints
        for (int i = 0; i < 4; i++) {
            ColumnConstraints column = new ColumnConstraints(267);
            gridMovies.getColumnConstraints().add(column);
        }

        // add to scroll pane
        scrollPane.setContent(gridMovies);

        for (Movie m : dramaMovies) {
            System.out.println(m.getTitle());
        }

        for (int i = 0; i < dramaMovies.size(); i++) {
            Movie m = dramaMovies.get(i);

            Button btn = new Button();
            btn.getStyleClass().add("buttonMovie");
            btn.setOnAction(event -> {
                int x = GridPane.getColumnIndex(btn);
                int y = GridPane.getRowIndex(btn);
                System.out.println(dramaMoviesCoords.get(new Pair<>(x, y)).getTitle());
                goToMovie(dramaMoviesCoords.get(new Pair<>(x, y)));
            });

            ImageView iv = new ImageView();
            iv.setFitHeight(361.0);
            iv.setFitWidth(272.0);
            iv.setPickOnBounds(true);
            iv.setPreserveRatio(true);

            Image img = new Image(m.getImageURL());
            iv.setImage(img);

            btn.setGraphic(iv);

            gridMovies.add(btn, c, r, 1, 1);
            dramaMoviesCoords.put(new Pair<>(c, r), m);

            updateColumnAndRow(i);
        }
    }
}

