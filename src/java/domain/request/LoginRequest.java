package domain.request;

public class LoginRequest extends DefaultRequest {


    public LoginRequest() {
    }


    public LoginRequest(String email, String password) {
        super(email, password);
    }

}
