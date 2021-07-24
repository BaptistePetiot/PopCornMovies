package model;

public class Employee extends User{

    public Employee(int id, String email, String hashPassword, String key, String lastName, String firstName, String dateOfCreation){
        this.id = id;
        this.email = email;
        this.hashPassword = hashPassword;
        this.key = key;
        this.lastName = lastName;
        this.firstName = firstName;
        this.dateOfCreation = dateOfCreation;
    }

}
