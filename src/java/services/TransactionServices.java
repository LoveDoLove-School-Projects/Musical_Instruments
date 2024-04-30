package services;

import entities.Carts;
import entities.OrderDetails;
import java.util.List;

public class TransactionServices {

    private static final double TAX_RATE = 0.1;
    private static final double FREE_SHIPPING_THRESHOLD = 1000;
    private static final double SHIPPING_COST = 25;

    public OrderDetails getOrderDetails(List<Carts> cartList) {
        OrderDetails orderDetail = new OrderDetails();
        double subtotal = 0;
        for (Carts cart : cartList) {
            subtotal += cart.getProductQuantity() * cart.getProductPrice();
        }
        double tax = subtotal * TAX_RATE;
        double shipping = subtotal >= FREE_SHIPPING_THRESHOLD ? 0 : SHIPPING_COST;
        orderDetail.setSubtotal(subtotal);
        orderDetail.setTax(tax);
        orderDetail.setShipping(shipping);
        orderDetail.setTotal(subtotal + tax + shipping);
        return orderDetail;
    }
}
