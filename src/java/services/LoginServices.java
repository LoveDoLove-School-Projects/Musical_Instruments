package services;

import common.Common;
import contollers.ConnectionController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import request.LoginRequest;
import response.LoginResponse;
import utilities.AesUtilities;

public class LoginServices {

    private final String LOGIN_CUSTOMER_SQL = "SELECT * FROM customers WHERE email = ? AND password = ?";
    private final String UPDATE_LAST_LOGIN_DATE_SQL = "UPDATE customers SET last_login_date = CURRENT_TIMESTAMP WHERE customer_id = ?";

    public LoginResponse loginCustomer(LoginRequest loginRequest) {
        LoginResponse loginResponse = new LoginResponse();
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(LOGIN_CUSTOMER_SQL)) {
            preparedStatement.setString(1, loginRequest.getEmail());
            preparedStatement.setString(2, AesUtilities.aes256EcbEncrypt(loginRequest.getPassword()));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int customerId = resultSet.getInt("customer_id");
                    updateLastLoginDate(connection, customerId);
                    loginResponse.setStatus(Common.Status.OK);
                    loginResponse.setLogin_id(customerId);
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

    public void updateLastLoginDate(Connection connection, int customerId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_LAST_LOGIN_DATE_SQL)) {
            preparedStatement.setInt(1, customerId);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error updating last login date: " + ex.getMessage());
        }
    }
}
