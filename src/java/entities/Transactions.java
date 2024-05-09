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
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "TRANSACTIONS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transactions.findAll", query = "SELECT t FROM Transactions t"),
    @NamedQuery(name = "Transactions.findByPkid", query = "SELECT t FROM Transactions t WHERE t.pkid = :pkid"),
    @NamedQuery(name = "Transactions.findByUserId", query = "SELECT t FROM Transactions t WHERE t.userId = :userId"),
    @NamedQuery(name = "Transactions.findByTransactionNumber", query = "SELECT t FROM Transactions t WHERE t.transactionNumber = :transactionNumber"),
    @NamedQuery(name = "Transactions.findByOrderNumber", query = "SELECT t FROM Transactions t WHERE t.orderNumber = :orderNumber"),
    @NamedQuery(name = "Transactions.findByTransactionStatus", query = "SELECT t FROM Transactions t WHERE t.transactionStatus = :transactionStatus"),
    @NamedQuery(name = "Transactions.findByPaymentMethod", query = "SELECT t FROM Transactions t WHERE t.paymentMethod = :paymentMethod"),
    @NamedQuery(name = "Transactions.findByCurrency", query = "SELECT t FROM Transactions t WHERE t.currency = :currency"),
    @NamedQuery(name = "Transactions.findByTotalAmount", query = "SELECT t FROM Transactions t WHERE t.totalAmount = :totalAmount"),
    @NamedQuery(name = "Transactions.findByDateCreatedGmt", query = "SELECT t FROM Transactions t WHERE t.dateCreatedGmt = :dateCreatedGmt"),
    @NamedQuery(name = "Transactions.findByDateUpdatedGmt", query = "SELECT t FROM Transactions t WHERE t.dateUpdatedGmt = :dateUpdatedGmt"),
    @NamedQuery(name = "Transactions.findByTransactionNumberAndUserId", query = "SELECT t FROM Transactions t WHERE t.transactionNumber = :transactionNumber AND t.userId = :userId"),
    @NamedQuery(name = "Transactions.findAllByTransactionStatusApproved", query = "SELECT t FROM Transactions t WHERE t.transactionStatus = 'approved'"),})
public class Transactions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PKID")
    private Integer pkid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "USER_ID")
    private int userId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "TRANSACTION_NUMBER")
    private String transactionNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ORDER_NUMBER")
    private String orderNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "TRANSACTION_STATUS")
    private String transactionStatus;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "PAYMENT_METHOD")
    private String paymentMethod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "CURRENCY")
    private String currency;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_AMOUNT")
    private BigDecimal totalAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_CREATED_GMT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreatedGmt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_UPDATED_GMT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdatedGmt;

    public Transactions() {
    }

    public Transactions(Integer pkid) {
        this.pkid = pkid;
    }

    public Transactions(Integer pkid, int userId, String transactionNumber, String orderNumber, String transactionStatus, String paymentMethod, String currency, BigDecimal totalAmount, Date dateCreatedGmt, Date dateUpdatedGmt) {
        this.pkid = pkid;
        this.userId = userId;
        this.transactionNumber = transactionNumber;
        this.orderNumber = orderNumber;
        this.transactionStatus = transactionStatus;
        this.paymentMethod = paymentMethod;
        this.currency = currency;
        this.totalAmount = totalAmount;
        this.dateCreatedGmt = dateCreatedGmt;
        this.dateUpdatedGmt = dateUpdatedGmt;
    }

    public Integer getPkid() {
        return pkid;
    }

    public void setPkid(Integer pkid) {
        this.pkid = pkid;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getDateCreatedGmt() {
        return dateCreatedGmt;
    }

    public void setDateCreatedGmt(Date dateCreatedGmt) {
        this.dateCreatedGmt = dateCreatedGmt;
    }

    public Date getDateUpdatedGmt() {
        return dateUpdatedGmt;
    }

    public void setDateUpdatedGmt(Date dateUpdatedGmt) {
        this.dateUpdatedGmt = dateUpdatedGmt;
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
        if (!(object instanceof Transactions)) {
            return false;
        }
        Transactions other = (Transactions) object;
        if ((this.pkid == null && other.pkid != null) || (this.pkid != null && !this.pkid.equals(other.pkid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Transactions[ pkid=" + pkid + " ]";
    }
}
