package services;

import controllers.ConnectionController;
import domain.common.Common;
import domain.request.LoginRequest;
import domain.response.LoginResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import utilities.AesUtilities;

public class LoginServices {

    private static final String CUSTOMER_LOGIN_SQL = "SELECT * FROM customers WHERE email = ? AND password = ?";
    private static final String CUSTOMER_UPDATE_LAST_LOGIN_DATE_SQL = "UPDATE customers SET last_login_date = CURRENT_TIMESTAMP WHERE customer_id = ?";

    private static final String ADMIN_LOGIN_SQL = "SELECT * FROM admins WHERE email = ? AND password = ?";
    private static final String ADMIN_UPDATE_LAST_LOGIN_DATE_SQL = "UPDATE admins SET last_login_date = CURRENT_TIMESTAMP WHERE admin_id = ?";

    public LoginResponse loginServices(LoginRequest loginRequest, Common.Role role) {
        LoginResponse loginResponse = new LoginResponse();
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(getLoginSqlQuery(role))) {
            preparedStatement.setString(1, loginRequest.getEmail());
            preparedStatement.setString(2, AesUtilities.aes256EcbEncrypt(loginRequest.getPassword()));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt(role == Common.Role.CUSTOMER ? "customer_id" : "admin_id");
                    String email = resultSet.getString("email");
                    Boolean two_factor_auth = resultSet.getBoolean("two_factor_auth");
                    updateLastLoginDate(connection, id, role);
                    loginResponse.setStatus(Common.Status.OK);
                    loginResponse.setLogin_id(id);
                    loginResponse.setEmail(email);
                    loginResponse.setTwo_factor_auth(two_factor_auth);
                } else {
                    loginResponse.setStatus(Common.Status.UNAUTHORIZED);
                }
            }
        } catch (SQLException ex) {
            loginResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
            System.err.println("Error logging in customer: " + ex.getMessage());
        }
        return loginResponse;
    }

    public void updateLastLoginDate(Connection connection, int id, Common.Role role) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(getUpdateLastLoginDateSqlQuery(role))) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error updating last login date: " + ex.getMessage());
        }
    }

    private String getLoginSqlQuery(Common.Role role) {
        return role == Common.Role.CUSTOMER ? CUSTOMER_LOGIN_SQL : ADMIN_LOGIN_SQL;
    }

    private String getUpdateLastLoginDateSqlQuery(Common.Role role) {
        return role == Common.Role.CUSTOMER ? CUSTOMER_UPDATE_LAST_LOGIN_DATE_SQL : ADMIN_UPDATE_LAST_LOGIN_DATE_SQL;
    }
}
