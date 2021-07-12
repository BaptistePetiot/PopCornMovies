package model;

public class Me {
    private static int id;
    private static String firstName, lastName;

    public static int getId() { return id; }
    public static void setId(int myId) { id = myId; }
    public static String getFirstName() { return firstName; }
    public static void setFirstName(String firstName) { Me.firstName = firstName; }
    public static String getLastName() { return lastName; }
    public static void setLastName(String lastName) { Me.lastName = lastName; }
}
