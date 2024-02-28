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

    public int insertCustomer(Customer customer) {
        int status = 0;
        try {
            connection = ConnectionController.getConnection();

            if (isEmailExists(customer.getEmail())) {
                return 0;
            }

            String sqlString = "INSERT INTO customers (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sqlString);

            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getPassword());

            status = preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException ex) {
            LoggerUtilities.logSevere(ex);
        }
        return status;
    }

    private boolean isEmailExists(String email) throws SQLException {
        boolean exists = false;
        String sqlString = "SELECT COUNT(*) FROM customers WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlString)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    exists = count > 0;
                }
            }
        }
        return exists;
    }
}
