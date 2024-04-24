/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
    @NamedQuery(name = "Carts.findByCartId", query = "SELECT c FROM Carts c WHERE c.cartId = :cartId"),
    @NamedQuery(name = "Carts.findByCustomerId", query = "SELECT c FROM Carts c WHERE c.customerId = :customerId"),
    @NamedQuery(name = "Carts.findByProductId", query = "SELECT c FROM Carts c WHERE c.productId = :productId"),
    @NamedQuery(name = "Carts.findByProductColor", query = "SELECT c FROM Carts c WHERE c.productColor = :productColor"),
    @NamedQuery(name = "Carts.findByImagePath", query = "SELECT c FROM Carts c WHERE c.imagePath = :imagePath"),
    @NamedQuery(name = "Carts.findByProductPrice", query = "SELECT c FROM Carts c WHERE c.productPrice = :productPrice"),
    @NamedQuery(name = "Carts.findByProductQuantity", query = "SELECT c FROM Carts c WHERE c.productQuantity = :productQuantity")})
public class Carts implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CART_ID")
    private Integer cartId;
    @Column(name = "CUSTOMER_ID")
    private Integer customerId;
    @Column(name = "PRODUCT_ID")
    private Integer productId;
    @Lob
    @Column(name = "PRODUCT_NAME")
    private String productName;
    @Column(name = "PRODUCT_COLOR")
    private String productColor;
    @Column(name = "IMAGE_PATH")
    private String imagePath;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PRODUCT_PRICE")
    private Double productPrice;
    @Column(name = "PRODUCT_QUANTITY")
    private Integer productQuantity;

    public Carts() {
    }

    public Carts(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cartId != null ? cartId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Carts)) {
            return false;
        }
        Carts other = (Carts) object;
        if ((this.cartId == null && other.cartId != null) || (this.cartId != null && !this.cartId.equals(other.cartId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Carts[ cartId=" + cartId + " ]";
    }
    
}
