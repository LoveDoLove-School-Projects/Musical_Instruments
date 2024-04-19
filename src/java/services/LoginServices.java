package services;

import controllers.ConnectionController;
import domain.common.Common;
import domain.request.LoginRequest;
import domain.response.LoginResponse;
import features.AesHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class LoginServices {

    private static final Logger LOG = Logger.getLogger(LoginServices.class.getName());
    private static final String LOGIN_SQL = "SELECT * FROM table_name WHERE email = ? AND password = ?";

    public LoginResponse loginServices(LoginRequest loginRequest, Common.Role role) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(replaceToCorrectTableName(LOGIN_SQL, role))) {
            preparedStatement.setString(1, loginRequest.getEmail());
            preparedStatement.setString(2, AesHandler.aes256EcbEncrypt(loginRequest.getPassword()));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("user_id");
                    String email = resultSet.getString("email");
                    boolean two_factor_auth = resultSet.getBoolean("two_factor_auth");
                    return new LoginResponse(Common.Status.OK, id, email, two_factor_auth);
                }
                return new LoginResponse(Common.Status.UNAUTHORIZED);
            }
        } catch (SQLException ex) {
            LOG.severe(ex.getMessage());
            return null;
        }
    }

    private String replaceToCorrectTableName(String sql, Common.Role role) {
        return sql.replace("table_name", role.getRole());
    }
}
