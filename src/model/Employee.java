package model;

public class Employee extends User{

    public Employee(int id, String email, String password, String lastName, String firstName, String dateOfCreation){
        this.id = id;
        this.email = email;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.dateOfCreation = dateOfCreation;
    }

}
