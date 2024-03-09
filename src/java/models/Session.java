package models;

public class Session {

    private boolean result;
    private int login_id;

    public Session() {
    }

    public Session(boolean result, int login_id) {
        this.result = result;
        this.login_id = login_id;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getLogin_id() {
        return login_id;
    }

    public void setLogin_id(int login_id) {
        this.login_id = login_id;
    }

}
