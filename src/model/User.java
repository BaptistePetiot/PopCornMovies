package model;

/**
 * User abstract class that forces the Employee and Customer classes to have certain shared characteristics
 * @author Baptiste Petiot
 */
public abstract class User {
    protected int id;
    protected String email;
    protected String hashPassword;
    protected String key;
    protected String lastName;
    protected String firstName;
    protected String dateOfCreation;
}
