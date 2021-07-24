package model;

public class Customer extends User{

    public Customer(int id, String email, String hashPassword, String key, String lastName, String firstName, String dateOfCreation){
        this.id = id;
        this.email = email;
        this.hashPassword = hashPassword;
        this.key = key;
        this.lastName = lastName;
        this.firstName = firstName;
        this.dateOfCreation = dateOfCreation;
    }


    public String getLastName(){ return lastName; }
    public String getFirstName(){ return firstName; }
    public int getId(){ return id; }

}
