package services;

import controllers.ConnectionController;
import domain.common.Common;
import domain.request.MailRequest;
import exceptions.DatabaseAccessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public Common.Status sendOtp(String email) {
        if (StringUtilities.anyNullOrBlank(email)) {
            return Common.Status.INVALID;
        }
        String otp = RandomUtilities.generateOtp();
        MailRequest mailRequest = new MailRequest(email, OTP_EMAIL_SUBJECT, OTP_EMAIL_BODY + otp);
        Common.Status mailStatus = mailServices.sendEmail(mailRequest);
        if (mailStatus != Common.Status.OK) {
            return mailStatus;
        }
        boolean otpExists = isOtpExists(email);
        if (otpExists ? updateOtp(email, otp) : addOtp(email, otp)) {
            return Common.Status.OK;
        } else {
            return Common.Status.INTERNAL_SERVER_ERROR;
        }
    }

    public Common.Status verifyOtp(String email, String otp) {
        if (StringUtilities.anyNullOrBlank(email, otp)) {
            return Common.Status.INVALID;
        }
        Common.Status getOtpStatus = getOtp(email, otp);
        if (getOtpStatus == Common.Status.OK) {
            return deleteOtp(email);
        }
        return getOtpStatus;
    }

    public boolean addOtp(String email, String otp) {
        return executeUpdate(ADD_OTP_SQL, email, otp);
    }

    public boolean updateOtp(String email, String otp) {
        return executeUpdate(UPDATE_OTP_SQL, otp, email);
    }

    public Common.Status deleteOtp(String email) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(DELETE_OTP_SQL)) {
            preparedStatement.setString(1, email);
            int update = preparedStatement.executeUpdate();
            return update > 0 ? Common.Status.OK : Common.Status.NOT_FOUND;
        } catch (SQLException ex) {
            throw new DatabaseAccessException("Database error while deleting OTP", ex);
        }
    }

    private boolean isOtpExists(String email) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(COUNT_OTP_SQL)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt(1) > 0 : null;
            }
        } catch (SQLException ex) {
            throw new DatabaseAccessException("Database error while checking OTP existence", ex);
        }
    }

    private boolean executeUpdate(String sql, String param1, String param2) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, param1);
            preparedStatement.setString(2, param2);
            int update = preparedStatement.executeUpdate();
            return update > 0;
        } catch (SQLException ex) {
            throw new DatabaseAccessException("Database error while adding OTP", ex);
        }
    }

    private Common.Status getOtp(String email, String otp) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(GET_OTP_SQL)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, otp);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? Common.Status.OK : Common.Status.INVALID;
            }
        } catch (SQLException ex) {
            throw new DatabaseAccessException("Database error while getting OTP", ex);
        }
    }
}
