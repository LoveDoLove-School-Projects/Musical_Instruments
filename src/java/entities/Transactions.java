/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

/**
 *
 * @author LoveDoLove
 */
@Entity
@Table(name = "TRANSACTIONS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transactions.findAll", query = "SELECT t FROM Transactions t"),
    @NamedQuery(name = "Transactions.findById", query = "SELECT t FROM Transactions t WHERE t.id = :id"),
    @NamedQuery(name = "Transactions.findByStatus", query = "SELECT t FROM Transactions t WHERE t.status = :status"),
    @NamedQuery(name = "Transactions.findByCurrency", query = "SELECT t FROM Transactions t WHERE t.currency = :currency"),
    @NamedQuery(name = "Transactions.findByTotalAmount", query = "SELECT t FROM Transactions t WHERE t.totalAmount = :totalAmount"),
    @NamedQuery(name = "Transactions.findByCustomerId", query = "SELECT t FROM Transactions t WHERE t.customerId = :customerId"),
    @NamedQuery(name = "Transactions.findByDateCreatedGmt", query = "SELECT t FROM Transactions t WHERE t.dateCreatedGmt = :dateCreatedGmt"),
    @NamedQuery(name = "Transactions.findByDateUpdatedGmt", query = "SELECT t FROM Transactions t WHERE t.dateUpdatedGmt = :dateUpdatedGmt"),
    @NamedQuery(name = "Transactions.findByParentOrderId", query = "SELECT t FROM Transactions t WHERE t.parentOrderId = :parentOrderId"),
    @NamedQuery(name = "Transactions.findByPaymentMethod", query = "SELECT t FROM Transactions t WHERE t.paymentMethod = :paymentMethod"),
    @NamedQuery(name = "Transactions.findByTransactionId", query = "SELECT t FROM Transactions t WHERE t.transactionId = :transactionId"),
    @NamedQuery(name = "Transactions.findByIpAddress", query = "SELECT t FROM Transactions t WHERE t.ipAddress = :ipAddress"),
    @NamedQuery(name = "Transactions.findByUserAgent", query = "SELECT t FROM Transactions t WHERE t.userAgent = :userAgent"),
    @NamedQuery(name = "Transactions.findByCustomerNote", query = "SELECT t FROM Transactions t WHERE t.customerNote = :customerNote")})
public class Transactions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private int status;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "CURRENCY")
    private String currency;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_AMOUNT")
    private BigDecimal totalAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CUSTOMER_ID")
    private int customerId;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "PARENT_ORDER_ID")
    private int parentOrderId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "PAYMENT_METHOD")
    private String paymentMethod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "TRANSACTION_ID")
    private String transactionId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "IP_ADDRESS")
    private String ipAddress;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "USER_AGENT")
    private String userAgent;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "CUSTOMER_NOTE")
    private String customerNote;

    public Transactions() {
    }

    public Transactions(Integer id) {
        this.id = id;
    }

    public Transactions(Integer id, int status, String currency, BigDecimal totalAmount, int customerId, Date dateCreatedGmt, Date dateUpdatedGmt, int parentOrderId, String paymentMethod, String transactionId, String ipAddress, String userAgent, String customerNote) {
        this.id = id;
        this.status = status;
        this.currency = currency;
        this.totalAmount = totalAmount;
        this.customerId = customerId;
        this.dateCreatedGmt = dateCreatedGmt;
        this.dateUpdatedGmt = dateUpdatedGmt;
        this.parentOrderId = parentOrderId;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.customerNote = customerNote;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

    public int getParentOrderId() {
        return parentOrderId;
    }

    public void setParentOrderId(int parentOrderId) {
        this.parentOrderId = parentOrderId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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

    public String getCustomerNote() {
        return customerNote;
    }

    public void setCustomerNote(String customerNote) {
        this.customerNote = customerNote;
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
        if (!(object instanceof Transactions)) {
            return false;
        }
        Transactions other = (Transactions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ms.paymentgateway.entities.Transactions[ id=" + id + " ]";
    }
}
