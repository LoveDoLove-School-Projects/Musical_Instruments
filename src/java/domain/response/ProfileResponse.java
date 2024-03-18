package domain.response;

import domain.models.Profile;

public class ProfileResponse extends DefaultResponse {

    private Profile profile;

    public ProfileResponse() {
    }

    public ProfileResponse(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

}
