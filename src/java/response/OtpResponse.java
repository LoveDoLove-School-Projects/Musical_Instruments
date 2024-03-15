package response;

public class OtpResponse extends DefaultResponse {

    private String otp;

    public OtpResponse() {
    }

    public OtpResponse(String otp) {
        this.otp = otp;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

}
