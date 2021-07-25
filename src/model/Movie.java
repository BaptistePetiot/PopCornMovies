package model;

/**
 * Class Movie
 * @author Baptiste Petiot
 */
public class Movie {
    private int id;
    private int duration;
    private String title;
    private String genre;
    private String director;
    private String cast;
    private String plot;
    private String imageURL;

    /**
     * Movie constructor
     * @param id : int
     * @param duration : int
     * @param title : String
     * @param genre : String
     * @param director : String
     * @param cast : String
     * @param plot : String
     * @param imageURL : String
     */
    public Movie(int id, int duration, String title, String genre, String director, String cast, String plot, String imageURL) {
        this.id = id;
        this.duration = duration;
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.cast = cast;
        this.plot = plot;
        this.imageURL = imageURL;
    }

    /**
     * allows to get the id of the movie
     * @return id : int
     */
    public int getId() {
        return id;
    }

    /**
     * allows to get the duration of the movie
     * @return duration : int
     */
    public int getDuration() {
        return duration;
    }

    /**
     * allows to get the title of the movie
     * @return title : String
     */
    public String getTitle() {
        return title;
    }

    /**
     * allows to get the genre of the movie
     * @return genre : String
     */
    public String getGenre() {
        return genre;
    }

    /**
     * allows to get the director of the movie
     * @return director : String
     */
    public String getDirector() {
        return director;
    }

    /**
     * allows to get the cast of the movie
     * @return cast : String
     */
    public String getCast() {
        return cast;
    }

    /**
     * allows to get the plot of the movie
     * @return plot : String
     */
    public String getPlot() {
        return plot;
    }

    /**
     * allows to get the image url
     * @return imageURl : String
     */
    public String getImageURL() {
        return imageURL;
    }
}
