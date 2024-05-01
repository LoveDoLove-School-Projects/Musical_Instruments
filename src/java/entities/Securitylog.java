package entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "SECURITYLOG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Securitylog.findAll", query = "SELECT s FROM Securitylog s"),
    @NamedQuery(name = "Securitylog.findByPkid", query = "SELECT s FROM Securitylog s WHERE s.pkid = :pkid"),
    @NamedQuery(name = "Securitylog.findByUserId", query = "SELECT s FROM Securitylog s WHERE s.userId = :userId"),
    @NamedQuery(name = "Securitylog.findByUsername", query = "SELECT s FROM Securitylog s WHERE s.username = :username"),
    @NamedQuery(name = "Securitylog.findByEmail", query = "SELECT s FROM Securitylog s WHERE s.email = :email"),
    @NamedQuery(name = "Securitylog.findByRole", query = "SELECT s FROM Securitylog s WHERE s.role = :role"),
    @NamedQuery(name = "Securitylog.findByAction", query = "SELECT s FROM Securitylog s WHERE s.action = :action"),
    @NamedQuery(name = "Securitylog.findByActionDate", query = "SELECT s FROM Securitylog s WHERE s.actionDate = :actionDate"),
    @NamedQuery(name = "Securitylog.findByIpAddress", query = "SELECT s FROM Securitylog s WHERE s.ipAddress = :ipAddress"),
    @NamedQuery(name = "Securitylog.findByUserAgent", query = "SELECT s FROM Securitylog s WHERE s.userAgent = :userAgent"),
    @NamedQuery(name = "Securitylog.findByUserIdAndRole", query = "SELECT s FROM Securitylog s WHERE s.userId = :userId AND s.role = :role")})
public class Securitylog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PKID")
    private Integer pkid;
    @Column(name = "USER_ID")
    private Integer userId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "USERNAME")
    private String username;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 50)
    @Column(name = "ROLE")
    private String role;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionDate;
    @Size(max = 45)
    @Column(name = "IP_ADDRESS")
    private String ipAddress;
    @Size(max = 255)
    @Column(name = "USER_AGENT")
    private String userAgent;

    public Securitylog() {
    }

    public Securitylog(Integer pkid) {
        this.pkid = pkid;
    }

    public Securitylog(Integer pkid, String username, String action) {
        this.pkid = pkid;
        this.username = username;
        this.action = action;
    }

    public Securitylog(Integer userId, String username, String email, String role, String action, String ipAddress, String userAgent) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
        this.action = action;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }

    public Integer getPkid() {
        return pkid;
    }

    public void setPkid(Integer pkid) {
        this.pkid = pkid;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pkid != null ? pkid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Securitylog)) {
            return false;
        }
        Securitylog other = (Securitylog) object;
        if ((this.pkid == null && other.pkid != null) || (this.pkid != null && !this.pkid.equals(other.pkid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Securitylog[ pkid=" + pkid + " ]";
    }
}
