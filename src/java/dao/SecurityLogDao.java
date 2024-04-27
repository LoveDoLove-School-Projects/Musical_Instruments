package dao;

import controllers.ConnectionController;
import entities.Securitylog;
import exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class SecurityLogDao {

    private static final String ADD_SECURITY_LOG_SQL = "INSERT INTO SECURITYLOG (USER_ID, ACTION, IP_ADDRESS, USER_AGENT) VALUES (?, ?, ?, ?)";

    /**
     * Adds a security log to the database.
     *
     * @param securitylog the security log to be added
     * @throws DatabaseException if there is an error accessing the database
     */
    public void addSecurityLog(Securitylog securitylog) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(ADD_SECURITY_LOG_SQL)) {
            preparedStatement.setInt(1, securitylog.getUserId());
            preparedStatement.setString(2, securitylog.getAction());
            preparedStatement.setString(3, securitylog.getIpAddress());
            preparedStatement.setString(4, securitylog.getUserAgent());
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }
}
