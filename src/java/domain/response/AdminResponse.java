package domain.response;

public class AdminResponse extends DefaultResponse {

    private String username;

    public AdminResponse() {
    }

    public AdminResponse(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
