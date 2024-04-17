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
@Table(name = "TRANSACTIONTYPES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transactiontypes.findAll", query = "SELECT t FROM Transactiontypes t"),
    @NamedQuery(name = "Transactiontypes.findByTransactiontypeid", query = "SELECT t FROM Transactiontypes t WHERE t.transactiontypeid = :transactiontypeid"),
    @NamedQuery(name = "Transactiontypes.findByTransactiontypename", query = "SELECT t FROM Transactiontypes t WHERE t.transactiontypename = :transactiontypename")})
public class Transactiontypes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "TRANSACTIONTYPEID")
    private Integer transactiontypeid;
    @Size(max = 50)
    @Column(name = "TRANSACTIONTYPENAME")
    private String transactiontypename;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transactiontypeid", fetch = FetchType.LAZY)
    private List<Transactions> transactionsList;

    public Transactiontypes() {
    }

    public Transactiontypes(Integer transactiontypeid) {
        this.transactiontypeid = transactiontypeid;
    }

    public Integer getTransactiontypeid() {
        return transactiontypeid;
    }

    public void setTransactiontypeid(Integer transactiontypeid) {
        this.transactiontypeid = transactiontypeid;
    }

    public String getTransactiontypename() {
        return transactiontypename;
    }

    public void setTransactiontypename(String transactiontypename) {
        this.transactiontypename = transactiontypename;
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
        hash += (transactiontypeid != null ? transactiontypeid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transactiontypes)) {
            return false;
        }
        Transactiontypes other = (Transactiontypes) object;
        if ((this.transactiontypeid == null && other.transactiontypeid != null) || (this.transactiontypeid != null && !this.transactiontypeid.equals(other.transactiontypeid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Transactiontypes[ transactiontypeid=" + transactiontypeid + " ]";
    }
}
