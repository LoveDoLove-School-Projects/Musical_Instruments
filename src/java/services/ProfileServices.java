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
    
    private final String UPDATE_PROFILE_CUSTOMER_SQL = "UPDATE customers SET username = ?, address = ?, phone_number = ?, gender = ?, two_factor_auth = ? WHERE customer_id = ?";
    
    private final String GET_PROFILE_ADMIN_SQL = "SELECT * FROM admins WHERE admin_id = ?";
    
    private final String UPLOAD_PICTURE_ADMIN_SQL = "UPDATE admins SET picture = ? WHERE admin_id = ?";
    
    private final String REMOVE_PICTURE_ADMIN_SQL = "UPDATE admins SET picture = NULL WHERE admin_id = ?";
    
    private final String UPDATE_PROFILE_ADMIN_SQL = "UPDATE admins SET username = ?, address = ?, phone_number = ?, gender = ?, two_factor_auth = ? WHERE admin_id = ?";
    
    public ProfileResponse getProfile(ProfileRequest profileRequest, Common.Role role) {
        String sql = role == Common.Role.CUSTOMER ? GET_PROFILE_CUSTOMER_SQL : GET_PROFILE_ADMIN_SQL;
        return getProfile(profileRequest, sql, role);
    }
    
    public ProfileResponse uploadPicture(ProfileRequest profileRequest, Common.Role role) {
        String sql = role == Common.Role.CUSTOMER ? UPLOAD_PICTURE_CUSTOMER_SQL : UPLOAD_PICTURE_ADMIN_SQL;
        return uploadPicture(profileRequest, sql);
    }
    
    public ProfileResponse removePicture(ProfileRequest profileRequest, Common.Role role) {
        String sql = role == Common.Role.CUSTOMER ? REMOVE_PICTURE_CUSTOMER_SQL : REMOVE_PICTURE_ADMIN_SQL;
        return removePicture(profileRequest, sql);
    }
    
    public ProfileResponse updateProfile(ProfileRequest profileRequest, Common.Role role) {
        String sql = role == Common.Role.CUSTOMER ? UPDATE_PROFILE_CUSTOMER_SQL : UPDATE_PROFILE_ADMIN_SQL;
        return updateProfile(profileRequest, sql);
    }
    
    private String getId(Common.Role role) {
        return role == Common.Role.CUSTOMER ? "customer_id" : "admin_id";
    }
    
    private ProfileResponse getProfile(ProfileRequest profileRequest, String sqlQuery, Common.Role role) {
        ProfileResponse profileResponse = new ProfileResponse();
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, profileRequest.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Profile profile = new Profile();
                    profile.setId(resultSet.getInt(getId(role)));
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
                    profile.setTwo_factor_auth(resultSet.getBoolean("two_factor_auth"));
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
    
    private ProfileResponse uploadPicture(ProfileRequest profileRequest, String sqlQuery) {
        ProfileResponse profileResponse = new ProfileResponse();
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setBlob(1, profileRequest.getPicture());
            preparedStatement.setInt(2, profileRequest.getId());
            int result = preparedStatement.executeUpdate();
            profileResponse.setStatus(result > 0 ? Common.Status.OK : Common.Status.INTERNAL_SERVER_ERROR);
        } catch (SQLException ex) {
            profileResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
            System.err.println("Error uploading picture: " + ex.getMessage());
        }
        return profileResponse;
    }
    
    private ProfileResponse removePicture(ProfileRequest profileRequest, String sqlQuery) {
        ProfileResponse profileResponse = new ProfileResponse();
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, profileRequest.getId());
            int result = preparedStatement.executeUpdate();
            profileResponse.setStatus(result > 0 ? Common.Status.OK : Common.Status.INTERNAL_SERVER_ERROR);
        } catch (SQLException ex) {
            profileResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
            System.err.println("Error removing picture: " + ex.getMessage());
        }
        return profileResponse;
    }
    
    public ProfileResponse updateProfile(ProfileRequest profileRequest, String sqlQuery) {
        ProfileResponse profileResponse = new ProfileResponse();
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, profileRequest.getUsername());
            preparedStatement.setString(2, profileRequest.getAddress());
            preparedStatement.setString(3, profileRequest.getPhoneNumber());
            preparedStatement.setString(4, profileRequest.getGender());
            preparedStatement.setBoolean(5, profileRequest.isTwo_factor_auth());
            preparedStatement.setInt(6, profileRequest.getId());
            int result = preparedStatement.executeUpdate();
            profileResponse.setStatus(result > 0 ? Common.Status.OK : Common.Status.INTERNAL_SERVER_ERROR);
        } catch (SQLException ex) {
            profileResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
            System.err.println("Error updating profile: " + ex.getMessage());
        }
        return profileResponse;
    }
}
