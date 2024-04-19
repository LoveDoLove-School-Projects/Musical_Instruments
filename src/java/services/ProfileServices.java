package services;

import controllers.ConnectionController;
import domain.common.Common;
import domain.models.Users;
import domain.request.ProfileRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Logger;

public class ProfileServices {

    private static final Logger LOG = Logger.getLogger(ProfileServices.class.getName());
    private static final String GET_PROFILE_SQL = "SELECT * FROM table_name WHERE user_id = ?";
    private static final String UPLOAD_PICTURE_SQL = "UPDATE table_name SET picture = ? WHERE user_id = ?";
    private static final String REMOVE_PICTURE_SQL = "UPDATE table_name SET picture = NULL WHERE user_id = ?";
    private static final String UPDATE_PROFILE_SQL = "UPDATE table_name SET username = ?, address = ?, phone_number = ?, gender = ?, two_factor_auth = ? WHERE user_id = ?";

    public Users getProfile(ProfileRequest profileRequest, Common.Role role) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(replaceToCorrectTableName(GET_PROFILE_SQL, role))) {
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
                    return new Users(id, username, email, address, phoneNumber, gender, pictureBytes, twoFactorAuth, accountCreationDate);
                }
                return null;
            }
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
            return null;
        }
    }

    public boolean uploadPicture(ProfileRequest profileRequest, Common.Role role) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(replaceToCorrectTableName(UPLOAD_PICTURE_SQL, role))) {
            preparedStatement.setBytes(1, profileRequest.getPicture());
            preparedStatement.setInt(2, profileRequest.getId());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            LOG.severe(ex.getMessage());
            return false;
        }
    }

    public boolean removePicture(ProfileRequest profileRequest, Common.Role role) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(replaceToCorrectTableName(REMOVE_PICTURE_SQL, role))) {
            preparedStatement.setInt(1, profileRequest.getId());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            LOG.severe(ex.getMessage());
            return false;
        }
    }

    public boolean updateProfile(ProfileRequest profileRequest, Common.Role role) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(replaceToCorrectTableName(UPDATE_PROFILE_SQL, role))) {
            preparedStatement.setString(1, profileRequest.getUsername());
            preparedStatement.setString(2, profileRequest.getAddress());
            preparedStatement.setString(3, profileRequest.getPhoneNumber());
            preparedStatement.setString(4, profileRequest.getGender());
            preparedStatement.setBoolean(5, profileRequest.isTwo_factor_auth());
            preparedStatement.setInt(6, profileRequest.getId());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            LOG.severe("Error updating profile: " + ex.getMessage());
            return false;
        }
    }

    private String replaceToCorrectTableName(String sql, Common.Role role) {
        return sql.replace("table_name", role.getRole());
    }
}
