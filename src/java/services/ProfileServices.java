package services;

import controllers.ConnectionController;
import domain.models.Users;
import domain.request.ProfileRequest;
import exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ProfileServices {

    private static final String GET_PROFILE_SQL = "SELECT * FROM customers WHERE user_id = ?";
    private static final String UPLOAD_PICTURE_SQL = "UPDATE customers SET picture = ? WHERE user_id = ?";
    private static final String REMOVE_PICTURE_SQL = "UPDATE customers SET picture = NULL WHERE user_id = ?";
    private static final String UPDATE_PROFILE_SQL = "UPDATE customers SET username = ?, address = ?, phone_number = ?, gender = ?, two_factor_auth = ? WHERE user_id = ?";

    public Users getProfile(ProfileRequest profileRequest) {
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
     * @return true if the picture was successfully uploaded, false otherwise.
     * @throws DatabaseException if there is an error accessing the database.
     */
    public boolean uploadPicture(ProfileRequest profileRequest) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(UPLOAD_PICTURE_SQL)) {
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
     * @return true if the picture was successfully removed, false otherwise.
     * @throws DatabaseException if there is an error accessing the database.
     */
    public boolean removePicture(ProfileRequest profileRequest) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_PICTURE_SQL)) {
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
     * @return true if the profile was successfully updated, false otherwise.
     * @throws DatabaseException if there is an error updating the profile in
     * the database.
     */
    public boolean updateProfile(ProfileRequest profileRequest) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PROFILE_SQL)) {
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
}
