package domain.models;

import java.sql.Timestamp;

public class Users {

    protected int id;
    protected String username;
    protected String password;
    protected String email;
    protected String address;
    protected String phoneNumber;
    protected String gender;
    protected byte[] picture;
    protected boolean two_factor_auth;
    protected Timestamp accountCreationDate;

    public Users() {
    }

    public Users(int id, String email, boolean two_factor_auth) {
        this.id = id;
        this.email = email;
        this.two_factor_auth = two_factor_auth;
    }

    public Users(String username, String password, String email, String address, String phoneNumber, String gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public Users(String username, String password, String email, String address, String phoneNumber, String gender, boolean two_factor_auth) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.two_factor_auth = two_factor_auth;
    }

    public Users(int id, String username, String email, String address, String phoneNumber, String gender, byte[] picture, boolean two_factor_auth, Timestamp accountCreationDate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.picture = picture;
        this.two_factor_auth = two_factor_auth;
        this.accountCreationDate = accountCreationDate;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public boolean getTwo_factor_auth() {
        return two_factor_auth;
    }

    public void setTwo_factor_auth(boolean two_factor_auth) {
        this.two_factor_auth = two_factor_auth;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Timestamp getAccountCreationDate() {
        return accountCreationDate;
    }

    public void setAccountCreationDate(Timestamp accountCreationDate) {
        this.accountCreationDate = accountCreationDate;
    }
}
