package services;

import controllers.ConnectionController;
import domain.models.Users;
import domain.request.LoginRequest;
import exceptions.DatabaseException;
import features.AesHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerLoginServices {

    private static final String CUSTOMER_LOGIN_SQL = "SELECT * FROM customers WHERE email = ? AND password = ?";

    public Users loginToCustomer(LoginRequest loginRequest) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(CUSTOMER_LOGIN_SQL)) {
            preparedStatement.setString(1, loginRequest.getEmail());
            preparedStatement.setString(2, AesHandler.aes256EcbEncrypt(loginRequest.getPassword()));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("user_id");
                    String email = resultSet.getString("email");
                    boolean two_factor_auth = resultSet.getBoolean("two_factor_auth");
                    return new Users(id, email, two_factor_auth);
                }
            }
            return null;
        } catch (Exception ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }
}
