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
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "CARDS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cards.findAll", query = "SELECT c FROM Cards c"),
    @NamedQuery(name = "Cards.findByPkid", query = "SELECT c FROM Cards c WHERE c.pkid = :pkid"),
    @NamedQuery(name = "Cards.findByCardHolderName", query = "SELECT c FROM Cards c WHERE c.cardHolderName = :cardHolderName"),
    @NamedQuery(name = "Cards.findByCardNumber", query = "SELECT c FROM Cards c WHERE c.cardNumber = :cardNumber"),
    @NamedQuery(name = "Cards.findByExpYear", query = "SELECT c FROM Cards c WHERE c.expYear = :expYear"),
    @NamedQuery(name = "Cards.findByExpMonth", query = "SELECT c FROM Cards c WHERE c.expMonth = :expMonth"),
    @NamedQuery(name = "Cards.findByCvv", query = "SELECT c FROM Cards c WHERE c.cvv = :cvv"),
    @NamedQuery(name = "Cards.findByBalance", query = "SELECT c FROM Cards c WHERE c.balance = :balance")})
public class Cards implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PKID")
    private Integer pkid;
    @Size(max = 100)
    @Column(name = "CARD_HOLDER_NAME")
    private String cardHolderName;
    @Size(max = 16)
    @Column(name = "CARD_NUMBER")
    private String cardNumber;
    @Size(max = 4)
    @Column(name = "EXP_YEAR")
    private String expYear;
    @Size(max = 2)
    @Column(name = "EXP_MONTH")
    private String expMonth;
    @Size(max = 3)
    @Column(name = "CVV")
    private String cvv;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "BALANCE")
    private BigDecimal balance;

    public Cards() {
    }

    public Cards(Integer pkid) {
        this.pkid = pkid;
    }

    public Cards(String cardHolderName, String cardNumber, String expYear, String expMonth, String cvv) {
        this.cardHolderName = cardHolderName;
        this.cardNumber = cardNumber;
        this.expYear = expYear;
        this.expMonth = expMonth;
        this.cvv = cvv;
    }

    public Integer getPkid() {
        return pkid;
    }

    public void setPkid(Integer pkid) {
        this.pkid = pkid;
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

    public String getExpYear() {
        return expYear;
    }

    public void setExpYear(String expYear) {
        this.expYear = expYear;
    }

    public String getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(String expMonth) {
        this.expMonth = expMonth;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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
        if (!(object instanceof Cards)) {
            return false;
        }
        Cards other = (Cards) object;
        if ((this.pkid == null && other.pkid != null) || (this.pkid != null && !this.pkid.equals(other.pkid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cards{");
        sb.append("pkid=").append(pkid);
        sb.append(", cardHolderName=").append(cardHolderName);
        sb.append(", cardNumber=").append(cardNumber);
        sb.append(", expYear=").append(expYear);
        sb.append(", expMonth=").append(expMonth);
        sb.append(", cvv=").append(cvv);
        sb.append(", balance=").append(balance);
        sb.append('}');
        return sb.toString();
    }
}
