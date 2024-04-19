package services;

import controllers.ConnectionController;
import domain.models.Users;
import domain.request.LoginRequest;
import exceptions.DatabaseException;
import features.AesHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminLoginServices {

    private static final String ADMIN_LOGIN_SQL = "SELECT * FROM admins WHERE email = ? AND password = ?";

    public Users loginToAdmin(LoginRequest loginRequest) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(ADMIN_LOGIN_SQL)) {
            preparedStatement.setString(1, loginRequest.getEmail());
            preparedStatement.setString(2, AesHandler.aes256EcbEncrypt(loginRequest.getPassword()));
            try (ResultSet adminResultSet = preparedStatement.executeQuery()) {
                if (adminResultSet.next()) {
                    int id = adminResultSet.getInt("user_id");
                    String email = adminResultSet.getString("email");
                    boolean twoFactorAuth = adminResultSet.getBoolean("two_factor_auth");
                    return new Users(id, email, twoFactorAuth);
                }
            }
            return null;
        } catch (Exception ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }
}
