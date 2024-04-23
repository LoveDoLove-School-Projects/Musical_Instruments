package services;

import controllers.ConnectionController;
import entities.Customers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Logger;

public class StaffServices {

    private static final Logger LOG = Logger.getLogger(StaffServices.class.getName());
    private static final String MODIFY_CUSTOMER_DETAILS_SQL = "UPDATE customers SET username = ?, gender = ?, address = ?, phone_number = ? WHERE user_id = ?";
    private static final String SEARCH_CUSTOMER_BY_ID_SQL = "SELECT * FROM customers WHERE user_id = ?";
    private static final String SEARCH_CUSTOMER_BY_EMAIL_SQL = "SELECT * FROM customers WHERE email = ?";

    public boolean modifyCustomerDetailsDB(Customers customer) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(MODIFY_CUSTOMER_DETAILS_SQL)) {
            preparedStatement.setString(1, customer.getUsername());
            preparedStatement.setString(2, customer.getGender());
            preparedStatement.setString(3, customer.getAddress());
            preparedStatement.setString(4, customer.getPhoneNumber());
            preparedStatement.setInt(5, customer.getUserId());
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
            return false;
        }
    }

    public Customers searchCustomerDetailsDB(int userId) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_CUSTOMER_BY_ID_SQL)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("user_id");
                    String username = resultSet.getString("username");
                    String email = resultSet.getString("email");
                    String address = resultSet.getString("address");
                    String phoneNumber = resultSet.getString("phone_number");
                    String gender = resultSet.getString("gender");
                    boolean twoFactorAuth = resultSet.getBoolean("two_factor_auth");
                    Timestamp accountCreationDate = resultSet.getTimestamp("account_creation_date");
                    return new Customers(id, username, email, address, phoneNumber, gender, twoFactorAuth, accountCreationDate);
                }
            }
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
        }
        return null;
    }

    public Customers searchCustomerDetailsDB(String email) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_CUSTOMER_BY_EMAIL_SQL)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("user_id");
                    String username = resultSet.getString("username");
                    email = resultSet.getString("email");
                    String address = resultSet.getString("address");
                    String phoneNumber = resultSet.getString("phone_number");
                    String gender = resultSet.getString("gender");
                    boolean twoFactorAuth = resultSet.getBoolean("two_factor_auth");
                    Timestamp accountCreationDate = resultSet.getTimestamp("account_creation_date");
                    return new Customers(id, username, email, address, phoneNumber, gender, twoFactorAuth, accountCreationDate);
                }
            }
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
        }
        return null;
    }
}
