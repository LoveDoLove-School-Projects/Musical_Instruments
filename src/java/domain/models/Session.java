package domain.models;

import domain.common.Common;
import domain.common.Common.Role;

public class Session {

    private boolean result;
    private int userId;
    private String email;
    private Common.Role role;

    public Session() {
    }

    public Session(int userId, String email, Role role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public Session(boolean result, int userId, Common.Role role) {
        this.result = result;
        this.userId = userId;
        this.role = role;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Common.Role getRole() {
        return role;
    }

    public void setRole(Common.Role role) {
        this.role = role;
    }
}
