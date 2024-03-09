package services;

import contollers.ConnectionController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.Customer;
import utilities.AesUtilities;

public class LoginServices {

    public int loginCustomer(Customer customer) throws Exception {
        try (Connection connection = ConnectionController.getConnection()) {
            return authenticateCustomer(connection, customer);
        }
    }

    private int authenticateCustomer(Connection connection, Customer customer) throws Exception {
        String sqlString = "SELECT * FROM customers WHERE email = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlString)) {
            preparedStatement.setString(1, customer.getEmail());
            preparedStatement.setString(2, AesUtilities.aes256EcbEncrypt(customer.getPassword()));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int customerId = resultSet.getInt("customer_id");
                    updateLastLoginDate(connection, customerId);
                    return customerId;
                }
            }
        } catch (Exception ex) {
            throw new Exception("Error authenticating customer: " + ex.getMessage());
        }
        return 0;
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
