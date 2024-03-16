package services;

import common.Common;
import contollers.ConnectionController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import response.MailResponse;
import response.OtpResponse;
import utilities.RandomUtilities;
import utilities.StringUtilities;

public class OtpServices {

    private final MailServices mailServices = new MailServices();

    private final String OTP_EMAIL_SUBJECT = "OTP";
    private final String OTP_EMAIL_BODY = "Your OTP is: ";

    private final String GET_OTP_SQL = "SELECT * FROM otps WHERE email = ? AND otp = ?";
    private final String COUNT_OTP_SQL = "SELECT COUNT(*) FROM otps WHERE email = ?";
    private final String ADD_OTP_SQL = "INSERT INTO OTPS(email, otp) VALUES(?, ?)";
    private final String UPDATE_OTP_SQL = "UPDATE otps SET otp = ? WHERE email = ?";
    private final String DELETE_OTP_SQL = "DELETE FROM otps WHERE email = ?";

    public OtpResponse sendOtp(String email) {
        OtpResponse otpResponse = new OtpResponse();
        if (StringUtilities.anyNullOrBlank(email)) {
            otpResponse.setStatus(Common.Status.INVALID);
            return otpResponse;
        }
        String otp = RandomUtilities.generateOtp();
        MailResponse mailResponse = mailServices.sendEmail(email, OTP_EMAIL_SUBJECT, OTP_EMAIL_BODY + otp);
        if (mailResponse.getStatus() != Common.Status.OK) {
            otpResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
            return otpResponse;
        }
        try (Connection connection = ConnectionController.getConnection()) {
            Boolean otpExists = isOtpExists(connection, email);
            if (otpExists == null || (otpExists ? updateOtp(connection, email, otp) : addOtp(connection, email, otp))) {
                otpResponse.setStatus(Common.Status.OK);
            } else {
                otpResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException ex) {
            System.err.println("Error establishing database connection: " + ex.getMessage());
            otpResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
        }
        return otpResponse;
    }

    public OtpResponse verifyOtp(String email, String otp) {
        OtpResponse otpResponse = new OtpResponse();
        if (StringUtilities.anyNullOrBlank(email, otp)) {
            otpResponse.setStatus(Common.Status.INVALID);
            return otpResponse;
        }
        OtpResponse getOtpResponse = getOtp(email, otp);
        if (getOtpResponse.getStatus() == Common.Status.OK) {
            deleteOtp(email);
        }
        return getOtpResponse;
    }

    public boolean addOtp(Connection connection, String email, String otp) {
        return executeUpdate(connection, ADD_OTP_SQL, email, otp);
    }

    public boolean updateOtp(Connection connection, String email, String otp) {
        return executeUpdate(connection, UPDATE_OTP_SQL, otp, email);
    }

    public OtpResponse deleteOtp(String email) {
        OtpResponse otpResponse = new OtpResponse();
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(DELETE_OTP_SQL)) {
            preparedStatement.setString(1, email);
            int update = preparedStatement.executeUpdate();
            otpResponse.setStatus(update > 0 ? Common.Status.OK : Common.Status.NOT_FOUND);
        } catch (SQLException ex) {
            System.err.println("Error establishing database connection: " + ex.getMessage());
            otpResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
        }
        return otpResponse;
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

    private OtpResponse getOtp(String email, String otp) {
        OtpResponse otpResponse = new OtpResponse();
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(GET_OTP_SQL)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, otp);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                otpResponse.setStatus(resultSet.next() ? Common.Status.OK : Common.Status.INVALID);
            }
        } catch (SQLException ex) {
            System.err.println("Error establishing database connection: " + ex.getMessage());
            otpResponse.setStatus(Common.Status.INTERNAL_SERVER_ERROR);
        }
        return otpResponse;
    }
}
