package model;

import java.sql.*;
import java.util.ArrayList;

public class Cinema {
    // credentials
    private static final String url       = "jdbc:mysql://localhost:3306/popcornmovie";
    private static final String user      = "root";
    private static final String password  = "";

    private static ArrayList<Movie> movies;
    private static ArrayList<Employee> employees;
    private static ArrayList<Customer> customers;

    public Cinema(){
        refresh();
    }

    public static void refresh(){
        // empty all array lists
        movies = new ArrayList<>();
        employees = new ArrayList<>();
        customers = new ArrayList<>();

        // connect to DB
        Connection connection = null;

        try{
            // create a connection to the database
            connection = DriverManager.getConnection(url, user, password);

            // statement
            Statement stmt=connection.createStatement();
            ResultSet rs;

            // fill movies
            rs = stmt.executeQuery("SELECT * FROM `Movies`");
            while(rs.next()){
                int id = rs.getInt("Id");
                String title = rs.getString("Title");
                String genre = rs.getString("Genre");
                String director = rs.getString("Director");
                String cast = rs.getString("Cast");
                String plot = rs.getString("Plot");
                String imageURL = rs.getString("ImageURL");

                //System.out.println(id + " | " + title + " | " + genre + " | " + director + " | " + cast + " | " + plot + " | " + imageURL);
                movies.add(new Movie(id, title, genre, director, cast, plot, imageURL));
            }

            // fill customers
            rs = stmt.executeQuery("SELECT * FROM `Customers`,`Logins` WHERE Customers.IdLogins = Logins.Id");
            int id;
            String lastName, firstName, dateOfCreation, email, password;
            while(rs.next()){
                id = rs.getInt("Id");
                email = rs.getString("Email");
                password = rs.getString("Password");
                lastName = rs.getString("LastName");
                firstName = rs.getString("FirstName");
                dateOfCreation = rs.getString("DateOfCreation");

                //System.out.println(id + " | " + email + " | " + password + " | " + lastName + " | " + firstName + " | " + dateOfCreation);
                customers.add(new Customer(id, email, password, lastName, firstName, dateOfCreation));
            }

            // fill employees
            rs = stmt.executeQuery("SELECT * FROM `Employees`,`Logins` WHERE Employees.IdLogins = Logins.Id");
            while(rs.next()){
                id = rs.getInt("Id");
                email = rs.getString("Email");
                password = rs.getString("Password");
                lastName = rs.getString("LastName");
                firstName = rs.getString("FirstName");
                dateOfCreation = rs.getString("DateOfCreation");

                //System.out.println(id + " | " + email + " | " + password + " | " + lastName + " | " + firstName + " | " + dateOfCreation);
                employees.add(new Employee(id, email, password, lastName, firstName, dateOfCreation));
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            try{
                if(connection != null)
                    connection.close();
            }catch(SQLException ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    public static ArrayList<Customer> getCustomers() { return customers; }

    public static ArrayList<Employee> getEmployees() { return employees; }

    public static ArrayList<Movie> getMovies() { return movies; }
}
