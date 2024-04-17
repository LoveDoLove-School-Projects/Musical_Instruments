package entities;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "TRANSACTIONSTATUS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transactionstatus.findAll", query = "SELECT t FROM Transactionstatus t"),
    @NamedQuery(name = "Transactionstatus.findByStatusid", query = "SELECT t FROM Transactionstatus t WHERE t.statusid = :statusid"),
    @NamedQuery(name = "Transactionstatus.findByStatusname", query = "SELECT t FROM Transactionstatus t WHERE t.statusname = :statusname")})
public class Transactionstatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUSID")
    private Integer statusid;
    @Size(max = 50)
    @Column(name = "STATUSNAME")
    private String statusname;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "statusid", fetch = FetchType.LAZY)
    private List<Transactions> transactionsList;

    public Transactionstatus() {
    }

    public Transactionstatus(Integer statusid) {
        this.statusid = statusid;
    }

    public Integer getStatusid() {
        return statusid;
    }

    public void setStatusid(Integer statusid) {
        this.statusid = statusid;
    }

    public String getStatusname() {
        return statusname;
    }

    public void setStatusname(String statusname) {
        this.statusname = statusname;
    }

    @XmlTransient
    public List<Transactions> getTransactionsList() {
        return transactionsList;
    }

    public void setTransactionsList(List<Transactions> transactionsList) {
        this.transactionsList = transactionsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (statusid != null ? statusid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transactionstatus)) {
            return false;
        }
        Transactionstatus other = (Transactionstatus) object;
        if ((this.statusid == null && other.statusid != null) || (this.statusid != null && !this.statusid.equals(other.statusid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Transactionstatus[ statusid=" + statusid + " ]";
    }
}
