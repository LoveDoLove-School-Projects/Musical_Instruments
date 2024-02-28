package services;

import contollers.ConnectionController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.Customer;
import utilities.LoggerUtilities;

public class LoginServices {

    private static Connection connection;
    private static PreparedStatement preparedStatement;

    public int loginCustomer(Customer customer) {
        int status = 0;
        try {
            connection = ConnectionController.getConnection();

            if (authenticateCustomer(customer.getEmail(), customer.getPassword())) {
                status = 1;
            }

            connection.close();
        } catch (SQLException ex) {
            LoggerUtilities.logSevere(ex);
        }
        return status;
    }

    private boolean authenticateCustomer(String email, String password) throws SQLException {
        boolean correct = false;
        String sqlString = "SELECT password FROM customers WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlString)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    correct = password.equals(storedPassword);
                }
            }
        }
        return correct;
    }
}
