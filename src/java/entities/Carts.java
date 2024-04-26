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
import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "CARTS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Carts.findAll", query = "SELECT c FROM Carts c"),
    @NamedQuery(name = "Carts.findByCardId", query = "SELECT c FROM Carts c WHERE c.cardId = :cardId"),
    @NamedQuery(name = "Carts.findByCustomerId", query = "SELECT c FROM Carts c WHERE c.customerId = :customerId"),
    @NamedQuery(name = "Carts.findByProductId", query = "SELECT c FROM Carts c WHERE c.productId = :productId"),
    @NamedQuery(name = "Carts.findByProductName", query = "SELECT c FROM Carts c WHERE c.productName = :productName"),
    @NamedQuery(name = "Carts.findByProductQuantity", query = "SELECT c FROM Carts c WHERE c.productQuantity = :productQuantity"),
    @NamedQuery(name = "Carts.findByProductColor", query = "SELECT c FROM Carts c WHERE c.productColor = :productColor"),
    @NamedQuery(name = "Carts.findByProductPrice", query = "SELECT c FROM Carts c WHERE c.productPrice = :productPrice"),
    @NamedQuery(name = "Carts.findByProductImagePath", query = "SELECT c FROM Carts c WHERE c.productImagePath = :productImagePath")})
public class Carts implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CARD_ID")
    private Integer cardId;
    @Basic(optional = false)
    @Column(name = "CUSTOMER_ID")
    private int customerId;
    @Basic(optional = false)
    @Column(name = "PRODUCT_ID")
    private int productId;
    @Basic(optional = false)
    @Column(name = "PRODUCT_NAME")
    private String productName;
    @Basic(optional = false)
    @Column(name = "PRODUCT_QUANTITY")
    private int productQuantity;
    @Basic(optional = false)
    @Column(name = "PRODUCT_COLOR")
    private String productColor;
    @Basic(optional = false)
    @Column(name = "PRODUCT_PRICE")
    private double productPrice;
    @Basic(optional = false)
    @Column(name = "PRODUCT_IMAGE_PATH")
    private String productImagePath;

    public Carts() {
    }

    public Carts(Integer cardId) {
        this.cardId = cardId;
    }

    public Carts(Integer cardId, int customerId, int productId, String productName, int productQuantity, String productColor, double productPrice, String productImagePath) {
        this.cardId = cardId;
        this.customerId = customerId;
        this.productId = productId;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productColor = productColor;
        this.productPrice = productPrice;
        this.productImagePath = productImagePath;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImagePath() {
        return productImagePath;
    }

    public void setProductImagePath(String productImagePath) {
        this.productImagePath = productImagePath;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cardId != null ? cardId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Carts)) {
            return false;
        }
        Carts other = (Carts) object;
        if ((this.cardId == null && other.cardId != null) || (this.cardId != null && !this.cardId.equals(other.cardId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Carts[ cardId=" + cardId + " ]";
    }

}
