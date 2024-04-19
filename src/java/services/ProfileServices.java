package services;

import controllers.ConnectionController;
import domain.common.Common;
import domain.models.Users;
import domain.request.ProfileRequest;
import exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ProfileServices {

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
            throw new DatabaseException(ex.getMessage());
        }
    }

    /**
     * Uploads a picture for a profile.
     *
     * @param profileRequest The profile request containing the picture and
     * profile ID.
     * @param role The role of the user uploading the picture.
     * @return true if the picture was successfully uploaded, false otherwise.
     * @throws DatabaseException if there is an error accessing the database.
     */
    public boolean uploadPicture(ProfileRequest profileRequest, Common.Role role) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(replaceToCorrectTableName(UPLOAD_PICTURE_SQL, role))) {
            preparedStatement.setBytes(1, profileRequest.getPicture());
            preparedStatement.setInt(2, profileRequest.getId());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    /**
     * Removes the picture associated with the given profile request.
     *
     * @param profileRequest The profile request containing the ID of the
     * profile.
     * @param role The role of the user performing the action.
     * @return true if the picture was successfully removed, false otherwise.
     * @throws DatabaseException if there is an error accessing the database.
     */
    public boolean removePicture(ProfileRequest profileRequest, Common.Role role) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(replaceToCorrectTableName(REMOVE_PICTURE_SQL, role))) {
            preparedStatement.setInt(1, profileRequest.getId());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    /**
     * Updates the profile information for a user.
     *
     * @param profileRequest The profile request object containing the updated
     * profile information.
     * @param role The role of the user.
     * @return true if the profile was successfully updated, false otherwise.
     * @throws DatabaseException if there is an error updating the profile in
     * the database.
     */
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
            throw new DatabaseException(ex.getMessage());
        }
    }

    /**
     * Replaces the placeholder "table_name" in the given SQL string with the
     * correct table name based on the specified role.
     *
     * @param sql the SQL string with the placeholder "table_name"
     * @param role the role used to determine the correct table name
     * @return the SQL string with the placeholder replaced by the correct table
     * name
     */
    private String replaceToCorrectTableName(String sql, Common.Role role) {
        return sql.replace("table_name", role.getRole());
    }
}
