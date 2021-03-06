package model;

import java.sql.*;
import java.util.ArrayList;

/**
 * Cinema static class that knows every information in the DB about the cinema : movies, employees, customers and discounts
 * @author Baptiste Petiot
 */
public class Cinema {
    // credentials
    private static final String url = "jdbc:mysql://localhost:3306/popcornmovie";
    private static final String user = "root";
    private static final String password = "";

    private static ArrayList<Movie> movies;
    private static ArrayList<Employee> employees;
    private static ArrayList<Customer> customers;
    private static ArrayList<Discount> discounts;

    public Cinema() {
        refresh();
    }

    /**
     * updates all the known movies, employees, customers and discounts from the DB
     */
    public static void refresh() {
        // initialize all array lists empty
        movies = new ArrayList<>();
        employees = new ArrayList<>();
        customers = new ArrayList<>();
        discounts = new ArrayList<>();

        // connect to DB
        Connection connection = null;

        try {
            // create a connection to the database
            connection = DriverManager.getConnection(url, user, password);

            // statement
            Statement stmt = connection.createStatement();
            ResultSet rs;

            // fill movies
            rs = stmt.executeQuery("SELECT * FROM `Movies`");
            while (rs.next()) {
                int id = rs.getInt("Id");
                int duration = rs.getInt("Duration");
                String title = rs.getString("Title");
                String genre = rs.getString("Genre");
                String director = rs.getString("Director");
                String cast = rs.getString("Cast");
                String plot = rs.getString("Plot");
                String imageURL = rs.getString("ImageURL");

                //System.out.println(id + " | " + title + " | " + genre + " | " + director + " | " + cast + " | " + plot + " | " + imageURL);
                movies.add(new Movie(id, duration, title, genre, director, cast, plot, imageURL));
            }

            // fill customers
            rs = stmt.executeQuery("SELECT * FROM `Customers`,`Logins` WHERE Customers.IdLogins = Logins.Id");
            int id;
            String lastName, firstName, dateOfCreation, email, hashPassword, key;
            while (rs.next()) {
                id = rs.getInt("Id");
                email = rs.getString("Email");
                hashPassword = rs.getString("HashPassword");
                key = rs.getString("KeyPassword");
                lastName = rs.getString("LastName");
                firstName = rs.getString("FirstName");
                dateOfCreation = rs.getString("DateOfCreation");

                //System.out.println(id + " | " + email + " | " + password + " | " + lastName + " | " + firstName + " | " + dateOfCreation);
                customers.add(new Customer(id, email, hashPassword, key, lastName, firstName, dateOfCreation));
            }

            // fill employees
            rs = stmt.executeQuery("SELECT * FROM `Employees`,`Logins` WHERE Employees.IdLogins = Logins.Id");
            while (rs.next()) {
                id = rs.getInt("Id");
                email = rs.getString("Email");
                hashPassword = rs.getString("HashPassword");
                key = rs.getString("KeyPassword");
                lastName = rs.getString("LastName");
                firstName = rs.getString("FirstName");
                dateOfCreation = rs.getString("DateOfCreation");

                //System.out.println(id + " | " + email + " | " + password + " | " + lastName + " | " + firstName + " | " + dateOfCreation);
                employees.add(new Employee(id, email, hashPassword, key, lastName, firstName, dateOfCreation));
            }

            // fill discounts
            rs = stmt.executeQuery("SELECT * FROM `Discounts`");
            while (rs.next()) {
                id = rs.getInt("Id");
                String name = rs.getString("Name");
                int amount = rs.getInt("Amount");
                char unit = rs.getString("Unit").charAt(0);
                String status = rs.getString("Status");

                discounts.add(new Discount(id, name, amount, unit, status));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * get the list of the customers
     * @return customers : ArrayList
     */
    public static ArrayList<Customer> getCustomers() {
        return customers;
    }

    /**
     * get the list of the employees
     * @return employees : ArrayList
     */
    public static ArrayList<Employee> getEmployees() {
        return employees;
    }

    /**
     * get the list of the movies
     * @return movies : ArrayList
     */
    public static ArrayList<Movie> getMovies() {
        return movies;
    }

    /**
     * get the list of the discounts
     * @return discounts : ArrayList
     */
    public static ArrayList<Discount> getDiscounts() {
        return discounts;
    }
}
