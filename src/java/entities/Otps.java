package entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "OTPS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Otps.findAll", query = "SELECT o FROM Otps o"),
    @NamedQuery(name = "Otps.findByOtp", query = "SELECT o FROM Otps o WHERE o.otp = :otp"),
    @NamedQuery(name = "Otps.findByEmail", query = "SELECT o FROM Otps o WHERE o.email = :email"),
    @NamedQuery(name = "Otps.findByCreatedAt", query = "SELECT o FROM Otps o WHERE o.createdAt = :createdAt"),
    @NamedQuery(name = "Otps.findByTryCount", query = "SELECT o FROM Otps o WHERE o.tryCount = :tryCount")})
public class Otps implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "OTP")
    private String otp;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "TRY_COUNT")
    private Integer tryCount;

    public Otps() {
    }

    public Otps(String otp) {
        this.otp = otp;
    }

    public Otps(String otp, String email) {
        this.otp = otp;
        this.email = email;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getTryCount() {
        return tryCount;
    }

    public void setTryCount(Integer tryCount) {
        this.tryCount = tryCount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otp != null ? otp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Otps)) {
            return false;
        }
        Otps other = (Otps) object;
        if ((this.otp == null && other.otp != null) || (this.otp != null && !this.otp.equals(other.otp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Otps[ otp=" + otp + " ]";
    }
}
