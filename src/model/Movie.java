package model;

public class Movie {
    private int id;
    private String title;
    private String genre;
    private String director;
    private String cast;
    private String plot;
    private String imageURL;

    public Movie(int id, String title, String genre, String director, String cast, String plot, String imageURL){
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.cast = cast;
        this.plot = plot;
        this.imageURL = imageURL;
    }
}
