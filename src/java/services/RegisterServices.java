package services;

import contollers.ConnectionController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.Customer;
import utilities.LoggerUtilities;

public class RegisterServices {

    private static Connection connection;
    private static PreparedStatement preparedStatement;

    public int registerNewCustomer(Customer customer) {
        int status = 0;
        try {
            connection = ConnectionController.getConnection();
            if (isEmailExists(customer.getEmail())) {
                return status;
            }
            String sqlString = "INSERT INTO customers (username, password, email, address, phone_number, gender) VALUES (?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, customer.getUsername());
            preparedStatement.setString(2, customer.getPassword());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getAddress());
            preparedStatement.setString(5, customer.getPhoneNumber());
            preparedStatement.setString(6, customer.getGender());
            status = preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException ex) {
            LoggerUtilities.logSevere(ex);
        }
        return status;
    }

    private boolean isEmailExists(String email) {
        boolean exists = false;
        String sqlString = "SELECT COUNT(*) FROM customers WHERE email = ?";
        try {
            preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    exists = count > 0;
                }
            }
        } catch (SQLException ex) {
            LoggerUtilities.logSevere(ex);
        }
        return exists;
    }
}
