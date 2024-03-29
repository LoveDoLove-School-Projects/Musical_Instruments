package domain.response;

import domain.common.Common;

public class AdminResponse {

    private Common.Status status;
    private String username;

    public AdminResponse() {
    }

    public AdminResponse(String username) {
        this.username = username;
    }

    public AdminResponse(Common.Status status) {
        this.status = status;
    }

    public AdminResponse(Common.Status status, String username) {
        this.status = status;
        this.username = username;
    }

    public Common.Status getStatus() {
        return status;
    }

    public void setStatus(Common.Status status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
