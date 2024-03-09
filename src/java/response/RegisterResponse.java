package response;

import common.Common;

public class RegisterResponse {

    private Common.Status status;

    public RegisterResponse() {
    }

    public RegisterResponse(Common.Status status) {
        this.status = status;
    }

    public Common.Status getStatus() {
        return status;
    }

    public void setStatus(Common.Status status) {
        this.status = status;
    }

}
