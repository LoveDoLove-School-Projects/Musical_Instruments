package domain.request;

import java.io.InputStream;

public class ProfileRequest extends DefaultRequest {

    public ProfileRequest() {
    }

    public ProfileRequest(int id) {
        super(id);
    }

    public ProfileRequest(int id, InputStream picture) {
        super(id, picture);
    }

    public ProfileRequest(int id, String username, String address, String phoneNumber, String gender) {
        super(id, username, address, phoneNumber, gender);
    }

}
