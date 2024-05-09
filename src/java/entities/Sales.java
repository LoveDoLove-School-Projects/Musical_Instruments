package entities;

import java.math.BigDecimal;
import java.util.Date;

public class Sales {

    private String productName;
    private Date orderDate;
    private int totalQuantity;
    private BigDecimal totalAmount;

    public Sales() {
    }

    public Sales(String productName, Date orderDate, int totalQuantity, BigDecimal totalAmount) {
        this.productName = productName;
        this.orderDate = orderDate;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
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
}
