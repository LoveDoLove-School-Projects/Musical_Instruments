package domain.models;

import domain.common.Common;

public class Session {

    private boolean result;
    private int id;
    private Common.Role role;

    public Session() {
    }

    public Session(boolean result, int id, Common.Role role) {
        this.result = result;
        this.id = id;
        this.role = role;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Common.Role getRole() {
        return role;
    }

    public void setRole(Common.Role role) {
        this.role = role;
    }

}
