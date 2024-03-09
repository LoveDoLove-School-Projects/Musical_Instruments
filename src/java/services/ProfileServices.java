package services;

import common.Common;
import contollers.ConnectionController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.Customer;
import request.ProfileRequest;
import response.ProfileResponse;

public class ProfileServices {

    private final String GET_CUSTOMER_PROFILE_SQL = "SELECT * FROM customers WHERE customer_id = ?";

    public ProfileResponse getCustomerProfile(ProfileRequest profileRequest) throws SQLException {
        ProfileResponse profileResponse = new ProfileResponse();
        try (Connection connection = ConnectionController.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_CUSTOMER_PROFILE_SQL)) {
                preparedStatement.setInt(1, profileRequest.getLogin_id());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Customer customer = new Customer();
                        customer.setCustomerId(resultSet.getInt("customer_id"));
                        customer.setUsername(resultSet.getString("username"));
                        customer.setEmail(resultSet.getString("email"));
                        customer.setAddress(resultSet.getString("address"));
                        customer.setPhoneNumber(resultSet.getString("phone_number"));
                        customer.setGender(resultSet.getString("gender"));
                        customer.setAccountCreationDate(resultSet.getTimestamp("account_creation_date"));
                        customer.setLastLoginDate(resultSet.getTimestamp("last_login_date"));
                        profileResponse.setStatus(Common.Status.OK);
                        profileResponse.setCustomer(customer);
                    } else {
                        profileResponse.setStatus(Common.Status.NOT_FOUND);
                    }
                }
                return profileResponse;
            }
        } catch (SQLException ex) {
            throw new SQLException("Error getting customer profile: " + ex.getMessage());
        }
    }
}
