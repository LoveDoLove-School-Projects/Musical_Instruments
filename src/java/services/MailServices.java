package services;

import common.Common;
import contollers.ConnectionController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.Mail;
import response.MailResponse;
import utilities.AesUtilities;
import utilities.MailUtilities;

public class MailServices {

    private final String OTP_EMAIL_SUBJECT = "OTP";
    private final String OTP_EMAIL_BODY = "Your OTP is: ";
    private final String GET_MAIL_CREDENTIALS_SQL = "SELECT * FROM mailcredentials WHERE pkid = 1";

    public MailResponse sendEmail(String toEmail, String subject, String body) {
        MailResponse mailResponse = new MailResponse();
        Mail mail = new Mail(toEmail, subject, body);
        Mail fromMail = getMailCredentials();
        if (fromMail == null) {
            System.err.println("Error getting mail credentials");
            mailResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
            return mailResponse;
        }
        mail.setFromEmail(AesUtilities.aes256EcbDecrypt(fromMail.getFromEmail()));
        mail.setFromPassword(AesUtilities.aes256EcbDecrypt(fromMail.getFromPassword()));
        mailResponse = MailUtilities.sendEmail(mail);
        return mailResponse;
    }

    public MailResponse sendOtpEmail(String toEmail, String otp) {
        return sendEmail(toEmail, OTP_EMAIL_SUBJECT, OTP_EMAIL_BODY + otp);
    }

    private Mail getMailCredentials() {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(GET_MAIL_CREDENTIALS_SQL); ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                String fromEmail = resultSet.getString("email");
                String fromPassword = resultSet.getString("password");
                return new Mail(fromEmail, fromPassword);
            }
        } catch (SQLException ex) {
            System.err.println("Error getting mail credentials: " + ex.getMessage());
        }
        return null;
    }
}
