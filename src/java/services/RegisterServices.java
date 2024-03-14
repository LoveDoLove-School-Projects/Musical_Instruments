package services;

import common.Common;
import common.Constants;
import contollers.ConnectionController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import request.RegisterRequest;
import response.RegisterResponse;
import utilities.AesUtilities;
import utilities.RandomUtilities;

public class RegisterServices {

    private final String OTP_EMAIL_SUBJECT = Constants.COMPANY_NAME + " OTP";
    private final String OTP_EMAIL_BODY = "Your OTP is: ";

    private final String ADD_NEW_CUSTOMER_SQL = "INSERT INTO customers (username, password, email, address, phone_number, gender) VALUES (?, ?, ?, ?, ?, ?)";
    private final String COUNT_EMAIL_SQL = "SELECT COUNT(*) FROM customers WHERE email = ?";

    private final String GET_OTP_SQL = "SELECT otp FROM otps WHERE email = ?";
    private final String COUNT_OTP_SQL = "SELECT COUNT(*) FROM otps WHERE email = ?";
    private final String ADD_OTP_SQL = "INSERT INTO OTPS(email, otp) VALUES(?, ?)";
    private final String UPDATE_OTP_SQL = "UPDATE otps SET otp = ? WHERE email = ?";
    private final String DELETE_OTP_SQL = "DELETE FROM otps WHERE email = ?";

    public RegisterResponse addNewCustomer(RegisterRequest registerRequest) {
        RegisterResponse registerResponse = new RegisterResponse();

        try (Connection connection = ConnectionController.getConnection()) {
            if (isEmailExists(connection, registerRequest.getEmail())) {
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
                if (registerResponse.getStatus() == Common.Status.OK) {
                    deleteOtp(connection, registerRequest.getEmail());
                }
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

    public RegisterResponse getOtp(String email) {
        RegisterResponse registerResponse = new RegisterResponse();
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(GET_OTP_SQL)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    registerResponse.setOtp(resultSet.getString("otp"));
                    registerResponse.setStatus(Common.Status.OK);
                } else {
                    registerResponse.setStatus(Common.Status.NOT_FOUND);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error establishing database connection: " + ex.getMessage());
            registerResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
        }
        return registerResponse;
    }

    public RegisterResponse sendOtp(String email) {
        RegisterResponse registerResponse = new RegisterResponse();
        String otp = RandomUtilities.generateOtp();
        try (Connection connection = ConnectionController.getConnection()) {
            Boolean otpExists = isOtpExists(connection, email);
            if (otpExists == null || (otpExists ? updateOtp(connection, email, otp) : addOtp(connection, email, otp))) {
                registerResponse.setStatus(Common.Status.OK);
                new MailServices().sendEmail(email, OTP_EMAIL_SUBJECT, OTP_EMAIL_BODY + otp);
            } else {
                registerResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException ex) {
            System.err.println("Error establishing database connection: " + ex.getMessage());
            registerResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
        }
        return registerResponse;
    }

    private boolean addOtp(Connection connection, String email, String otp) {
        return executeUpdate(connection, ADD_OTP_SQL, email, otp);
    }

    private boolean updateOtp(Connection connection, String email, String otp) {
        return executeUpdate(connection, UPDATE_OTP_SQL, otp, email);
    }

    private boolean deleteOtp(Connection connection, String email) {
        return executeUpdate(connection, DELETE_OTP_SQL, email, null);
    }

    private Boolean isOtpExists(Connection connection, String email) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(COUNT_OTP_SQL)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error checking if OTP exists: " + ex.getMessage());
            return null;
        }
        return false;
    }

    private boolean executeUpdate(Connection connection, String sql, String param1, String param2) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, param1);
            preparedStatement.setString(2, param2);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("Error executing update: " + ex.getMessage());
            return false;
        }
    }

    private boolean isEmailExists(Connection connection, String email) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(COUNT_EMAIL_SQL)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error checking if email exists: " + ex.getMessage());
        }
        return false;
    }
}
