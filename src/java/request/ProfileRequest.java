package request;

import java.io.InputStream;

public class ProfileRequest {

    private int login_id;

    private InputStream picture;

    public ProfileRequest() {
    }

    public ProfileRequest(int login_id) {
        this.login_id = login_id;
    }

    public int getLogin_id() {
        return login_id;
    }

    public void setLogin_id(int login_id) {
        this.login_id = login_id;
    }

    public InputStream getPicture() {
        return picture;
    }

    public void setPicture(InputStream picture) {
        this.picture = picture;
    }
}
