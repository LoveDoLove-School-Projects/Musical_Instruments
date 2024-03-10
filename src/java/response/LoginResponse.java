package response;

import common.Common;

public class LoginResponse {

    private Common.Status status;

    private int login_id;

    public LoginResponse() {
    }

    public LoginResponse(Common.Status status, int login_id) {
        this.status = status;
        this.login_id = login_id;
    }

    public Common.Status getStatus() {
        return status;
    }

    public void setStatus(Common.Status status) {
        this.status = status;
    }

    public int getLogin_id() {
        return login_id;
    }

    public void setLogin_id(int login_id) {
        this.login_id = login_id;
    }

}
