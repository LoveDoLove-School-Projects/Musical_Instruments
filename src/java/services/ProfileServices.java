package services;

import common.Common;
import contollers.ConnectionController;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.Profile;
import request.ProfileRequest;
import response.ProfileResponse;

public class ProfileServices {

    private final String GET_PROFILE_CUSTOMER_SQL = "SELECT * FROM customers WHERE customer_id = ?";

    private final String UPLOAD_PICTURE_CUSTOMER_SQL = "UPDATE customers SET picture = ? WHERE customer_id = ?";

    private final String REMOVE_PICTURE_CUSTOMER_SQL = "UPDATE customers SET picture = NULL WHERE customer_id = ?";

    private final String GET_PROFILE_ADMIN_SQL = "SELECT * FROM admins WHERE admin_id = ?";

    private final String UPLOAD_PICTURE_ADMIN_SQL = "UPDATE admins SET picture = ? WHERE admin_id = ?";

    private final String REMOVE_PICTURE_ADMIN_SQL = "UPDATE admins SET picture = NULL WHERE admin_id = ?";

    public ProfileResponse getProfile(ProfileRequest profileRequest, Common.Role role) {
        String sql = role == Common.Role.CUSTOMER ? GET_PROFILE_CUSTOMER_SQL : GET_PROFILE_ADMIN_SQL;
        return getProfile(profileRequest, sql, role);
    }

    private ProfileResponse getProfile(ProfileRequest profileRequest, String sqlQuery, Common.Role role) {
        ProfileResponse profileResponse = new ProfileResponse();
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, profileRequest.getLogin_id());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Profile profile = new Profile();
                    switch (role) {
                        case CUSTOMER:
                            profile.setProfileId(resultSet.getInt("customer_id"));
                            break;
                        case ADMIN:
                            profile.setProfileId(resultSet.getInt("admin_id"));
                            break;
                    }
                    profile.setUsername(resultSet.getString("username"));
                    profile.setEmail(resultSet.getString("email"));
                    profile.setAddress(resultSet.getString("address"));
                    profile.setPhoneNumber(resultSet.getString("phone_number"));
                    profile.setGender(resultSet.getString("gender"));
                    Blob picture = resultSet.getBlob("picture");
                    if (picture != null) {
                        int blobLength = (int) picture.length();
                        if (blobLength > 0) {
                            byte[] blobAsBytes = picture.getBytes(1, blobLength);
                            profile.setPicture(blobAsBytes);
                        }
                    }
                    profile.setAccountCreationDate(resultSet.getTimestamp("account_creation_date"));
                    profile.setLastLoginDate(resultSet.getTimestamp("last_login_date"));
                    profileResponse.setStatus(Common.Status.OK);
                    profileResponse.setProfile(profile);
                } else {
                    profileResponse.setStatus(Common.Status.NOT_FOUND);
                }
            }
        } catch (SQLException ex) {
            profileResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
            System.err.println("Error getting " + role.name().toLowerCase() + " profile: " + ex.getMessage());
        }
        return profileResponse;
    }

    public ProfileResponse uploadPicture(ProfileRequest profileRequest, Common.Role role) {
        String sql = role == Common.Role.CUSTOMER ? UPLOAD_PICTURE_CUSTOMER_SQL : UPLOAD_PICTURE_ADMIN_SQL;
        return uploadPicture(profileRequest, sql);
    }

    public ProfileResponse uploadPicture(ProfileRequest profileRequest, String sqlQuery) {
        ProfileResponse profileResponse = new ProfileResponse();
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
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

    public ProfileResponse removePicture(ProfileRequest profileRequest, Common.Role role) {
        String sql = role == Common.Role.CUSTOMER ? REMOVE_PICTURE_CUSTOMER_SQL : REMOVE_PICTURE_ADMIN_SQL;
        return removePicture(profileRequest, sql);
    }

    public ProfileResponse removePicture(ProfileRequest profileRequest, String sqlQuery) {
        ProfileResponse profileResponse = new ProfileResponse();
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
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
