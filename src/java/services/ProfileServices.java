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

    private static final String GET_PROFILE_SQL = "SELECT * FROM users WHERE user_id = ?";
    private static final String UPLOAD_PICTURE_SQL = "UPDATE users SET picture = ? WHERE user_id = ?";
    private static final String REMOVE_PICTURE_SQL = "UPDATE users SET picture = NULL WHERE user_id = ?";
    private static final String UPDATE_PROFILE_SQL = "UPDATE users SET username = ?, address = ?, phone_number = ?, gender = ?, two_factor_auth = ? WHERE user_id = ?";

    public ProfileResponse getProfile(ProfileRequest profileRequest, Common.Role role) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(GET_PROFILE_SQL)) {
            preparedStatement.setInt(1, profileRequest.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("user_id");
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
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(UPLOAD_PICTURE_SQL)) {
            preparedStatement.setBytes(1, profileRequest.getPicture());
            preparedStatement.setInt(2, profileRequest.getId());
            preparedStatement.executeUpdate();
            return Common.Status.OK;
        } catch (SQLException ex) {
            throw new DatabaseAccessException("Error uploading picture", ex);
        }
    }

    public Common.Status removePicture(ProfileRequest profileRequest, Common.Role role) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_PICTURE_SQL)) {
            preparedStatement.setInt(1, profileRequest.getId());
            preparedStatement.executeUpdate();
            return Common.Status.OK;
        } catch (SQLException ex) {
            throw new DatabaseAccessException("Error removing picture", ex);
        }
    }

    public ProfileResponse updateProfile(ProfileRequest profileRequest, Common.Role role) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PROFILE_SQL)) {
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
}
