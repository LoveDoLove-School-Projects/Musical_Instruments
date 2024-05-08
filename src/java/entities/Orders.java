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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "ORDERS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Orders.findAll", query = "SELECT o FROM Orders o"),
    @NamedQuery(name = "Orders.findById", query = "SELECT o FROM Orders o WHERE o.id = :id"),
    @NamedQuery(name = "Orders.findByUserId", query = "SELECT o FROM Orders o WHERE o.userId = :userId"),
    @NamedQuery(name = "Orders.findByProductId", query = "SELECT o FROM Orders o WHERE o.productId = :productId"),
    @NamedQuery(name = "Orders.findByProductName", query = "SELECT o FROM Orders o WHERE o.productName = :productName"),
    @NamedQuery(name = "Orders.findByProductQuantity", query = "SELECT o FROM Orders o WHERE o.productQuantity = :productQuantity"),
    @NamedQuery(name = "Orders.findByProductColor", query = "SELECT o FROM Orders o WHERE o.productColor = :productColor"),
    @NamedQuery(name = "Orders.findByProductPrice", query = "SELECT o FROM Orders o WHERE o.productPrice = :productPrice"),
    @NamedQuery(name = "Orders.findByProductTotalprice", query = "SELECT o FROM Orders o WHERE o.productTotalprice = :productTotalprice"),
    @NamedQuery(name = "Orders.findByOrderNumber", query = "SELECT o FROM Orders o WHERE o.orderNumber = :orderNumber"),
    @NamedQuery(name = "Orders.findByOrderDate", query = "SELECT o FROM Orders o WHERE o.orderDate = :orderDate")})
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "USER_ID")
    private int userId;
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
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "PRODUCT_TOTALPRICE")
    private BigDecimal productTotalprice;
    @Basic(optional = false)
    @Column(name = "ORDER_NUMBER")
    private String orderNumber;
    @Basic(optional = false)
    @Column(name = "ORDER_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    public Orders() {
    }

    public Orders(Integer id) {
        this.id = id;
    }

    public Orders(Integer id, int userId, int productId, String productName, int productQuantity, String productColor, double productPrice, byte[] productImage, BigDecimal productTotalprice, String orderNumber, Date orderDate) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productColor = productColor;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.productTotalprice = productTotalprice;
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public BigDecimal getProductTotalprice() {
        return productTotalprice;
    }

    public void setProductTotalprice(BigDecimal productTotalprice) {
        this.productTotalprice = productTotalprice;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
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
        if (!(object instanceof Orders)) {
            return false;
        }
        Orders other = (Orders) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Orders{");
        sb.append("id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", productId=").append(productId);
        sb.append(", productName=").append(productName);
        sb.append(", productQuantity=").append(productQuantity);
        sb.append(", productColor=").append(productColor);
        sb.append(", productPrice=").append(productPrice);
        sb.append(", productImage=").append(productImage);
        sb.append(", productTotalprice=").append(productTotalprice);
        sb.append(", orderNumber=").append(orderNumber);
        sb.append(", orderDate=").append(orderDate);
        sb.append('}');
        return sb.toString();
    }

}
