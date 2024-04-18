package services;

import controllers.ConnectionController;
import domain.common.Common;
import domain.request.RegisterRequest;
import exceptions.DatabaseAccessException;
import features.AesHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterServices {

    private static final String ADD_NEW_USER_SQL = "INSERT INTO Users (username, password, email, address, phone_number, gender) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String ADD_NEW_CUSTOMER_SQL = "INSERT INTO Customer (user_id) VALUES (?)";
    private static final String COUNT_USER_EMAIL_SQL = "SELECT COUNT(*) FROM Users WHERE email = ?";

    public Common.Status addNewCustomer(RegisterRequest registerRequest) {
        try (Connection connection = ConnectionController.getConnection()) {
            if (doesEmailExist(connection, registerRequest.getEmail())) {
                return Common.Status.EXISTS;
            }
            try (PreparedStatement userStatement = connection.prepareStatement(ADD_NEW_USER_SQL, Statement.RETURN_GENERATED_KEYS)) {
                userStatement.setString(1, registerRequest.getUsername());
                userStatement.setString(2, AesHandler.aes256EcbEncrypt(registerRequest.getPassword()));
                userStatement.setString(3, registerRequest.getEmail());
                userStatement.setString(4, registerRequest.getAddress());
                userStatement.setString(5, registerRequest.getPhoneNumber());
                userStatement.setString(6, registerRequest.getGender());
                userStatement.executeUpdate();
                try (ResultSet generatedKeys = userStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);
                        try (PreparedStatement customerStatement = connection.prepareStatement(ADD_NEW_CUSTOMER_SQL)) {
                            customerStatement.setInt(1, userId);
                            customerStatement.executeUpdate();
                        }
                    }
                }
                return Common.Status.OK;
            } catch (SQLException ex) {
                throw new DatabaseAccessException("Error adding new customer: " + ex.getMessage(), ex);
            }
        } catch (SQLException ex) {
            throw new DatabaseAccessException("Error establishing database connection: " + ex.getMessage(), ex);
        }
    }

    private boolean doesEmailExist(Connection connection, String email) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(COUNT_USER_EMAIL_SQL)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            throw new DatabaseAccessException("Database error while checking email existence", ex);
        }
    }
}
