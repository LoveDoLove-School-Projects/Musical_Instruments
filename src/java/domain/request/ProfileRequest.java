package domain.request;

public class ProfileRequest {

    private int id;
    private String username;
    private String address;
    private String phoneNumber;
    private String gender;
    private byte[] picture;
    private boolean two_factor_auth;

    public ProfileRequest() {
    }

    public ProfileRequest(int id) {
        this.id = id;
    }

    public ProfileRequest(int id, byte[] picture) {
        this.id = id;
        this.picture = picture;
    }

    public ProfileRequest(int id, String username, String address, String phoneNumber, String gender, boolean two_factor_auth) {
        this.id = id;
        this.username = username;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.two_factor_auth = two_factor_auth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public boolean isTwo_factor_auth() {
        return two_factor_auth;
    }

    public void setTwo_factor_auth(boolean two_factor_auth) {
        this.two_factor_auth = two_factor_auth;
    }
}
