package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MoviesEmployeeController implements Initializable {
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
    @FXML
    Pane pane;
    @FXML
    Button minus;
    @FXML
    ScrollPane scrollPane;

    // class attributes
    private int c, r;
    private HashMap<Pair<Integer, Integer>, Movie> allMoviesCoords, actionMoviesCoords, adventureMoviesCoords, fantasyMoviesCoords, documentaryMoviesCoords, scifiMoviesCoords, horrorMoviesCoords, animationMoviesCoords, thrillerMoviesCoords, comedyMoviesCoords, dramaMoviesCoords;
    private boolean minusSelected;

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

    /**
     * allws to then click a movie to delete it
     */
    @FXML
    public void selectMinus() {
        if (minusSelected) {
            minusSelected = false;
            minus.getStyleClass().remove("minusSelected");
            minus.getStyleClass().add("minusNotSelected");
        } else {
            minusSelected = true;
            minus.getStyleClass().remove("minusNotSelected");
            minus.getStyleClass().add("minusSelected");
        }
    }

    /**
     * removes the specified movie from the DB
     * @param m : Movie (the movie to delete)
     * @param option : String (the page to refresh)
     */
    public void deleteMovie(Movie m, String option) {

        int idMovieDell = m.getId();
        System.out.println(idMovieDell);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this movie ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            // connect to DB
            Connection connection = null;
            try {
                // create a connection to the database
                connection = DriverManager.getConnection(url, user, password);

                // statement
                Statement stmt = connection.createStatement();

                // delete movie
                stmt.executeUpdate("DELETE FROM movies WHERE Id = " + idMovieDell);

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        Cinema.refresh();
        switch (option) {
            case "all":
                all();
                break;
            case "action":
                action();
                break;
            case "drama":
                drama();
                break;
            case "horror":
                horror();
                break;
            case "science fiction":
                scifi();
                break;
            case "documentary":
                documentary();
                break;
            case "animation":
                animation();
                break;
            case "comedy":
                comedy();
                break;
            case "fantasy":
                fantasy();
                break;
            case "adventure":
                adventure();
                break;
            case "thriller":
                thriller();
                break;
        }
    }

    /***
     * function that loads the NEW MOVIE scene of EMPLOYEE application
     */
    public void plusMovie() {
        System.out.println("PLUS MOVIE");
        try {
            SceneManager.loadScene("../view/employee-new-movie.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the Movie scene of EMPLOYEE application
     */
    private void goToMovie(Movie m) {
        Me.setLookingAtMovie(m);
        try {
            SceneManager.loadScene("../view/employee-movie.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the OVERVIEW scene of EMPLOYEE application
     * scene that displays the 2 most attractive movies of the moment
     * and the most interesting discount that is currently active
     */
    public void goToOverview() {
        System.out.println("OVERVIEW EMPLOYEE");
        try {
            SceneManager.loadScene("../view/employee-overview.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the MOVIES scene of EMPLOYEE application
     * displays the list of movies available depending on their genre
     * add or remove a movie
     */
    public void goToMovies() {
        System.out.println("MOVIES EMPLOYEE");
        try {
            SceneManager.loadScene("../view/employee-movies.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the DISCOUNTS scene of EMPLOYEE application
     * add, remove or modify a discount
     */
    public void goToDiscounts() {
        System.out.println("DISCOUNTS EMPLOYEE");
        try {
            SceneManager.loadScene("../view/employee-discounts.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the RECORDS scene of EMPLOYEE application
     * displays the list of all purchases, 2 possibilities : for employees or customers
     * sorts them dynamically depending on chosen criterium (email, title, nbr of tickets, date)
     */
    public void goToRecords() {
        System.out.println("RECORDS EMPLOYEE");
        try {
            SceneManager.loadScene("../view/employee-records.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the STATISTICS scene of EMPLOYEE application
     * displays 2 charts that summarize the statistics of the PopCorn Movies cinema
     * a line chart that shows the nbr of tickets bought during the last year (the label ticks ie the months are dynamically generated depending on the current month)
     * a pie chart that show the distribution of the genres of movies seen
     */
    public void goToStatistics() {
        System.out.println("STATISTICS EMPLOYEE");
        try {
            SceneManager.loadScene("../view/employee-statistics.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the PURCHASES scene of EMPLOYEE application
     * displays the total number of tickets bought, the number of tickets bought in the last 12 months and in the current month
     * displays all purchases of the user in a scrollable area
     */
    public void goToPurchases() {
        System.out.println("PURCHASES EMPLOYEE");
        try {
            SceneManager.loadScene("../view/employee-purchases.fxml", 1400, 800);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /***
     * function that loads the ACCOUNT scene of EMPLOYEE application
     * displays the date of creation of the account
     * lets the user select the appropriate category for the account (regular, senior or child)
     * lets the user select the theme of his choice (light or dark)
     * lets the user add a picture or change the current one
     * lets the user delete the account
     */
    public void goToAccount() {
        System.out.println("ACCOUNT EMPLOYEE");
        try {
            SceneManager.loadScene("../view/employee-account.fxml", 1400, 800);
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
     * exit the EMPLOYEE application
     */
    public void exit() {
        System.exit(0);
    }

    /***
     * first method called for initialization
     * loads user picture
     * sets chosen theme
     * displays all movies
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //handle minus button
        minusSelected = false;
        minus.getStyleClass().add("minusNotSelected");

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

        // initializes all the hashmaps
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
                if (minusSelected) {
                    deleteMovie(allMoviesCoords.get(new Pair<>(x, y)), "all");
                } else {
                    goToMovie(allMoviesCoords.get(new Pair<>(x, y)));
                }

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
                if (minusSelected) {
                    deleteMovie(actionMoviesCoords.get(new Pair<>(x, y)), "action");
                } else {
                    goToMovie(actionMoviesCoords.get(new Pair<>(x, y)));
                }
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
                if (minusSelected) {
                    deleteMovie(adventureMoviesCoords.get(new Pair<>(x, y)), "adventure");
                } else {
                    goToMovie(adventureMoviesCoords.get(new Pair<>(x, y)));
                }
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
                if (minusSelected) {
                    deleteMovie(fantasyMoviesCoords.get(new Pair<>(x, y)), "fantasy");
                } else {
                    goToMovie(fantasyMoviesCoords.get(new Pair<>(x, y)));
                }
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
                if (minusSelected) {
                    deleteMovie(documentaryMoviesCoords.get(new Pair<>(x, y)), "documentary");
                } else {
                    goToMovie(documentaryMoviesCoords.get(new Pair<>(x, y)));
                }
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
                if (minusSelected) {
                    deleteMovie(scifiMoviesCoords.get(new Pair<>(x, y)), "science fiction");
                } else {
                    goToMovie(scifiMoviesCoords.get(new Pair<>(x, y)));
                }
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
                if (minusSelected) {
                    deleteMovie(horrorMoviesCoords.get(new Pair<>(x, y)), "horror");
                } else {
                    goToMovie(horrorMoviesCoords.get(new Pair<>(x, y)));
                }
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
                if (minusSelected) {
                    deleteMovie(animationMoviesCoords.get(new Pair<>(x, y)), "animation");
                } else {
                    goToMovie(animationMoviesCoords.get(new Pair<>(x, y)));
                }
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
                if (minusSelected) {
                    deleteMovie(thrillerMoviesCoords.get(new Pair<>(x, y)), "thriller");
                } else {
                    goToMovie(thrillerMoviesCoords.get(new Pair<>(x, y)));
                }
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
                if (minusSelected) {
                    deleteMovie(comedyMoviesCoords.get(new Pair<>(x, y)), "comedy");
                } else {
                    goToMovie(comedyMoviesCoords.get(new Pair<>(x, y)));
                }
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
                if (minusSelected) {
                    deleteMovie(dramaMoviesCoords.get(new Pair<>(x, y)), "drama");
                } else {
                    goToMovie(dramaMoviesCoords.get(new Pair<>(x, y)));
                }
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
