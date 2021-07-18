package model;

public class Me {
    private static int id;
    private static String firstName, lastName;
    private static Movie lookingAtMovie;
    private static int category = 0, theme = 0;

    public static int getId() { return id; }
    public static void setId(int myId) { id = myId; }
    public static String getFirstName() { return firstName; }
    public static void setFirstName(String firstName) { Me.firstName = firstName; }
    public static String getLastName() { return lastName; }
    public static void setLastName(String lastName) { Me.lastName = lastName; }
    public static Movie getLookingAtMovie() { return lookingAtMovie; }
    public static void setLookingAtMovie(Movie lookingAtMovie) { Me.lookingAtMovie = lookingAtMovie; }
    public static int getCategory() { return category; }
    public static void setCategory(int category) { category = category; }
    public static int getTheme() { return theme; }
    public static void setTheme(int theme) { theme = theme; }
}
