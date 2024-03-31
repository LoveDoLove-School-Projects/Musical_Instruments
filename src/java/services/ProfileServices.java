package services;

import controllers.ConnectionController;
import domain.common.Common;
import domain.request.ProfileRequest;
import domain.response.ProfileResponse;
import exceptions.DatabaseAccessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ProfileServices {

    private static final String GET_PROFILE_CUSTOMER_SQL = "SELECT * FROM customers WHERE customer_id = ?";
    private static final String UPLOAD_PICTURE_CUSTOMER_SQL = "UPDATE customers SET picture = ? WHERE customer_id = ?";
    private static final String REMOVE_PICTURE_CUSTOMER_SQL = "UPDATE customers SET picture = NULL WHERE customer_id = ?";
    private static final String UPDATE_PROFILE_CUSTOMER_SQL = "UPDATE customers SET username = ?, address = ?, phone_number = ?, gender = ?, two_factor_auth = ? WHERE customer_id = ?";
    private static final String GET_PROFILE_ADMIN_SQL = "SELECT * FROM admins WHERE admin_id = ?";
    private static final String UPLOAD_PICTURE_ADMIN_SQL = "UPDATE admins SET picture = ? WHERE admin_id = ?";
    private static final String REMOVE_PICTURE_ADMIN_SQL = "UPDATE admins SET picture = NULL WHERE admin_id = ?";
    private static final String UPDATE_PROFILE_ADMIN_SQL = "UPDATE admins SET username = ?, address = ?, phone_number = ?, gender = ?, two_factor_auth = ? WHERE admin_id = ?";

    public ProfileResponse getProfile(ProfileRequest profileRequest, Common.Role role) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(getProfile(role))) {
            preparedStatement.setInt(1, profileRequest.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt(getId(role));
                    String username = resultSet.getString("username");
                    String email = resultSet.getString("email");
                    String address = resultSet.getString("address");
                    String phoneNumber = resultSet.getString("phone_number");
                    String gender = resultSet.getString("gender");
                    boolean twoFactorAuth = resultSet.getBoolean("two_factor_auth");
                    byte[] pictureBytes = resultSet.getBytes("picture");
                    Timestamp accountCreationDate = resultSet.getTimestamp("account_creation_date");
                    Timestamp lastLoginDate = resultSet.getTimestamp("last_login_date");
                    return new ProfileResponse(Common.Status.OK, id, username, email, address, phoneNumber, gender, pictureBytes, twoFactorAuth, accountCreationDate, lastLoginDate);
                } else {
                    return new ProfileResponse(Common.Status.NOT_FOUND);
                }
            }
        } catch (SQLException ex) {
            throw new DatabaseAccessException("Database error while getting profile", ex);
        }
    }

    public Common.Status uploadPicture(ProfileRequest profileRequest, Common.Role role) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(uploadPicture(role))) {
            preparedStatement.setBytes(1, profileRequest.getPicture());
            preparedStatement.setInt(2, profileRequest.getId());
            preparedStatement.executeUpdate();
            return Common.Status.OK;
        } catch (SQLException ex) {
            throw new DatabaseAccessException("Error uploading picture", ex);
        }
    }

    public Common.Status removePicture(ProfileRequest profileRequest, Common.Role role) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(removePicture(role))) {
            preparedStatement.setInt(1, profileRequest.getId());
            preparedStatement.executeUpdate();
            return Common.Status.OK;
        } catch (SQLException ex) {
            throw new DatabaseAccessException("Error removing picture", ex);
        }
    }

    public ProfileResponse updateProfile(ProfileRequest profileRequest, Common.Role role) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(updateProfile(role))) {
            preparedStatement.setString(1, profileRequest.getUsername());
            preparedStatement.setString(2, profileRequest.getAddress());
            preparedStatement.setString(3, profileRequest.getPhoneNumber());
            preparedStatement.setString(4, profileRequest.getGender());
            preparedStatement.setBoolean(5, profileRequest.isTwo_factor_auth());
            preparedStatement.setInt(6, profileRequest.getId());
            preparedStatement.executeUpdate();
            return new ProfileResponse(Common.Status.OK);
        } catch (SQLException ex) {
            throw new DatabaseAccessException("Error updating profile", ex);
        }
    }

    private String getProfile(Common.Role role) {
        return role == Common.Role.CUSTOMER ? GET_PROFILE_CUSTOMER_SQL : GET_PROFILE_ADMIN_SQL;
    }

    private String uploadPicture(Common.Role role) {
        return role == Common.Role.CUSTOMER ? UPLOAD_PICTURE_CUSTOMER_SQL : UPLOAD_PICTURE_ADMIN_SQL;
    }

    private String removePicture(Common.Role role) {
        return role == Common.Role.CUSTOMER ? REMOVE_PICTURE_CUSTOMER_SQL : REMOVE_PICTURE_ADMIN_SQL;
    }

    private String updateProfile(Common.Role role) {
        return role == Common.Role.CUSTOMER ? UPDATE_PROFILE_CUSTOMER_SQL : UPDATE_PROFILE_ADMIN_SQL;
    }

    private String getId(Common.Role role) {
        return role == Common.Role.CUSTOMER ? "customer_id" : "admin_id";
    }
}
