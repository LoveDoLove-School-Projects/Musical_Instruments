package domain.response;

public class LoginResponse extends DefaultResponse {

    private int login_id;

    private String email;

    private boolean two_factor_auth;

    public LoginResponse() {
    }

    public LoginResponse(int login_id) {
        this.login_id = login_id;
    }

    public LoginResponse(int login_id, String email) {
        this.login_id = login_id;
        this.email = email;
    }

    public LoginResponse(int login_id, String email, boolean two_factor_auth) {
        this.login_id = login_id;
        this.email = email;
        this.two_factor_auth = two_factor_auth;
    }

    public int getLogin_id() {
        return login_id;
    }

    public void setLogin_id(int login_id) {
        this.login_id = login_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isTwo_factor_auth() {
        return two_factor_auth;
    }

    public void setTwo_factor_auth(boolean two_factor_auth) {
        this.two_factor_auth = two_factor_auth;
    }

}
