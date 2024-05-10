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
@Table(name = "CUSTOMERS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Customers.findAll", query = "SELECT c FROM Customers c"),
    @NamedQuery(name = "Customers.findByUserId", query = "SELECT c FROM Customers c WHERE c.userId = :userId"),
    @NamedQuery(name = "Customers.findByUsername", query = "SELECT c FROM Customers c WHERE c.username = :username"),
    @NamedQuery(name = "Customers.findByPassword", query = "SELECT c FROM Customers c WHERE c.password = :password"),
    @NamedQuery(name = "Customers.findByEmail", query = "SELECT c FROM Customers c WHERE c.email = :email"),
    @NamedQuery(name = "Customers.findByAddress", query = "SELECT c FROM Customers c WHERE c.address = :address"),
    @NamedQuery(name = "Customers.findByPhoneNumber", query = "SELECT c FROM Customers c WHERE c.phoneNumber = :phoneNumber"),
    @NamedQuery(name = "Customers.findByGender", query = "SELECT c FROM Customers c WHERE c.gender = :gender"),
    @NamedQuery(name = "Customers.findByTwoFactorAuth", query = "SELECT c FROM Customers c WHERE c.twoFactorAuth = :twoFactorAuth"),
    @NamedQuery(name = "Customers.findByFirstName", query = "SELECT c FROM Customers c WHERE c.firstName = :firstName"),
    @NamedQuery(name = "Customers.findByLastName", query = "SELECT c FROM Customers c WHERE c.lastName = :lastName"),
    @NamedQuery(name = "Customers.findByCountry", query = "SELECT c FROM Customers c WHERE c.country = :country"),
    @NamedQuery(name = "Customers.findByCity", query = "SELECT c FROM Customers c WHERE c.city = :city"),
    @NamedQuery(name = "Customers.findByState", query = "SELECT c FROM Customers c WHERE c.state = :state"),
    @NamedQuery(name = "Customers.findByZipCode", query = "SELECT c FROM Customers c WHERE c.zipCode = :zipCode"),
    @NamedQuery(name = "Customers.findByAccountCreationDate", query = "SELECT c FROM Customers c WHERE c.accountCreationDate = :accountCreationDate"),
    @NamedQuery(name = "Customers.findByEmailAndPassword", query = "SELECT c FROM Customers c WHERE c.email = :email AND c.password = :password")})
public class Customers implements Serializable {

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
    @Size(max = 100)
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Size(max = 100)
    @Column(name = "LAST_NAME")
    private String lastName;
    @Size(max = 100)
    @Column(name = "COUNTRY")
    private String country;
    @Size(max = 100)
    @Column(name = "CITY")
    private String city;
    @Size(max = 100)
    @Column(name = "STATE")
    private String state;
    @Size(max = 10)
    @Column(name = "ZIP_CODE")
    private String zipCode;
    @Column(name = "ACCOUNT_CREATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date accountCreationDate;

    public Customers() {
    }

    public Customers(Integer userId) {
        this.userId = userId;
    }

    public Customers(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Customers(Integer userId, byte[] picture) {
        this.userId = userId;
        this.picture = picture;
    }

    public Customers(Integer userId, String username, String address, String phoneNumber, String gender) {
        this.userId = userId;
        this.username = username;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public Customers(Integer userId, String username, String address, String phoneNumber, String gender, Boolean twoFactorAuth) {
        this.userId = userId;
        this.username = username;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.twoFactorAuth = twoFactorAuth;
    }

    public Customers(String username, String password, String email, String address, String phoneNumber, String gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public Customers(Integer userId, String username, String password, String email, String address, String phoneNumber, String gender) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public Customers(Integer userId, String username, String email, String address, String phoneNumber, String gender, Boolean twoFactorAuth, Date accountCreationDate) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.twoFactorAuth = twoFactorAuth;
        this.accountCreationDate = accountCreationDate;
    }

    public Customers(Integer userId, String address, String phoneNumber, String firstName, String lastName, String country, String city, String state, String zipCode) {
        this.userId = userId;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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
        if (!(object instanceof Customers)) {
            return false;
        }
        Customers other = (Customers) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Customers[ userId=" + userId + " ]";
    }
}
