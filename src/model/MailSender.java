package model;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSender {
    public static void sendMail(String userEmail, String firstName, boolean isEmployee) throws Exception{
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

    private static Message prepareMessage(Session session, String myEmail, String userEmail, String firstName, boolean isEmployee){
        try{
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            message.setSubject("Welcome to PopCorn Movies!");

            if(isEmployee){
                message.setText("Dear " + firstName + ",\n Welcome to our team, we hope you'll enjoy working with us, we certainly will!\n We're impatient to meet you in person!\n See you soon, \n The PopCorn Movies team");
            }else{
                message.setText("Dear " + firstName + ",\n Thank you for singing up!\nThe whole PopCorn Movies teams genuinely wants to welcome you on our app!\n Go ahead and explore it, we're impatient to see you in person in our cinema.\n Best regards,\n The PopCorn Movies team");
            }

            return message;
        }catch(AddressException e){
            System.out.println(e.getMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
