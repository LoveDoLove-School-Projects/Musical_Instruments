package response;

public class LoginResponse extends DefaultResponse {

    private int login_id;

    public LoginResponse() {
    }

    public LoginResponse(int login_id) {
        this.login_id = login_id;
    }

    public int getLogin_id() {
        return login_id;
    }

    public void setLogin_id(int login_id) {
        this.login_id = login_id;
    }

}
