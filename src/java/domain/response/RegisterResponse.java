package domain.response;

import domain.common.Common;

public class RegisterResponse extends DefaultResponse {

    private String otp;

    public RegisterResponse() {
    }

    public RegisterResponse(Common.Status status) {
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
