package dao;

import controllers.ConnectionController;
import entities.Internalsecuritylog;
import entities.Securitylog;
import exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class SecurityLogDao {

    private static final String ADD_SECURITY_LOG_SQL = "INSERT INTO SECURITYLOG (USER_ID, ACTION, IP_ADDRESS, USER_AGENT) VALUES (?, ?, ?, ?)";
    private static final String ADD_INTERNAL_SECURITY_LOG_SQL = "INSERT INTO INTERNALSECURITYLOG (USERNAME, ACTION, IP_ADDRESS, USER_AGENT) VALUES (0, ?, ?, ?)";

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

    /**
     * Adds an internal security log entry to the database.
     *
     * @param internalsecuritylog The internal security log object to be added.
     * @throws DatabaseException If there is an error while accessing the
     * database.
     */
    public void addInternalSecurityLog(Internalsecuritylog internalsecuritylog) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(ADD_INTERNAL_SECURITY_LOG_SQL)) {
            preparedStatement.setString(1, internalsecuritylog.getUsername());
            preparedStatement.setString(2, internalsecuritylog.getAction());
            preparedStatement.setString(3, internalsecuritylog.getIpAddress());
            preparedStatement.setString(4, internalsecuritylog.getUserAgent());
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }
}
