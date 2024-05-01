package dao;

import controllers.ConnectionController;
import entities.Internalsecuritylog;
import entities.Securitylog;
import exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class SecurityLogDao {

    private static final String ADD_SECURITY_LOG_SQL = "INSERT INTO SECURITYLOG (USER_ID, USERNAME, EMAIL, ACTION, IP_ADDRESS, USER_AGENT) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String ADD_INTERNAL_SECURITY_LOG_SQL = "INSERT INTO INTERNALSECURITYLOG (USER_ID, USERNAME, EMAIL, ACTION, IP_ADDRESS, USER_AGENT) VALUES (?, ?, ?, ?, ?, ?)";

    public void addSecurityLog(Securitylog securitylog) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(ADD_SECURITY_LOG_SQL)) {
            preparedStatement.setInt(1, securitylog.getUserId());
            preparedStatement.setString(2, securitylog.getUsername());
            preparedStatement.setString(3, securitylog.getEmail());
            preparedStatement.setString(4, securitylog.getAction());
            preparedStatement.setString(5, securitylog.getIpAddress());
            preparedStatement.setString(6, securitylog.getUserAgent());
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    public void addInternalSecurityLog(Internalsecuritylog internalsecuritylog) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(ADD_INTERNAL_SECURITY_LOG_SQL)) {
            preparedStatement.setInt(1, internalsecuritylog.getUserId());
            preparedStatement.setString(2, internalsecuritylog.getUsername());
            preparedStatement.setString(3, internalsecuritylog.getEmail());
            preparedStatement.setString(4, internalsecuritylog.getAction());
            preparedStatement.setString(5, internalsecuritylog.getIpAddress());
            preparedStatement.setString(6, internalsecuritylog.getUserAgent());
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }
}
