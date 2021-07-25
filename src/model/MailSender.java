package model;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * MailSender class that allows to send  emails from the popcornmovies.cinema@gmail.com address
 * @author Baptiste Petiot
 */
public class MailSender {
    /**
     * send email to userEmail, knowing his or her firstName and whether he or she is an employee or not
     * @param userEmail : String
     * @param firstName : String
     * @param isEmployee : String
     * @throws Exception : exception
     */
    public static void sendMail(String userEmail, String firstName, boolean isEmployee) throws Exception {
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String myEmail = "popcornmovies.cinema@gmail.com";
        String myPassword = "p0pc0rnm0v13s";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myEmail, myPassword);
            }
        });

        Message message = prepareMessage(session, myEmail, userEmail, firstName, isEmployee);

        Transport.send(message);
        System.out.println("mail sent successfully to " + userEmail);
    }

    /**
     * Prepare message depending on the firstName of the user and if he or she is an employee or not
     * @param session : Session
     * @param myEmail : String
     * @param userEmail : String
     * @param firstName : String
     * @param isEmployee : boolean
     * @return
     */
    private static Message prepareMessage(Session session, String myEmail, String userEmail, String firstName, boolean isEmployee) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            message.setSubject("Welcome to PopCorn Movies!");

            if (isEmployee) {
                message.setText("Dear " + firstName + ",\n Welcome to our team, we hope you'll enjoy working with us, we certainly will!\n We're impatient to meet you in person!\n See you soon, \n The PopCorn Movies team");
            } else {
                message.setText("Dear " + firstName + ",\n Thank you for singing up!\nThe whole PopCorn Movies teams genuinely wants to welcome you on our app!\n Go ahead and explore it, we're impatient to see you in person in our cinema.\n Best regards,\n The PopCorn Movies team");
            }

            return message;

        } catch (AddressException e) {
            System.out.println(e.getMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Send tickets to the user that bought them
     * @param userEmail : String
     * @param firstName : String
     * @param title : String
     * @param nbrTickets : int
     * @throws Exception : exception
     */
    public static void sendTickets(String userEmail, String firstName, String title, int nbrTickets) throws Exception {
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String myEmail = "popcornmovies.cinema@gmail.com";
        String myPassword = "p0pc0rnm0v13s";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myEmail, myPassword);
            }
        });

        Message message = prepareTickets(session, myEmail, userEmail, firstName, title, nbrTickets);

        Transport.send(message);
        System.out.println("tickets sent successfully to " + userEmail);
    }

    /**
     * Prepare tickets depending on the firstName of the user, the number of tickets bought and the title of the film
     * @param session : Session
     * @param myEmail : String
     * @param userEmail : String
     * @param firstName : String
     * @param title : String
     * @param nbrTickets : int
     * @return
     */
    private static Message prepareTickets(Session session, String myEmail, String userEmail, String firstName, String title, int nbrTickets) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            message.setSubject("Your tickets");

            if (firstName != null) {
                message.setText("Dear " + firstName + ",\n Please find attached your " + nbrTickets + " tickets to see : " + title + "\n See you soon, \n The PopCorn Movies team");
            } else {
                message.setText("Dear Guest,\n Please find attached your " + nbrTickets + " tickets to see : " + title + "\n See you soon, \n The PopCorn Movies team");
            }

            return message;

        } catch (AddressException e) {
            System.out.println(e.getMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
