package model;

public class Movie {
    private int id;
    private int duration;
    private String title;
    private String genre;
    private String director;
    private String cast;
    private String plot;
    private String imageURL;

    public Movie(int id, int duration, String title, String genre, String director, String cast, String plot, String imageURL){
        this.id = id;
        this.duration = duration;
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.cast = cast;
        this.plot = plot;
        this.imageURL = imageURL;
    }

    public int getId() { return id; }

    public int getDuration() { return duration; }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public String getCast() {
        return cast;
    }

    public String getPlot() {
        return plot;
    }

    public String getImageURL() {
        return imageURL;
    }
}
