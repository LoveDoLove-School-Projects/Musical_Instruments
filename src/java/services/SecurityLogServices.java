package services;

import controllers.ConnectionController;
import entities.Securitylog;
import exceptions.DatabaseAccessException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class SecurityLogServices {

    private static final String ADD_SECURITY_LOG_SQL = "INSERT INTO SECURITYLOG (USER_ID, ROLE, ACTION, IP_ADDRESS, USER_AGENT) VALUES (?, ?, ?, ?, ?)";

    public void addSecurityLog(Securitylog securitylog) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(ADD_SECURITY_LOG_SQL)) {
            preparedStatement.setInt(1, securitylog.getUserId());
            preparedStatement.setString(2, securitylog.getRole());
            preparedStatement.setString(3, securitylog.getAction());
            preparedStatement.setString(4, securitylog.getIpAddress());
            preparedStatement.setString(5, securitylog.getUserAgent());
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            throw new DatabaseAccessException("Error adding security log", ex);
        }
    }
}
