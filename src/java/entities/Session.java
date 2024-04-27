package entities;

public class Session {

    private boolean result;
    private int userId;
    private String email;

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
}
