package services;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import utilities.AesUtilities;

public class MailServices {

    private static final String FROM_EMAIL = "yPK5aybh/TUDRZluNBxpWjiBkyyD6cBirB9U2IaadmQXVfhKLN4bVxBaNrSIJZ1D";

    private static final String FROM_PASSWORD = "0S4CapPLnu1WmfZAw3MmfsOJBcg/nSHmeMBv1FaDgrw=";

    private static final String NO_REPLY_EMAIL = "no_reply@musicalinstruments.com";

    private static final Properties SMTP_PROPERTIES;

    static {
        SMTP_PROPERTIES = System.getProperties();
        SMTP_PROPERTIES.put("mail.smtp.host", "smtp.gmail.com");
        SMTP_PROPERTIES.put("mail.smtp.auth", "true");
        SMTP_PROPERTIES.put("mail.smtp.port", "465");
        SMTP_PROPERTIES.put("mail.smtp.socketFactory.port", "465");
        SMTP_PROPERTIES.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        SMTP_PROPERTIES.put("mail.smtp.socketFactory.fallback", "false");
        SMTP_PROPERTIES.put("mail.smtp.ssl.checkserveridentity", "false");
        SMTP_PROPERTIES.put("mail.smtp.ssl.trust", "*");
    }

    public void sendEmail(String toEmail, String subject, String body) {
        try {
            System.out.println("Sending email to " + toEmail);

            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(AesUtilities.aes256EcbDecrypt(FROM_EMAIL), AesUtilities.aes256EcbDecrypt(FROM_PASSWORD));
                }
            };

            Session session = Session.getInstance(SMTP_PROPERTIES, authenticator);
            session.setDebug(false);

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(NO_REPLY_EMAIL));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setReplyTo(InternetAddress.parse(NO_REPLY_EMAIL));
            message.setContent(body, "text/html");
            message.setSubject(subject);
            message.setSentDate(new Date());

            Transport.send(message);

            System.out.println("Email sent successfully to " + toEmail);
        } catch (MessagingException ex) {
            System.err.println(ex);
        }
    }
}
