package entities;

public class Session {

    private boolean result;
    private int userId;
    private String email;
    private Role role;

    public Session() {
    }

    public Session(int userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public Session(boolean result, int userId) {
        this.result = result;
        this.userId = userId;
    }

    public Session(int userId, String email, Role role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public Session(boolean result, int userId, Role role) {
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Session{");
        sb.append("result=").append(result);
        sb.append(", userId=").append(userId);
        sb.append(", email=").append(email);
        sb.append(", role=").append(role);
        sb.append('}');
        return sb.toString();
    }
}
