package request;

public class RegisterRequest extends DefaultRequest {

    private String confirm_password;
    private String otp;

    public RegisterRequest() {
    }

    public RegisterRequest(String username, String password, String confirm_password, String email, String address, String phoneNumber, String gender, String otp) {
        super(username, password, email, address, phoneNumber, gender);
        this.confirm_password = confirm_password;
        this.otp = otp;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

}
