package infrastructure.services;

import domain.common.Common;
import presentation.controllers.ConnectionController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import domain.request.MailRequest;
import domain.response.MailResponse;
import application.utilities.AesUtilities;
import application.utilities.MailUtilities;

public class MailServices {

    private static final String GET_MAIL_CREDENTIALS_SQL = "SELECT * FROM mailcredentials WHERE pkid = 1";

    public MailResponse sendEmail(String toEmail, String subject, String body) {
        MailResponse mailResponse = new MailResponse();
        MailRequest mail = new MailRequest(toEmail, subject, body);
        MailRequest fromMail = getMailCredentials();
        if (fromMail == null) {
            mailResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
            return mailResponse;
        }
        mail.setFromEmail(AesUtilities.aes256EcbDecrypt(fromMail.getFromEmail()));
        mail.setFromPassword(AesUtilities.aes256EcbDecrypt(fromMail.getFromPassword()));
        mailResponse = MailUtilities.sendEmail(mail);
        return mailResponse;
    }

    private MailRequest getMailCredentials() {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(GET_MAIL_CREDENTIALS_SQL); ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                String fromEmail = resultSet.getString("email");
                String fromPassword = resultSet.getString("password");
                return new MailRequest(fromEmail, fromPassword);
            }
        } catch (SQLException ex) {
            System.err.println("Error getting mail credentials: " + ex.getMessage());
        }
        return null;
    }
}
