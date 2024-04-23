package services;

import controllers.ConnectionController;
import entities.Resetpassword;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Logger;

public class ResetPasswordServices {

    private static final Logger LOG = Logger.getLogger(ResetPasswordServices.class.getName());
    private static final String ADD_NEW_RESET_PASSWORD_SQL = "INSERT INTO resetpassword (email, token) VALUES (?, ?)";

    public boolean addNewResetPassword(Resetpassword resetPassword) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(ADD_NEW_RESET_PASSWORD_SQL)) {
            preparedStatement.setString(1, resetPassword.getEmail());
            preparedStatement.setString(2, resetPassword.getToken());
            return preparedStatement.executeUpdate() > 0;
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
            return false;
        }
    }
}
