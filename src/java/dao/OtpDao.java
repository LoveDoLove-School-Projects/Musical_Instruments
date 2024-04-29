package dao;

import controllers.ConnectionController;
import entities.OtpStatus;
import entities.Otps;
import exceptions.DatabaseException;
import features.MailSender;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import utilities.RandomUtilities;
import utilities.StringUtilities;

public class OtpDao {

    private static final String SUBJECT = "OTP";
    private static final String CONTENT = "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width,initial-scale=1'><title>Email OTP Design</title><style>body{font-family:Arial,sans-serif}.container{width:100%;max-width:600px;margin:0 auto}.card{border:1px solid #ddd;border-radius:5px;margin-top:50px;padding:20px;text-align:center}.card-title{font-size:24px;margin-bottom:20px}.card-text{font-size:18px;margin-bottom:20px}.otp{font-size:24px;font-weight:700}</style></head><body><div class='container'><div class='card'><h5 class='card-title'>OTP Verification</h5><p class='card-text'>Your One-Time Password (OTP) is:<br><span class='otp'>${otpvalue}</span></p></div></div></body></html>";
    private static final String GET_OTP_SQL = "SELECT * FROM otps WHERE email = ?";
    private static final String COUNT_OTP_SQL = "SELECT COUNT(*) FROM otps WHERE email = ?";
    private static final String ADD_OTP_SQL = "INSERT INTO OTPS(email, otp) VALUES(?, ?)";
    private static final String UPDATE_OTP_SQL = "UPDATE otps SET otp = ?, try_count = ? WHERE email = ?";
    private static final String DELETE_OTP_SQL = "DELETE FROM otps WHERE email = ?";
    private static final String UPDATE_TRY_COUNT_SQL = "UPDATE otps SET try_count = ? WHERE email = ?";
    private static final MailSender MAIL_SENDER = new MailSender();

    public boolean sendOtp(String email) {
        if (StringUtilities.anyNullOrBlank(email)) {
            return false;
        }
        String otp = RandomUtilities.generateOtp();
        boolean mailStatus = MAIL_SENDER.sendEmail(email, SUBJECT, CONTENT.replace("${otpvalue}", otp));
        if (!mailStatus) {
            return false;
        }
        boolean otpExists = isOtpExists(email);
        return otpExists ? updateOtp(email, otp) : addOtp(email, otp);
    }

    public OtpStatus verifyOtp(String email, String otp) {
        if (StringUtilities.anyNullOrBlank(email, otp)) {
            return OtpStatus.INVALID;
        }
        Otps dbOtp = getOtp(email);
        if (dbOtp == null) {
            return OtpStatus.NOT_FOUND;
        }
        if (dbOtp.getTryCount() >= 5) {
            return OtpStatus.UNAUTHORIZED;
        }
        if (dbOtp.getCreatedAt().before(new Timestamp(System.currentTimeMillis() - 300000))) {
            return OtpStatus.EXPIRED;
        }
        if (!dbOtp.getOtp().equals(otp)) {
            int tryCount = dbOtp.getTryCount() + 1;
            updateTryCount(email, tryCount);
            return OtpStatus.FAILED;
        }
        if (otp.equals(dbOtp.getOtp())) {
            deleteOtp(email);
            return OtpStatus.OK;
        }
        return OtpStatus.INVALID;
    }

    /**
     * Adds an OTP (One-Time Password) to the database for the specified email.
     *
     * @param email the email address to associate with the OTP
     * @param otp the OTP to be added
     * @return true if the OTP is successfully added, false otherwise
     * @throws DatabaseException if there is an error accessing the database
     */
    public boolean addOtp(String email, String otp) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(ADD_OTP_SQL)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, otp);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    /**
     * Updates the OTP (One-Time Password) for a given email address.
     *
     * @param email the email address for which the OTP needs to be updated
     * @param otp the new OTP to be set
     * @return true if the OTP is updated successfully, false otherwise
     * @throws DatabaseException if there is an error updating the OTP in the
     * database
     */
    public boolean updateOtp(String email, String otp) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_OTP_SQL)) {
            preparedStatement.setString(1, otp);
            preparedStatement.setInt(2, 0);
            preparedStatement.setString(3, email);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    /**
     * Deletes the OTP (One-Time Password) associated with the given email.
     *
     * @param email the email for which the OTP needs to be deleted
     * @throws DatabaseException if there is an error accessing the database
     */
    public void deleteOtp(String email) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(DELETE_OTP_SQL)) {
            preparedStatement.setString(1, email);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    /**
     * Updates the try count for a given email in the database.
     *
     * @param email The email for which the try count needs to be updated.
     * @param tryCount The new try count value.
     * @throws DatabaseException if there is an error updating the try count in
     * the database.
     */
    public void updateTryCount(String email, int tryCount) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TRY_COUNT_SQL)) {
            preparedStatement.setInt(1, tryCount);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    /**
     * Checks if an OTP (One-Time Password) exists for the given email.
     *
     * @param email the email for which to check the OTP existence
     * @return true if an OTP exists for the given email, false otherwise
     * @throws DatabaseException if there is an error accessing the database
     */
    private boolean isOtpExists(String email) {
        try (Connection connection = ConnectionController.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(COUNT_OTP_SQL)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt(1) > 0 : null;
            }
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    /**
     * Represents a single OTP (One-Time Password) entry in the database. An OTP
     * consists of a code, associated email, creation timestamp, and try count.
     */
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
            }
            return null;
        } catch (SQLException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }
}
