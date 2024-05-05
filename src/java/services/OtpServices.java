package services;

import dao.OtpDao;
import static dao.OtpDao.generateOtp;
import entities.Otps;
import entities.OtpsType;
import java.sql.Timestamp;
import utilities.MailSender;
import utilities.StringUtilities;

public class OtpServices {

    private static final String SUBJECT = "OTP";
    private static final String CONTENT = "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width,initial-scale=1'><title>Email OTP Design</title><style>body{font-family:Arial,sans-serif}.container{width:100%;max-width:600px;margin:0 auto}.card{border:1px solid #ddd;border-radius:5px;margin-top:50px;padding:20px;text-align:center}.card-title{font-size:24px;margin-bottom:20px}.card-text{font-size:18px;margin-bottom:20px}.otp{font-size:24px;font-weight:700}</style></head><body><div class='container'><div class='card'><h5 class='card-title'>OTP Verification</h5><p class='card-text'>Your One-Time Password (OTP) is:<br><span class='otp'>${otpvalue}</span></p></div></div></body></html>";
    private final OtpDao otpDao = new OtpDao();

    public boolean sendOtp(String email) {
        if (StringUtilities.anyNullOrBlank(email)) {
            return false;
        }
        String otp = generateOtp();
        boolean mailStatus = MailSender.sendEmail(email, SUBJECT, CONTENT.replace("${otpvalue}", otp));
        if (!mailStatus) {
            return false;
        }
        boolean otpExists = otpDao.isOtpExists(email);
        return otpExists ? otpDao.updateOtp(email, otp) : otpDao.addOtp(email, otp);
    }

    public OtpsType verifyOtp(String email, String otp) {
        if (StringUtilities.anyNullOrBlank(email, otp)) {
            return OtpsType.INVALID;
        }
        Otps dbOtp = otpDao.getOtp(email);
        if (dbOtp == null) {
            return OtpsType.NOT_FOUND;
        }
        if (dbOtp.getTryCount() >= 5) {
            return OtpsType.UNAUTHORIZED;
        }
        if (dbOtp.getCreatedAt().before(new Timestamp(System.currentTimeMillis() - 300000))) {
            return OtpsType.EXPIRED;
        }
        if (!dbOtp.getOtp().equals(otp)) {
            int tryCount = dbOtp.getTryCount() + 1;
            otpDao.updateTryCount(email, tryCount);
            return OtpsType.FAILED;
        }
        if (otp.equals(dbOtp.getOtp())) {
            otpDao.deleteOtp(email);
            return OtpsType.OK;
        }
        return OtpsType.INVALID;
    }
}
