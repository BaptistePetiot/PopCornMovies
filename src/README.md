# Pop Corn Movies

#### Members
Marie SIMON,
Baptiste Petiot

#### Deadlines
June 21st : DataBase design + GUI + first UML

July 25th : project deliverable deadline

July 26th : Oral Presentation

### Organization
    - PACKAGES
        - controller : contains all of our controllers (one per view, redundant but easier to work with and to improve over time)
        - main : PopCornMovie ie our application
        - model : conatains all the classes of our Model, we have made the choice to have a reduced amount of classes,
            we wanted to be sure that each of the classes were going to be useful to us and to make sense in the way we saw the project evolving
            - Scene Manager : allows us to switch between seens more easily
            - Mail Sender : allows us to send emails from the adress popcornmovies.cinema@gmail.com to the user after singing in or buying new tickets
            - Password Encrypter Decrypter : allows us to encrypt and decrypt passwords using the AES algorithm
            - Me : static class that knows important information about the current user : a Data Access Module more centered on the user,
            allws us to avoid using more different classes, such as Guest, Member and their subclasses
        - view : contains all of our fxml files created using Sccene Builder

    - other folders
        - css : contains LightTheme.css and DarkTheme.css, the 2 files that redefine differently the styles of the application depending on the choice of the user
        - imgs : contains our icons and our hand drawn logo

    - additional files
        - passwords.txt : a little reminder. Since the DB is encrypted, you might want the clear passwords.
        - sql.txt : a reminder of the commands used to generate our Data Base

    - additional resources
        In the deliverable you will also find our ppt presentation, the JavaDoc folder that contains a helpful documentation of our project, a UML of the controllers (list), 
        a popcornmovie sql text file that allows to generate our DB simply by importing this file into PhpMyAdmin, the DB design diagram, the UML (Model) and the E-R diagram and a hand drawn wireframe.

### Naming conventions
    We followed a Camel Case convention in our java code.
    
    We named all of our views based on the example : [type of user]-[name of the view].fxml, except those related to login
    
    We named all of our controllers based on the example : [Name of the view][type of user]Controller.java
    
### CRITERIA FOR NOTATION:
    - we have a : home window allowing the user to connect to the DB by entering EMAIL and PASSWORD
    - reliability of our system : every major action (db connection and encryption) is handled in a try catch block
    - relevance of the information displayed : we've tried to display information on our GUI in a clear way and to sort it for more
        conviniency ("RECORDS" by email, title, nbr of tickets or date & 2 options : by customers or by employees)
    - the visual quality, fluidity, ergonomics and particular originality of the graphical interface : we were inspired 
        by modern graphical looks and tried to replicate when is the most useful and nice-looking at the same time,
        this is one of the reasons why we have decided to add the possibility to choose between 2 themes
    - good interaction of the project with the DB : 
        + the static class Cinema acts as a Data Access module and allowed us to conviniently retrieve and modify information.
        - nonetheless, there remains some sql requests in some controlers, we could have pushed it further
    - search on a table with filtering of information other than the identifying number : (sum of tickets bought)
        lots of examples, here is one, the request to determine the most seen movies :
        "SELECT IdMovies, SUM(NbrTickets) AS s FROM Purchases GROUP BY IdMovies ORDER BY s DESC"
    - cross-search (joins) on several tables : 
        several examples, here is one, the request to determine the number of action tickets bought:
        "SELECT SUM(p.NbrTickets), m.Genre FROM Purchases as p, Movies as m WHERE p.IdMovies = m.Id AND m.Genre = 'Action'"
    - DB connection : INSERT INTO, UPDATE, DELETE:
        insert encrypted user in DB : "INSERT INTO `logins` (`Id`, `Email`, `HashPassword`, `KeyPassword`, `IV`) VALUES ('" + nextId + "', '" + emailSignup.getText() + "', '" + cipherPassword + "', '" + keyPassword + "', '" + ivString + "');"
        update user profile picture : "UPDATE `Pictures` SET picture=? WHERE IdLogins=?;"
        delete account : "DELETE FROM logins WHERE Id = " + Me.getId()
    - comments in the code : 100% commented + JavaDoc generated

    - OOP principles (inheritance, classes, interface, static classes, etc.)
    - MVC (in seperated packages)
    - Data Access module is our Cinema class --> it knows every customer, every employee, every discount and every movie
        this is the class we use to retrieve and modify informations in the DB

## BONUSES:
    - hand drawn logo
    - 2 themes
    - payment progress bar animation
    - profile picture saved to DB (adding and changing)
    - email (signup for customer and employee + for tickets at each purchase)
    - charts
        - months bellow the line chart (x ticks) are dynamically generated depending on today's date
    - password encryption using AES algorithm
    
    
