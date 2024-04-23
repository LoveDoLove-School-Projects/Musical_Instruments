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
@Table(name = "RESETPASSWORD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Resetpassword.findAll", query = "SELECT r FROM Resetpassword r"),
    @NamedQuery(name = "Resetpassword.findById", query = "SELECT r FROM Resetpassword r WHERE r.id = :id"),
    @NamedQuery(name = "Resetpassword.findByEmail", query = "SELECT r FROM Resetpassword r WHERE r.email = :email"),
    @NamedQuery(name = "Resetpassword.findByToken", query = "SELECT r FROM Resetpassword r WHERE r.token = :token"),
    @NamedQuery(name = "Resetpassword.findByCreatedAt", query = "SELECT r FROM Resetpassword r WHERE r.createdAt = :createdAt")})
public class Resetpassword implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "EMAIL")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "TOKEN")
    private String token;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Resetpassword() {
    }

    public Resetpassword(Integer id) {
        this.id = id;
    }

    public Resetpassword(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public Resetpassword(Integer id, String email, String token, Date createdAt) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Resetpassword)) {
            return false;
        }
        Resetpassword other = (Resetpassword) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Resetpassword[ id=" + id + " ]";
    }
}
