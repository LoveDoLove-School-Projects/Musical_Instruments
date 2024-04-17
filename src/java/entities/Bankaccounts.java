package entities;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "BANKACCOUNTS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bankaccounts.findAll", query = "SELECT b FROM Bankaccounts b"),
    @NamedQuery(name = "Bankaccounts.findByAccountid", query = "SELECT b FROM Bankaccounts b WHERE b.accountid = :accountid"),
    @NamedQuery(name = "Bankaccounts.findByCardHolderName", query = "SELECT b FROM Bankaccounts b WHERE b.cardHolderName = :cardHolderName"),
    @NamedQuery(name = "Bankaccounts.findByCardNumber", query = "SELECT b FROM Bankaccounts b WHERE b.cardNumber = :cardNumber"),
    @NamedQuery(name = "Bankaccounts.findByExpiryDate", query = "SELECT b FROM Bankaccounts b WHERE b.expiryDate = :expiryDate"),
    @NamedQuery(name = "Bankaccounts.findByBalance", query = "SELECT b FROM Bankaccounts b WHERE b.balance = :balance"),
    @NamedQuery(name = "Bankaccounts.findByCurrency", query = "SELECT b FROM Bankaccounts b WHERE b.currency = :currency")})
public class Bankaccounts implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ACCOUNTID")
    private Integer accountid;
    @Size(max = 100)
    @Column(name = "CARD_HOLDER_NAME")
    private String cardHolderName;
    @Size(max = 16)
    @Column(name = "CARD_NUMBER")
    private String cardNumber;
    @Column(name = "EXPIRY_DATE")
    @Temporal(TemporalType.DATE)
    private Date expiryDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "BALANCE")
    private BigDecimal balance;
    @Size(max = 3)
    @Column(name = "CURRENCY")
    private String currency;
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Bankusers userid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountid", fetch = FetchType.LAZY)
    private List<Transactions> transactionsList;

    public Bankaccounts() {
    }

    public Bankaccounts(Integer accountid) {
        this.accountid = accountid;
    }

    public Integer getAccountid() {
        return accountid;
    }

    public void setAccountid(Integer accountid) {
        this.accountid = accountid;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Bankusers getUserid() {
        return userid;
    }

    public void setUserid(Bankusers userid) {
        this.userid = userid;
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
        hash += (accountid != null ? accountid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bankaccounts)) {
            return false;
        }
        Bankaccounts other = (Bankaccounts) object;
        if ((this.accountid == null && other.accountid != null) || (this.accountid != null && !this.accountid.equals(other.accountid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Bankaccounts[ accountid=" + accountid + " ]";
    }
}
