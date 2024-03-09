package services;

import contollers.ConnectionController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import models.Customer;
import request.LoginRequest;
import response.LoginResponse;
import utilities.AesUtilities;

public class LoginServices {

    public LoginResponse loginCustomer(LoginRequest loginRequest) throws SQLException {
        LoginResponse loginResponse = new LoginResponse();
        try (Connection connection = ConnectionController.getConnection()) {
            Optional<Integer> customerId = authenticateCustomer(connection, loginRequest);
            if (customerId.isPresent()) {
                updateLastLoginDate(connection, customerId.get());
                loginResponse.setStatus(common.Common.Status.OK);
                loginResponse.setCustomer(new Customer(customerId.get()));
            } else {
                loginResponse.setStatus(common.Common.Status.INVALID);
            }
            return loginResponse;
        }
    }

    private Optional<Integer> authenticateCustomer(Connection connection, LoginRequest loginRequest) throws SQLException {
        String sqlString = "SELECT * FROM customers WHERE email = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlString)) {
            preparedStatement.setString(1, loginRequest.getEmail());
            preparedStatement.setString(2, AesUtilities.aes256EcbEncrypt(loginRequest.getPassword()));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSet.getInt("customer_id"));
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error authenticating customer: " + ex.getMessage());
        }
        return Optional.empty();
    }

    public void updateLastLoginDate(Connection connection, int customerId) throws SQLException {
        String sqlString = "UPDATE customers SET last_login_date = CURRENT_TIMESTAMP WHERE customer_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlString)) {
            preparedStatement.setInt(1, customerId);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Error updating last login date: " + ex.getMessage());
        }
    }
}
