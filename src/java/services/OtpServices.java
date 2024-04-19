package services;

import controllers.ConnectionController;
import domain.common.Common;
import domain.request.MailRequest;
import entities.Otps;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Logger;
import utilities.RandomUtilities;
import utilities.StringUtilities;

public class OtpServices {

    private static final Logger LOG = Logger.getLogger(OtpServices.class.getName());
    private static final String OTP_EMAIL_SUBJECT = "OTP";
    private static final String OTP_EMAIL_BODY = "Your OTP is: ";
    private static final String GET_OTP_SQL = "SELECT * FROM otps WHERE email = ?";
    private static final String COUNT_OTP_SQL = "SELECT COUNT(*) FROM otps WHERE email = ?";
    private static final String ADD_OTP_SQL = "INSERT INTO OTPS(email, otp) VALUES(?, ?)";
    private static final String UPDATE_OTP_SQL = "UPDATE otps SET otp = ?, try_count = ? WHERE email = ?";
    private static final String DELETE_OTP_SQL = "DELETE FROM otps WHERE email = ?";
    private static final String UPDATE_TRY_COUNT_SQL = "UPDATE otps SET try_count = ? WHERE email = ?";
    private static final MailServices MAIL_SERVICES = new MailServices();

    public Common.Status sendOtp(String email) {
        if (StringUtilities.anyNullOrBlank(email)) {
            return Common.Status.INVALID;
        }
        String otp = RandomUtilities.generateOtp();
        MailRequest mailRequest = new MailRequest(email, OTP_EMAIL_SUBJECT, OTP_EMAIL_BODY + otp);
        Common.Status mailStatus = MAIL_SERVICES.sendEmail(mailRequest);
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
        Otps dbOtp = getOtp(email);
        if (dbOtp == null) {
            return Common.Status.NOT_FOUND;
        }
        if (dbOtp.getTryCount() >= 5) {
            return Common.Status.UNAUTHORIZED;
        }
        if (dbOtp.getCreatedAt().before(new Timestamp(System.currentTimeMillis() - 300000))) {
            return Common.Status.EXPIRED;
        }
        if (!dbOtp.getOtp().equals(otp)) {
            int tryCount = dbOtp.getTryCount() + 1;
            updateTryCount(email, tryCount);
            return Common.Status.FAILED;
        }
        if (otp.equals(dbOtp.getOtp())) {
            deleteOtp(email);
            return Common.Status.OK;
        }
        return Common.Status.INVALID;
    }

    public boolean addOtp(String email, String otp) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(ADD_OTP_SQL)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, otp);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            LOG.severe(ex.getMessage());
            return false;
        }
    }

    public boolean updateOtp(String email, String otp) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_OTP_SQL)) {
            preparedStatement.setString(1, otp);
            preparedStatement.setInt(2, 0);
            preparedStatement.setString(3, email);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            LOG.severe(ex.getMessage());
            return false;
        }
    }

    public void deleteOtp(String email) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(DELETE_OTP_SQL)) {
            preparedStatement.setString(1, email);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            LOG.severe(ex.getMessage());
        }
    }

    public void updateTryCount(String email, int tryCount) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TRY_COUNT_SQL)) {
            preparedStatement.setInt(1, tryCount);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            LOG.severe(ex.getMessage());
        }
    }

    private boolean isOtpExists(String email) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(COUNT_OTP_SQL)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt(1) > 0 : null;
            }
        } catch (SQLException ex) {
            LOG.severe(ex.getMessage());
            return false;
        }
    }

    private Otps getOtp(String email) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(GET_OTP_SQL)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String otp = resultSet.getString("otp");
                    Timestamp timestamp = resultSet.getTimestamp("created_at");
                    int tryCount = resultSet.getInt("try_count");
                    return new Otps(otp, email, timestamp, tryCount);
                }
                return null;
            }
        } catch (SQLException ex) {
            LOG.severe(ex.getMessage());
            return null;
        }
    }
}
