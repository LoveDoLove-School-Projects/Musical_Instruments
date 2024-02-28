package services;

import contollers.ConnectionController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import utilities.LoggerUtilities;
import models.Customer;

public class LoginServices {

    private static Connection connection;
    private static PreparedStatement preparedStatement;

    public int loginCustomer(Customer customer) {
        int customer_id = 0;
        try {
            connection = ConnectionController.getConnection();
            customer_id = authenticateCustomer(customer.getEmail(), customer.getPassword());
            connection.close();
        } catch (SQLException ex) {
            LoggerUtilities.logSevere(ex);
        }
        return customer_id;
    }

    public int authenticateCustomer(String email, String password) {
        int customer_id = 0;
        String sqlString = "SELECT * FROM customers WHERE email = ? AND password = ?";
        try {
            connection = ConnectionController.getConnection();
            preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    customer_id = resultSet.getInt("customer_id");
                }
            }
        } catch (SQLException ex) {
            LoggerUtilities.logSevere(ex);
        }
        return customer_id;
    }
}
