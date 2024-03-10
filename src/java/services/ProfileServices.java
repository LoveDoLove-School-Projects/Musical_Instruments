package services;

import common.Common;
import contollers.ConnectionController;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.Customer;
import request.ProfileRequest;
import response.ProfileResponse;

public class ProfileServices {

    private final String GET_CUSTOMER_PROFILE_SQL = "SELECT * FROM customers WHERE customer_id = ?";

    private final String UPLOAD_PICTURE_SQL = "UPDATE customers SET picture = ? WHERE customer_id = ?";

    private final String REMOVE_PICTURE_SQL = "UPDATE customers SET picture = NULL WHERE customer_id = ?";

    public ProfileResponse getCustomerProfile(ProfileRequest profileRequest) {
        ProfileResponse profileResponse = new ProfileResponse();
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(GET_CUSTOMER_PROFILE_SQL)) {
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
                    Blob picture = resultSet.getBlob("picture");
                    if (picture != null) {
                        int blobLength = (int) picture.length();
                        if (blobLength > 0) {
                            byte[] blobAsBytes = picture.getBytes(1, blobLength);
                            customer.setPicture(blobAsBytes);
                        }
                    }
                    customer.setAccountCreationDate(resultSet.getTimestamp("account_creation_date"));
                    customer.setLastLoginDate(resultSet.getTimestamp("last_login_date"));
                    profileResponse.setStatus(Common.Status.OK);
                    profileResponse.setCustomer(customer);
                } else {
                    profileResponse.setStatus(Common.Status.NOT_FOUND);
                }
            }
        } catch (SQLException ex) {
            profileResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
            System.err.println("Error getting customer profile: " + ex.getMessage());
        }
        return profileResponse;
    }

    public ProfileResponse uploadPicture(ProfileRequest profileRequest) {
        ProfileResponse profileResponse = new ProfileResponse();
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(UPLOAD_PICTURE_SQL)) {
            preparedStatement.setBlob(1, profileRequest.getPicture());
            preparedStatement.setInt(2, profileRequest.getLogin_id());
            int result = preparedStatement.executeUpdate();
            profileResponse.setStatus(result > 0 ? Common.Status.OK : Common.Status.INTERNAL_SERVER_ERROR);
        } catch (SQLException ex) {
            profileResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
            System.err.println("Error uploading picture: " + ex.getMessage());
        }
        return profileResponse;
    }

    public ProfileResponse removePicture(ProfileRequest profileRequest) {
        ProfileResponse profileResponse = new ProfileResponse();
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_PICTURE_SQL)) {
            preparedStatement.setInt(1, profileRequest.getLogin_id());
            int result = preparedStatement.executeUpdate();
            profileResponse.setStatus(result > 0 ? Common.Status.OK : Common.Status.INTERNAL_SERVER_ERROR);
        } catch (SQLException ex) {
            profileResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
            System.err.println("Error removing picture: " + ex.getMessage());
        }
        return profileResponse;
    }
}
