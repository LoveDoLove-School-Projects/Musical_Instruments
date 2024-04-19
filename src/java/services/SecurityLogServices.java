package services;

import controllers.ConnectionController;
import entities.Securitylog;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Logger;

public class SecurityLogServices {

    private static final Logger LOG = Logger.getLogger(SecurityLogServices.class.getName());
    private static final String ADD_SECURITY_LOG_SQL = "INSERT INTO SECURITYLOG (USER_ID, ROLE, ACTION, IP_ADDRESS, USER_AGENT) VALUES (?, ?, ?, ?, ?)";

    /**
     * Adds a security log entry to the database.
     *
     * @param securitylog The security log object containing the log details.
     */
    public void addSecurityLog(Securitylog securitylog) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(ADD_SECURITY_LOG_SQL)) {
            preparedStatement.setInt(1, securitylog.getUserId());
            preparedStatement.setString(2, securitylog.getRole());
            preparedStatement.setString(3, securitylog.getAction());
            preparedStatement.setString(4, securitylog.getIpAddress());
            preparedStatement.setString(5, securitylog.getUserAgent());
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
        }
    }
}
