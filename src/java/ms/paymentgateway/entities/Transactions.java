package ms.paymentgateway.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "TRANSACTIONS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transactions.findAll", query = "SELECT t FROM Transactions t"),
    @NamedQuery(name = "Transactions.findByTransactionid", query = "SELECT t FROM Transactions t WHERE t.transactionid = :transactionid"),
    @NamedQuery(name = "Transactions.findByTransactiondate", query = "SELECT t FROM Transactions t WHERE t.transactiondate = :transactiondate"),
    @NamedQuery(name = "Transactions.findByAmount", query = "SELECT t FROM Transactions t WHERE t.amount = :amount"),
    @NamedQuery(name = "Transactions.findByCurrency", query = "SELECT t FROM Transactions t WHERE t.currency = :currency"),
    @NamedQuery(name = "Transactions.findByDescription", query = "SELECT t FROM Transactions t WHERE t.description = :description")})
public class Transactions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "TRANSACTIONID")
    private String transactionid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TRANSACTIONDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactiondate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "CURRENCY")
    private String currency;
    @Size(max = 255)
    @Column(name = "DESCRIPTION")
    private String description;
    @JoinColumn(name = "ACCOUNTID", referencedColumnName = "ACCOUNTID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Bankaccounts accountid;
    @JoinColumn(name = "STATUSID", referencedColumnName = "STATUSID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Transactionstatus statusid;
    @JoinColumn(name = "TRANSACTIONTYPEID", referencedColumnName = "TRANSACTIONTYPEID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Transactiontypes transactiontypeid;

    public Transactions() {
    }

    public Transactions(String transactionid) {
        this.transactionid = transactionid;
    }

    public Transactions(String transactionid, Date transactiondate, BigDecimal amount, String currency) {
        this.transactionid = transactionid;
        this.transactiondate = transactiondate;
        this.amount = amount;
        this.currency = currency;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public Date getTransactiondate() {
        return transactiondate;
    }

    public void setTransactiondate(Date transactiondate) {
        this.transactiondate = transactiondate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bankaccounts getAccountid() {
        return accountid;
    }

    public void setAccountid(Bankaccounts accountid) {
        this.accountid = accountid;
    }

    public Transactionstatus getStatusid() {
        return statusid;
    }

    public void setStatusid(Transactionstatus statusid) {
        this.statusid = statusid;
    }

    public Transactiontypes getTransactiontypeid() {
        return transactiontypeid;
    }

    public void setTransactiontypeid(Transactiontypes transactiontypeid) {
        this.transactiontypeid = transactiontypeid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transactionid != null ? transactionid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transactions)) {
            return false;
        }
        Transactions other = (Transactions) object;
        if ((this.transactionid == null && other.transactionid != null) || (this.transactionid != null && !this.transactionid.equals(other.transactionid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Transactions[ transactionid=" + transactionid + " ]";
    }
}
