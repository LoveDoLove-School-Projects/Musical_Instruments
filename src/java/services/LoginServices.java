package services;

import controllers.ConnectionController;
import domain.common.Common;
import domain.request.LoginRequest;
import domain.response.LoginResponse;
import exceptions.DatabaseAccessException;
import features.AesHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginServices {

    private static final String LOGIN_SQL = "SELECT * FROM users WHERE email = ? AND password = ?";
    private static final String UPDATE_LAST_LOGIN_DATE_SQL = "UPDATE users SET last_login_date = CURRENT_TIMESTAMP WHERE user_id = ?";

    public LoginResponse loginServices(LoginRequest loginRequest, Common.Role role) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(LOGIN_SQL)) {
            preparedStatement.setString(1, loginRequest.getEmail());
            preparedStatement.setString(2, AesHandler.aes256EcbEncrypt(loginRequest.getPassword()));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("user_id");
                    String email = resultSet.getString("email");
                    boolean two_factor_auth = resultSet.getBoolean("two_factor_auth");
                    if (!updateLastLoginDate(connection, id)) {
                        return new LoginResponse(Common.Status.INTERNAL_SERVER_ERROR);
                    }
                    return new LoginResponse(Common.Status.OK, id, email, two_factor_auth);
                }
                return new LoginResponse(Common.Status.UNAUTHORIZED);
            } catch (SQLException ex) {
                throw new DatabaseAccessException("Database error while logging in customer", ex);
            }
        } catch (SQLException ex) {
            throw new DatabaseAccessException("Database error while logging in customer", ex);
        }
    }

    private boolean updateLastLoginDate(Connection connection, int id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_LAST_LOGIN_DATE_SQL)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            throw new DatabaseAccessException("Database error while updating last login date", ex);
        }
    }
}
