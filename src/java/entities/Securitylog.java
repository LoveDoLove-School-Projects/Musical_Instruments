/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

/**
 *
 * @author LoveDoLove
 */
@Entity
@Table(name = "SECURITYLOG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Securitylog.findAll", query = "SELECT s FROM Securitylog s"),
    @NamedQuery(name = "Securitylog.findByPkid", query = "SELECT s FROM Securitylog s WHERE s.pkid = :pkid"),
    @NamedQuery(name = "Securitylog.findByAction", query = "SELECT s FROM Securitylog s WHERE s.action = :action"),
    @NamedQuery(name = "Securitylog.findByActionDate", query = "SELECT s FROM Securitylog s WHERE s.actionDate = :actionDate"),
    @NamedQuery(name = "Securitylog.findByIpAddress", query = "SELECT s FROM Securitylog s WHERE s.ipAddress = :ipAddress"),
    @NamedQuery(name = "Securitylog.findByUserAgent", query = "SELECT s FROM Securitylog s WHERE s.userAgent = :userAgent")})
public class Securitylog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PKID")
    private Integer pkid;
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
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Users userId;

    public Securitylog() {
    }

    public Securitylog(Integer pkid) {
        this.pkid = pkid;
    }

    public Securitylog(Integer pkid, String action) {
        this.pkid = pkid;
        this.action = action;
    }

    public Integer getPkid() {
        return pkid;
    }

    public void setPkid(Integer pkid) {
        this.pkid = pkid;
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

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
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
