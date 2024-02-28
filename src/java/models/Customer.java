package models;

import java.sql.Timestamp;
import utilities.AesUtilities;

public class Customer {
    
    private int customerId;
    private String username;
    private String password;
    private String email;
    private String address;
    private String phoneNumber;
    private String gender;
    private Timestamp accountCreationDate;
    private Timestamp lastLoginDate;
    
    public Customer() {
    }
    
    public int getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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
        this.password = AesUtilities.aes256EcbEncrypt(password);
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
    
    public Timestamp getAccountCreationDate() {
        return accountCreationDate;
    }
    
    public void setAccountCreationDate(Timestamp accountCreationDate) {
        this.accountCreationDate = accountCreationDate;
    }
    
    public Timestamp getLastLoginDate() {
        return lastLoginDate;
    }
    
    public void setLastLoginDate(Timestamp lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
    
}
