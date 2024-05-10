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
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Kai Quan
 */
@Entity
@Table(name = "CARTS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Carts.findAll", query = "SELECT c FROM Carts c"),
    @NamedQuery(name = "Carts.findByCartId", query = "SELECT c FROM Carts c WHERE c.cartId = :cartId"),
    @NamedQuery(name = "Carts.findByCustomerId", query = "SELECT c FROM Carts c WHERE c.customerId = :customerId"),
    @NamedQuery(name = "Carts.findByProductId", query = "SELECT c FROM Carts c WHERE c.productId = :productId"),
    @NamedQuery(name = "Carts.findByProductName", query = "SELECT c FROM Carts c WHERE c.productName = :productName"),
    @NamedQuery(name = "Carts.findByProductQuantity", query = "SELECT c FROM Carts c WHERE c.productQuantity = :productQuantity"),
    @NamedQuery(name = "Carts.findByProductColor", query = "SELECT c FROM Carts c WHERE c.productColor = :productColor"),
    @NamedQuery(name = "Carts.findByProductPrice", query = "SELECT c FROM Carts c WHERE c.productPrice = :productPrice"),
    @NamedQuery(name = "Carts.findByProductTotalprice", query = "SELECT c FROM Carts c WHERE c.productTotalprice = :productTotalprice")})
public class Carts {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CART_ID")
    private Integer cartId;
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
    @Lob
    @Column(name = "PRODUCT_IMAGE")
    private byte[] productImage;
    @Basic(optional = false)
    @Column(name = "PRODUCT_TOTALPRICE")
    private double productTotalprice;

    public Carts() {
    }

    public Carts(Integer cartId) {
        this.cartId = cartId;
    }

    public Carts(Integer cartId, int customerId, int productId, String productName, int productQuantity, String productColor, double productPrice, byte[] productImage, double productTotalprice) {
        this.cartId = cartId;
        this.customerId = customerId;
        this.productId = productId;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productColor = productColor;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.productTotalprice = productTotalprice;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
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

    public byte[] getProductImage() {
        return productImage;
    }

    public void setProductImage(byte[] productImage) {
        this.productImage = productImage;
    }

    public double getProductTotalprice() {
        return productTotalprice;
    }

    public void setProductTotalprice(double productTotalprice) {
        this.productTotalprice = productTotalprice;
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
}
