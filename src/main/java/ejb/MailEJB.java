package ejb;

import javax.ejb.Stateless;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by Arturo on 25-06-2017.
 */

@Stateless
public class MailEJB {

    private int port = 465;
    private String host = "smtp.gmail.com";
    private String from = "feelms.usach@gmail.com";
    private boolean auth = true;
    private String username = "feelms.usach";
    private String password = "TBD123123";
    private Protocol protocol = Protocol.SMTP;

    public enum Protocol {
        SMTP,
        SMTPS,
        TLS
    }

    private void sendMail(String to, String subject, String body) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(body);

            // Send message
            Transport.send(message);

        } catch (MessagingException e) {
            throw e;
        }

    }

    public void sendWelcomeMail(String to) throws MessagingException {
        String body = "Bienvenido a Feelms!! :D";
        sendMail(to, "Suscripcion a Feelms", body);
    }

    public void sendGoodbyeMail(String to) throws MessagingException {
        String body = "Usted a cancelado su suscripcion a Feelms :C";
        sendMail(to, "Suscripcion a Feelms cancelada", body);
    }


}
