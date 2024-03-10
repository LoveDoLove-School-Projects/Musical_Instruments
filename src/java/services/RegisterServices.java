package services;

import common.Common;
import contollers.ConnectionController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import request.RegisterRequest;
import response.RegisterResponse;
import utilities.AesUtilities;

public class RegisterServices {

    private final String ADD_NEW_CUSTOMER_SQL = "INSERT INTO customers (username, password, email, address, phone_number, gender) VALUES (?, ?, ?, ?, ?, ?)";
    private final String COUNT_EMAIL_SQL = "SELECT COUNT(*) FROM customers WHERE email = ?";

    public RegisterResponse addNewCustomer(RegisterRequest registerRequest) throws SQLException {
        RegisterResponse registerResponse = new RegisterResponse();

        try (Connection connection = ConnectionController.getConnection()) {
            if (isEmailExists(connection, registerRequest.getEmail())) {
                registerResponse.setStatus(Common.Status.EXISTS);
                return registerResponse;
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_NEW_CUSTOMER_SQL)) {
                preparedStatement.setString(1, registerRequest.getUsername());
                preparedStatement.setString(2, AesUtilities.aes256EcbEncrypt(registerRequest.getPassword()));
                preparedStatement.setString(3, registerRequest.getEmail());
                preparedStatement.setString(4, registerRequest.getAddress());
                preparedStatement.setString(5, registerRequest.getPhoneNumber());
                preparedStatement.setString(6, registerRequest.getGender());
                int update = preparedStatement.executeUpdate();
                if (update > 0) {
                    registerResponse.setStatus(Common.Status.OK);
                } else {
                    registerResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
                }
                return registerResponse;
            }
        } catch (SQLException ex) {
            throw new SQLException("Error registering new customer: " + ex.getMessage());
        }
    }

    private boolean isEmailExists(Connection connection, String email) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(COUNT_EMAIL_SQL)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error checking if email exists: " + ex.getMessage());
        }
        return false;
    }
}
