package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MoviesCustomerController implements Initializable {
    // credentials
    private final String url = "jdbc:mysql://localhost:3306/popcornmovie";
    private final String user = "root";
    private final String password = "";

    // Javafx elements
    @FXML
    ImageView picture;
    @FXML
    Label firstNameAndLastName;
    @FXML
    SplitMenuButton splitMenu;
    //@FXML GridPane gridMovies;
    @FXML
    Pane pane;
    @FXML
    ScrollPane scrollPane;

    // class attributes
    private int c, r;
    private HashMap<Pair<Integer, Integer>, Movie> allMoviesCoords, actionMoviesCoords, adventureMoviesCoords, fantasyMoviesCoords, documentaryMoviesCoords, scifiMoviesCoords, horrorMoviesCoords, animationMoviesCoords, thrillerMoviesCoords, comedyMoviesCoords, dramaMoviesCoords;

    /***
     * loads the user picture in the dedicated ImageView
     */
    private void loadPicture() {
        try {
            File img = new File("picture.jpg");
            FileOutputStream ostreamImage = new FileOutputStream(img);

            // create a connection to the database
            Connection connection = DriverManager.getConnection(url, user, password);
            // prepared statement
            PreparedStatement ps = connection.prepareStatement("SELECT picture FROM pictures WHERE IdLogins=?");

            try {
                ps.setInt(1, Me.getId());
                ResultSet rs = ps.executeQuery();

                try {
                    if (rs.next()) {
                        InputStream istreamImage = rs.getBinaryStream("picture");

                        byte[] buffer = new byte[1024];
                        int length = 0;

                        while ((length = istreamImage.read(buffer)) != -1) {
                            ostreamImage.write(buffer, 0, length);  // save image locally
                        }

                        // set image
                        Image image = new Image(img.toURI().toString());
                        picture.setImage(image);
                    }
                } finally {
                    rs.close();
                }
            } finally {
                ostreamImage.close();
                ps.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // NAVIGATION

    /***
     * function that loads the OVERVIEW scene of CUSTOMER application
     * scene that displays the 2 most attractive movies of the moment
     * and the most interesting discount that is currently active
     */
    public void goToOverview() {
        System.out.println("OVERVIEW CUSTOMER");
        try {
            SceneManager.loadScene("../view/customer-overview.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the MOVIES scene of CUSTOMER application
     * displays the list of movies available depending on their genre
     */
    public void goToMovies() {
        System.out.println("MOVIES CUSTOMER");
        try {
            SceneManager.loadScene("../view/customer-movies.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the MOVIE scene of CUSTOMER application
     * displays all information about the specific movie selected
     * @param m : Movie (the selected movie)
     */
    private void goToMovie(Movie m) {
        Me.setLookingAtMovie(m);
        try {
            SceneManager.loadScene("../view/customer-movie.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the PURCHASES scene of CUSTOMER application
     * displays the total number of tickets bought, the number of tickets bought in the last 12 months and in the current month
     * displays all purchases of the user in a scrollable area
     */
    public void goToPurchases() {
        System.out.println("PURCHASES CUSTOMER");
        try {
            SceneManager.loadScene("../view/customer-purchases.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the ACCOUNT scene of CUSTOMER application
     * displays the date of creation of the account
     * lets the user select the appropriate category for the account (regular, senior or child)
     * lets the user select the theme of his choice (light or dark)
     * lets the user add a picture or change the current one
     * lets the user delete the account
     */
    public void goToAccount() {
        System.out.println("ACCOUNT CUSTOMER");
        try {
            SceneManager.loadScene("../view/customer-account.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that signs the user out and loads the LOGIN scene
     */
    public void signout() {
        System.out.println("SIGN OUT");
        try {
            SceneManager.loadScene("../view/login.fxml", 700, 400);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * exit the CUSTOMER application
     */
    @FXML
    private void exit() {
        System.exit(0);
    }

    /***
     * first method called for initialization
     * loads user picture
     * sets chosen theme
     *
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // load picture
        try {
            loadPicture();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // theme
        if (Me.getTheme() == 0) {
            pane.getStylesheets().remove("css/DarkTheme.css");
            pane.getStylesheets().add("css/LightTheme.css");
        } else if (Me.getTheme() == 1) {
            pane.getStylesheets().remove("css/LightTheme.css");
            pane.getStylesheets().add("css/DarkTheme.css");
        }

        // initializes all hashmaps
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

        firstNameAndLastName.setText(Me.getFirstName() + " " + Me.getLastName());
        all();  // displays all movies by default
    }

    /***
     * updates the column and the row given a certain index
     * @param i : int (index)
     */
    private void updateColumnAndRow(int i) {
        if ((i % 4) == 0 && i != 0) {
            r++;
            c = 0;
        } else {
            c++;
        }
    }

    /***
     * retrieves and displays all movies from the DB
     */
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

    /***
     * retrieves and displays all ACTION movies from the DB
     */
    public void action() {
        c = 0;
        r = 0;
        Cinema.refresh();

        splitMenu.setText("Action");

        ArrayList<Movie> actionMovies = Cinema.getMovies();
        actionMovies.removeIf(m -> !m.getGenre().equals("Action"));

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

    /***
     * retrieves and displays all ADVENTURE movies from the DB
     */
    public void adventure() {
        c = 0;
        r = 0;
        Cinema.refresh();

        splitMenu.setText("Adventure");

        ArrayList<Movie> adventureyMovies = Cinema.getMovies();
        adventureyMovies.removeIf(m -> !m.getGenre().equals("Adventure"));

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

    /***
     * retrieves and displays all FANTASY movies from the DB
     */
    public void fantasy() {
        c = 0;
        r = 0;
        // refresh available movies
        Cinema.refresh();

        splitMenu.setText("Fantasy");

        // retrieve the fantasy Movies
        ArrayList<Movie> fantasyMovies = Cinema.getMovies();
        fantasyMovies.removeIf(m -> !m.getGenre().equals("Fantasy"));

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

    /***
     * retrieves and displays all DOCUMENTARY movies from the DB
     */
    public void documentary() {
        c = 0;
        r = 0;
        Cinema.refresh();

        splitMenu.setText("Documentary");

        ArrayList<Movie> documentaryMovies = Cinema.getMovies();
        documentaryMovies.removeIf(m -> !m.getGenre().equals("Documentary"));

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

    /***
     * retrieves and displays all SCIENCE FICTION movies from the DB
     */
    public void scifi() {
        c = 0;
        r = 0;
        Cinema.refresh();

        splitMenu.setText("Science Fiction");

        ArrayList<Movie> scifiMovies = Cinema.getMovies();
        scifiMovies.removeIf(m -> !m.getGenre().equals("Science Fiction"));

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

    /***
     * retrieves and displays all HORROR movies from the DB
     */
    public void horror() {
        c = 0;
        r = 0;
        Cinema.refresh();

        splitMenu.setText("Horror");

        ArrayList<Movie> horrorMovies = Cinema.getMovies();
        horrorMovies.removeIf(m -> !m.getGenre().equals("Horror"));

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

    /***
     * retrieves and displays all ANIMATION movies from the DB
     */
    public void animation() {
        c = 0;
        r = 0;
        Cinema.refresh();

        splitMenu.setText("Animation");

        ArrayList<Movie> animationMovies = Cinema.getMovies();
        animationMovies.removeIf(m -> !m.getGenre().equals("Animation"));

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

    /***
     * retrieves and displays all THRILLER movies from the DB
     */
    public void thriller() {
        c = 0;
        r = 0;
        Cinema.refresh();

        splitMenu.setText("Thriller");

        ArrayList<Movie> thrillerMovies = Cinema.getMovies();
        thrillerMovies.removeIf(m -> !m.getGenre().equals("Thriller"));

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

    /***
     * retrieves and displays all COMEDY movies from the DB
     */
    public void comedy() {
        c = 0;
        r = 0;
        Cinema.refresh();

        splitMenu.setText("Comedy");

        ArrayList<Movie> comedyMovies = Cinema.getMovies();
        comedyMovies.removeIf(m -> !m.getGenre().equals("Comedy"));

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

    /***
     * retrieves and displays all DRAMA movies from the DB
     */
    public void drama() {
        c = 0;
        r = 0;
        Cinema.refresh();

        splitMenu.setText("Drama");

        ArrayList<Movie> dramaMovies = Cinema.getMovies();
        dramaMovies.removeIf(m -> !m.getGenre().equals("Drama"));

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
