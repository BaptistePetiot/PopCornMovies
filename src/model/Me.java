package model;

/**
 * static class Me that knows every important information about the current user of the software
 * @author Baptiste Petiot
 */
public class Me {
    private static int id;
    private static String firstName, lastName, email;
    private static Movie lookingAtMovie;
    private static Discount modifyingDiscount;
    private static int category = 0, theme = 0;

    /**
     * allows to get the id of the current user
     * @return id : int
     */
    public static int getId() {
        return id;
    }

    /**
     * allows to set the id of the current user
     * @param myId : int
     */
    public static void setId(int myId) {
        id = myId;
    }

    /**
     * allows to get the firstName of the current user
     * @return firstName : String
     */
    public static String getFirstName() {
        return firstName;
    }

    /**
     * allows to set the firstName of the current user
     * @param firstName : String
     */
    public static void setFirstName(String firstName) {
        Me.firstName = firstName;
    }

    /**
     * allows to get the lastName of the current user
     * @return lastName : String
     */
    public static String getLastName() {
        return lastName;
    }

    /**
     * allows to set the lastName of the current user
     * @param lastName : String
     */
    public static void setLastName(String lastName) {
        Me.lastName = lastName;
    }

    /**
     * allows to get the movie the current user is looking at
     * @return lookingAtMovie : Movie
     */
    public static Movie getLookingAtMovie() {
        return lookingAtMovie;
    }

    /**
     * allows to set the movie the current user is looking at
     * @param lookingAtMovie : Movie
     */
    public static void setLookingAtMovie(Movie lookingAtMovie) {
        Me.lookingAtMovie = lookingAtMovie;
    }

    /**
     * allows to get the category of the current user
     * @return category : int
     */
    public static int getCategory() {
        return category;
    }

    /**
     * allows to set the category of the current user
     * @param category : int
     */
    public static void setCategory(int category) {
        Me.category = category;
    }

    /**
     * allows to get the preferred theme of the current user
     * @return theme : int
     */
    public static int getTheme() {
        return theme;
    }

    /**
     * allows to set the preferred theme of the current user
     * @param theme : int
     */
    public static void setTheme(int theme) {
        Me.theme = theme;
    }

    /**
     * allows to get the email of the current user
     * @return email : String
     */
    public static String getEmail() {
        return email;
    }

    /**
     * allows to set the email of the current user
     * @param email : String
     */
    public static void setEmail(String email) {
        Me.email = email;
    }

    /**
     * allows to get the discount the current user is modifying
     * @return modifyingDiscount : Discount
     */
    public static Discount getModifyingDiscount() {
        return modifyingDiscount;
    }

    /**
     * allows to set the discount the current user is modifying
     * @param modifyingDiscount : Discount
     */
    public static void setModifyingDiscount(Discount modifyingDiscount) {
        Me.modifyingDiscount = modifyingDiscount;
    }
}
