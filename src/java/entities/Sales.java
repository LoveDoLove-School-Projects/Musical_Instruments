package entities;

import java.math.BigDecimal;
import java.util.Date;

public class Sales {

    private int productId;
    private String productName;
    private Date orderDate;
    private int totalQuantity;
    private BigDecimal totalAmount;
    private byte[] productImage;

    public Sales() {
    }

    public Sales(String productName, Date orderDate, int totalQuantity, BigDecimal totalAmount) {
        this.productName = productName;
        this.orderDate = orderDate;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
    }

    public Sales(int productId, String productName, Date orderDate, int totalQuantity, BigDecimal totalAmount, byte[] productImage) {
        this.productId = productId;
        this.productName = productName;
        this.orderDate = orderDate;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.productImage = productImage;
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

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public byte[] getProductImage() {
        return productImage;
    }

    public void setProductImage(byte[] productImage) {
        this.productImage = productImage;
    }

}
