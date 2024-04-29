package entities;

public class OrderDetails {

    private double subtotal;
    private double shipping;
    private double tax;
    private double total;

    public OrderDetails() {
    }

    public OrderDetails(double subtotal, double shipping, double tax, double total) {
        this.subtotal = subtotal;
        this.shipping = shipping;
        this.tax = tax;
        this.total = total;
    }

    public String getSubtotal() {
        return String.format("%.2f", subtotal);
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getShipping() {
        return String.format("%.2f", shipping);
    }

    public void setShipping(double shipping) {
        this.shipping = shipping;
    }

    public String getTax() {
        return String.format("%.2f", tax);
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public String getTotal() {
        return String.format("%.2f", total);
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
