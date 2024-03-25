package domain.response;

public class LoginResponse extends DefaultResponse {

    private int login_id;

    private String email;

    public LoginResponse() {
    }

    public LoginResponse(int login_id) {
        this.login_id = login_id;
    }

    public LoginResponse(int login_id, String email) {
        this.login_id = login_id;
        this.email = email;
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

}
