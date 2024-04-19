package services;

import controllers.ConnectionController;
import domain.common.Common;
import domain.request.RegisterRequest;
import exceptions.DatabaseException;
import features.AesHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterServices {

    private static final String ADD_NEW_CUSTOMER_SQL = "INSERT INTO Customers (username, password, email, address, phone_number, gender) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String COUNT_USER_EMAIL_SQL = "SELECT COUNT(*) FROM Customers WHERE email = ?";

    public Common.Status addNewCustomer(RegisterRequest registerRequest) {
        try (Connection connection = ConnectionController.getConnection()) {
            if (doesEmailExist(connection, registerRequest.getEmail())) {
                return Common.Status.EXISTS;
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_NEW_CUSTOMER_SQL)) {
                preparedStatement.setString(1, registerRequest.getUsername());
                preparedStatement.setString(2, AesHandler.aes256EcbEncrypt(registerRequest.getPassword()));
                preparedStatement.setString(3, registerRequest.getEmail());
                preparedStatement.setString(4, registerRequest.getAddress());
                preparedStatement.setString(5, registerRequest.getPhoneNumber());
                preparedStatement.setString(6, registerRequest.getGender());
                preparedStatement.executeUpdate();
                return Common.Status.OK;
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    /**
     * Checks if an email already exists in the database.
     *
     * @param connection the database connection
     * @param email the email to check
     * @return true if the email exists, false otherwise
     * @throws DatabaseException if there is an error accessing the database
     */
    private boolean doesEmailExist(Connection connection, String email) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(COUNT_USER_EMAIL_SQL)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }
}
