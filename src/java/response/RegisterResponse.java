package response;

import common.Common;

public class RegisterResponse {

    private Common.Status status;

    private String otp;

    public RegisterResponse() {
    }

    public RegisterResponse(Common.Status status) {
        this.status = status;
    }

    public RegisterResponse(Common.Status status, String otp) {
        this.status = status;
        this.otp = otp;
    }

    public Common.Status getStatus() {
        return status;
    }

    public void setStatus(Common.Status status) {
        this.status = status;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    @Override
    public String toString() {
        return "{status: " + status.getCode() + ",message: " + status.getMessage() + "}";
    }
}
