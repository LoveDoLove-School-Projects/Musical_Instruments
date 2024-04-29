package entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "STAFFS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Staffs.findAll", query = "SELECT s FROM Staffs s"),
    @NamedQuery(name = "Staffs.findByUserId", query = "SELECT s FROM Staffs s WHERE s.userId = :userId"),
    @NamedQuery(name = "Staffs.findByUsername", query = "SELECT s FROM Staffs s WHERE s.username = :username"),
    @NamedQuery(name = "Staffs.findByPassword", query = "SELECT s FROM Staffs s WHERE s.password = :password"),
    @NamedQuery(name = "Staffs.findByEmail", query = "SELECT s FROM Staffs s WHERE s.email = :email"),
    @NamedQuery(name = "Staffs.findByAddress", query = "SELECT s FROM Staffs s WHERE s.address = :address"),
    @NamedQuery(name = "Staffs.findByPhoneNumber", query = "SELECT s FROM Staffs s WHERE s.phoneNumber = :phoneNumber"),
    @NamedQuery(name = "Staffs.findByGender", query = "SELECT s FROM Staffs s WHERE s.gender = :gender"),
    @NamedQuery(name = "Staffs.findByTwoFactorAuth", query = "SELECT s FROM Staffs s WHERE s.twoFactorAuth = :twoFactorAuth"),
    @NamedQuery(name = "Staffs.findByAccountCreationDate", query = "SELECT s FROM Staffs s WHERE s.accountCreationDate = :accountCreationDate"),
    @NamedQuery(name = "Staffs.findByEmailAndPassword", query = "SELECT s FROM Staffs s WHERE s.email = :email AND s.password = :password")})
public class Staffs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "USER_ID")
    private Integer userId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "USERNAME")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "PASSWORD")
    private String password;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "EMAIL")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ADDRESS")
    private String address;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "GENDER")
    private String gender;
    @Lob
    @Column(name = "PICTURE")
    private byte[] picture;
    @Column(name = "TWO_FACTOR_AUTH")
    private Boolean twoFactorAuth;
    @Column(name = "ACCOUNT_CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date accountCreationDate;

    public Staffs() {
    }

    public Staffs(Integer userId) {
        this.userId = userId;
    }

    public Staffs(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Staffs(Integer userId, byte[] picture) {
        this.userId = userId;
        this.picture = picture;
    }

    public Staffs(Integer userId, String username, String password, String email, String address, String phoneNumber, String gender) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public Staffs(Integer userId, String username, String address, String phoneNumber, String gender, Boolean twoFactorAuth) {
        this.userId = userId;
        this.username = username;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.twoFactorAuth = twoFactorAuth;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Boolean getTwoFactorAuth() {
        return twoFactorAuth;
    }

    public void setTwoFactorAuth(Boolean twoFactorAuth) {
        this.twoFactorAuth = twoFactorAuth;
    }

    public Date getAccountCreationDate() {
        return accountCreationDate;
    }

    public void setAccountCreationDate(Date accountCreationDate) {
        this.accountCreationDate = accountCreationDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Staffs)) {
            return false;
        }
        Staffs other = (Staffs) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Staffs{");
        sb.append("userId=").append(userId);
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", email=").append(email);
        sb.append(", address=").append(address);
        sb.append(", phoneNumber=").append(phoneNumber);
        sb.append(", gender=").append(gender);
        sb.append(", picture=").append(picture);
        sb.append(", twoFactorAuth=").append(twoFactorAuth);
        sb.append(", accountCreationDate=").append(accountCreationDate);
        sb.append('}');
        return sb.toString();
    }
}
