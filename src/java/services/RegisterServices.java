package services;

import contollers.ConnectionController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.Customer;
import utilities.AesUtilities;
import utilities.LoggerUtilities;

public class RegisterServices {

    public int registerNewCustomer(Customer customer) throws Exception {
        String sqlString = "INSERT INTO customers (username, password, email, address, phone_number, gender) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionController.getConnection()) {
            if (isEmailExists(connection, customer.getEmail())) {
                return 0;
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlString)) {
                preparedStatement.setString(1, customer.getUsername());
                preparedStatement.setString(2, AesUtilities.aes256EcbEncrypt(customer.getPassword()));
                preparedStatement.setString(3, customer.getEmail());
                preparedStatement.setString(4, customer.getAddress());
                preparedStatement.setString(5, customer.getPhoneNumber());
                preparedStatement.setString(6, customer.getGender());

                return preparedStatement.executeUpdate();
            }
        } catch (Exception ex) {
            LoggerUtilities.logSevere("Error while registering new customer: ", ex);
            throw ex;
        }
    }

    private boolean isEmailExists(Connection connection, String email) throws SQLException {
        String sqlString = "SELECT COUNT(*) FROM customers WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlString)) {
            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            LoggerUtilities.logSevere("Error while checking if email exists: ", ex);
            throw ex;
        }

        return false;
    }
}
