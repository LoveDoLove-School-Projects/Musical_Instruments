package entities;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "BANKUSERS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bankusers.findAll", query = "SELECT b FROM Bankusers b"),
    @NamedQuery(name = "Bankusers.findByUserid", query = "SELECT b FROM Bankusers b WHERE b.userid = :userid"),
    @NamedQuery(name = "Bankusers.findByFirstname", query = "SELECT b FROM Bankusers b WHERE b.firstname = :firstname"),
    @NamedQuery(name = "Bankusers.findByLastname", query = "SELECT b FROM Bankusers b WHERE b.lastname = :lastname"),
    @NamedQuery(name = "Bankusers.findByEmail", query = "SELECT b FROM Bankusers b WHERE b.email = :email"),
    @NamedQuery(name = "Bankusers.findByPassword", query = "SELECT b FROM Bankusers b WHERE b.password = :password")})
public class Bankusers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "USERID")
    private Integer userid;
    @Size(max = 100)
    @Column(name = "FIRSTNAME")
    private String firstname;
    @Size(max = 100)
    @Column(name = "LASTNAME")
    private String lastname;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 255)
    @Column(name = "PASSWORD")
    private String password;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userid", fetch = FetchType.LAZY)
    private List<Bankaccounts> bankaccountsList;

    public Bankusers() {
    }

    public Bankusers(Integer userid) {
        this.userid = userid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @XmlTransient
    public List<Bankaccounts> getBankaccountsList() {
        return bankaccountsList;
    }

    public void setBankaccountsList(List<Bankaccounts> bankaccountsList) {
        this.bankaccountsList = bankaccountsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userid != null ? userid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bankusers)) {
            return false;
        }
        Bankusers other = (Bankusers) object;
        if ((this.userid == null && other.userid != null) || (this.userid != null && !this.userid.equals(other.userid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Bankusers[ userid=" + userid + " ]";
    }
}
