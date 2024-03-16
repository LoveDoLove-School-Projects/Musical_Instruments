package services;

import common.Common;
import contollers.ConnectionController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import request.RegisterRequest;
import response.RegisterResponse;
import utilities.AesUtilities;

public class RegisterServices {

    private static final CustomerServices CUSTOMER_SERVICES = new CustomerServices();

    private static final String ADD_NEW_CUSTOMER_SQL = "INSERT INTO customers (username, password, email, address, phone_number, gender) VALUES (?, ?, ?, ?, ?, ?)";

    public RegisterResponse addNewCustomer(RegisterRequest registerRequest) {
        RegisterResponse registerResponse = new RegisterResponse();

        try (Connection connection = ConnectionController.getConnection()) {
            if (CUSTOMER_SERVICES.isEmailExists(connection, registerRequest.getEmail())) {
                registerResponse.setStatus(Common.Status.EXISTS);
                return registerResponse;
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_NEW_CUSTOMER_SQL)) {
                preparedStatement.setString(1, registerRequest.getUsername());
                preparedStatement.setString(2, AesUtilities.aes256EcbEncrypt(registerRequest.getPassword()));
                preparedStatement.setString(3, registerRequest.getEmail());
                preparedStatement.setString(4, registerRequest.getAddress());
                preparedStatement.setString(5, registerRequest.getPhoneNumber());
                preparedStatement.setString(6, registerRequest.getGender());
                int update = preparedStatement.executeUpdate();
                registerResponse.setStatus(update > 0 ? Common.Status.OK : Common.Status.INTERNAL_SERVER_ERROR);
            } catch (SQLException ex) {
                System.err.println("Error adding new customer: " + ex.getMessage());
                registerResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException ex) {
            System.err.println("Error establishing database connection: " + ex.getMessage());
            registerResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
        }
        return registerResponse;
    }
}
