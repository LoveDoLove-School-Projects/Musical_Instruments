package domain.request;

public class AdminRequest {

    private int login_id;

    public AdminRequest() {
    }

    public AdminRequest(int login_id) {
        this.login_id = login_id;
    }

    public int getLogin_id() {
        return login_id;
    }

    public void setLogin_id(int login_id) {
        this.login_id = login_id;
    }
}
