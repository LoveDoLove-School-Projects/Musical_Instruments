package services;

import contollers.ConnectionController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import utilities.LoggerUtilities;
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
                    return resultSet.getInt("customer_id");
                }
            }
        } catch (Exception ex) {
            LoggerUtilities.logSevere(ex.getMessage(), ex);
            throw ex;
        }
        return 0;
    }
}
