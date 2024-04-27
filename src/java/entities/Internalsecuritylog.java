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
@Table(name = "INTERNALSECURITYLOG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Internalsecuritylog.findAll", query = "SELECT i FROM Internalsecuritylog i"),
    @NamedQuery(name = "Internalsecuritylog.findByPkid", query = "SELECT i FROM Internalsecuritylog i WHERE i.pkid = :pkid"),
    @NamedQuery(name = "Internalsecuritylog.findByUsername", query = "SELECT i FROM Internalsecuritylog i WHERE i.username = :username"),
    @NamedQuery(name = "Internalsecuritylog.findByAction", query = "SELECT i FROM Internalsecuritylog i WHERE i.action = :action"),
    @NamedQuery(name = "Internalsecuritylog.findByActionDate", query = "SELECT i FROM Internalsecuritylog i WHERE i.actionDate = :actionDate"),
    @NamedQuery(name = "Internalsecuritylog.findByIpAddress", query = "SELECT i FROM Internalsecuritylog i WHERE i.ipAddress = :ipAddress"),
    @NamedQuery(name = "Internalsecuritylog.findByUserAgent", query = "SELECT i FROM Internalsecuritylog i WHERE i.userAgent = :userAgent")})
public class Internalsecuritylog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PKID")
    private Integer pkid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "USERNAME")
    private String username;
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

    public Internalsecuritylog() {
    }

    public Internalsecuritylog(Integer pkid) {
        this.pkid = pkid;
    }

    public Internalsecuritylog(Integer pkid, String username, String action) {
        this.pkid = pkid;
        this.username = username;
        this.action = action;
    }

    public Internalsecuritylog(String username, String action, String ipAddress, String userAgent) {
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
        if (!(object instanceof Internalsecuritylog)) {
            return false;
        }
        Internalsecuritylog other = (Internalsecuritylog) object;
        if ((this.pkid == null && other.pkid != null) || (this.pkid != null && !this.pkid.equals(other.pkid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Internalsecuritylog[ pkid=" + pkid + " ]";
    }
}
