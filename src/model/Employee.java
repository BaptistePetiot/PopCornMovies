package model;

/**
 * Employee class that extends User
 * @author Baptiste Petiot
 */
public class Employee extends User {

    /**
     * Employee constructor
     * @param id : int
     * @param email : String
     * @param hashPassword : String
     * @param key : String
     * @param lastName : String
     * @param firstName : String
     * @param dateOfCreation : String
     */
    public Employee(int id, String email, String hashPassword, String key, String lastName, String firstName, String dateOfCreation) {
        this.id = id;
        this.email = email;
        this.hashPassword = hashPassword;
        this.key = key;
        this.lastName = lastName;
        this.firstName = firstName;
        this.dateOfCreation = dateOfCreation;
    }

}
