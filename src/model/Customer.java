package model;

/**
 * Customer class that extends User
 * @author Baptiste Petiot
 */
public class Customer extends User {

    /**
     * Customer constructor
     * @param id : int
     * @param email : String
     * @param hashPassword : String
     * @param key : String
     * @param lastName : String
     * @param firstName : String
     * @param dateOfCreation : String
     */
    public Customer(int id, String email, String hashPassword, String key, String lastName, String firstName, String dateOfCreation) {
        this.id = id;
        this.email = email;
        this.hashPassword = hashPassword;
        this.key = key;
        this.lastName = lastName;
        this.firstName = firstName;
        this.dateOfCreation = dateOfCreation;
    }

    /**
     * allows to get the last name of the customer
     * @return lastName : String
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * allows to get the first name of the customer
     * @return firstName : String
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * allows to get the id of the customer
     * @return id : int
     */
    public int getId() {
        return id;
    }

}
