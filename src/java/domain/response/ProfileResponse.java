package domain.response;

import domain.common.Common;
import domain.models.Profile;
import java.sql.Timestamp;

public class ProfileResponse extends Profile {

    private Common.Status status;

    public ProfileResponse(Common.Status status) {
        this.status = status;
    }

    public ProfileResponse(Common.Status status, String username, String password, String email, String address, String phoneNumber, String gender) {
        super(username, password, email, address, phoneNumber, gender);
        this.status = status;
    }

    public ProfileResponse(Common.Status status, String username, String password, String email, String address, String phoneNumber, String gender, boolean two_factor_auth) {
        super(username, password, email, address, phoneNumber, gender, two_factor_auth);
        this.status = status;
    }

    public ProfileResponse(Common.Status status, int id, String username, String email, String address, String phoneNumber, String gender, byte[] picture, boolean two_factor_auth, Timestamp accountCreationDate, Timestamp lastLoginDate) {
        super(id, username, email, address, phoneNumber, gender, picture, two_factor_auth, accountCreationDate, lastLoginDate);
        this.status = status;
    }

    public Common.Status getStatus() {
        return status;
    }

    public void setStatus(Common.Status status) {
        this.status = status;
    }
}
