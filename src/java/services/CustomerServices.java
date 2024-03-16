package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerServices {

    private static final String COUNT_CUSTOMER_EMAIL_SQL = "SELECT COUNT(*) FROM customers WHERE email = ?";

    public boolean isEmailExists(Connection connection, String email) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(COUNT_CUSTOMER_EMAIL_SQL)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error checking if email exists: " + ex.getMessage());
        }
        return false;
    }
}
