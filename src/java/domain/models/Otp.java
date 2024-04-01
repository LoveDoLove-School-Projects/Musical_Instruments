package domain.models;

import java.sql.Timestamp;

public class Otp {

    private String otp;
    private String email;
    private Timestamp created_at;
    private int try_count;

    public Otp() {
    }

    public Otp(String otp, String email, Timestamp created_at, int try_count) {
        this.otp = otp;
        this.email = email;
        this.created_at = created_at;
        this.try_count = try_count;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public int getTry_count() {
        return try_count;
    }

    public void setTry_count(int try_count) {
        this.try_count = try_count;
    }
}
