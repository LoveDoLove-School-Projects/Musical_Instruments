package utilities;

import common.Common;
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
import request.MailRequest;
import response.MailResponse;

public class MailUtilities {

    private static final Properties SMTP_PROPERTIES;

    static {
        SMTP_PROPERTIES = System.getProperties();
        SMTP_PROPERTIES.put("mail.smtp.auth", "true");
        SMTP_PROPERTIES.put("mail.smtp.starttls.enable", "true");
        SMTP_PROPERTIES.put("mail.smtp.host", "smtp.office365.com");
        SMTP_PROPERTIES.put("mail.smtp.port", 587);
        SMTP_PROPERTIES.put("mail.smtp.ssl.protocols", "TLSv1.2");
        SMTP_PROPERTIES.put("mail.smtp.ssl.checkserveridentity", "false");
        SMTP_PROPERTIES.put("mail.smtp.ssl.trust", "*");
    }

    public static MailResponse sendEmail(MailRequest mailRequest) {
        return sendEmail(mailRequest.getFromEmail(), mailRequest.getFromPassword(), mailRequest.getToEmail(), mailRequest.getSubject(), mailRequest.getBody());
    }

    public static MailResponse sendEmail(String fromEmail, String fromPassword, String toEmail, String subject, String body) {
        MailResponse mailResponse = new MailResponse();
        try {
            System.out.println("Sending email to " + toEmail);

            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, fromPassword);
                }
            };

            Session session = Session.getInstance(SMTP_PROPERTIES, authenticator);
            session.setDebug(false);

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setContent(body, "text/html");
            message.setSubject(subject);
            message.setSentDate(new Date());

            Transport.send(message);

            System.out.println("Email sent successfully to " + toEmail);
            mailResponse.setStatus(Common.Status.OK);
        } catch (MessagingException ex) {
            mailResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
            System.err.println(ex);
        }
        return mailResponse;
    }
}
