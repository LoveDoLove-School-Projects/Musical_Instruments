package services;

import entities.Carts;
import entities.OrderDetails;
import entities.Orders;
import entities.Transactions;
import java.util.List;
import utilities.MailSender;

public class TransactionServices {

    private static final double TAX_RATE = 0.1;
    private static final double FREE_SHIPPING_THRESHOLD = 1000;
    private static final double SHIPPING_COST = 25;
    public static final String SUBJECT = "Payment Receipt";
    public static final String MESSAGE = "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width,initial-scale=1'><title>Payment Receipt</title><style>body{font-family:Arial,sans-serif;margin:0;padding:20px}.container{max-width:600px;margin:0 auto}h1{text-align:center}table{width:100%;border-collapse:collapse}table td,table th{border:1px solid #ddd;padding:8px;text-align:left}table th{background-color:#f2f2f2}</style></head><body><div class='container'><h1>Payment Successfully, Thank you for purchasing our TAR Music Product!</h1><br><h2>Receipt Details:</h2><table><tr><th scope='row'>Transaction Created Date:</th><td>${dateCreatedGmt}</td></tr><tr><th scope='row'>Transaction Updated Date:</th><td>${dateUpdatedGmt}</td></tr><tr><th>Merchant:</th><td>TAR Music</td></tr><tr><th scope='row'>Transaction Number:</th><td>${transactionNumber}</td></tr><tr><th scope='row'>Order Number:</th><td>${orderNumber}</td></tr><tr><th scope='row'>Payment Method:</th><td>${paymentMethod}</td></tr><tr><th scope='row'>Amount:</th><td>MYR ${totalAmount}</td></tr></table></div></body></html>";

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

    public OrderDetails getOrderHistoryDetails(List<Orders> orderList) {
        OrderDetails orderDetail = new OrderDetails();
        double subtotal = 0;
        for (Orders orders : orderList) {
            subtotal += orders.getProductQuantity() * orders.getProductPrice();
        }
        double tax = subtotal * TAX_RATE;
        double shipping = subtotal >= FREE_SHIPPING_THRESHOLD ? 0 : SHIPPING_COST;
        orderDetail.setSubtotal(subtotal);
        orderDetail.setTax(tax);
        orderDetail.setShipping(shipping);
        orderDetail.setTotal(subtotal + tax + shipping);
        return orderDetail;
    }

    public void sendPaymentReceipt(Transactions transaction, String toEmail) {
        String body = MESSAGE.replace("${dateCreatedGmt}", transaction.getDateCreatedGmt().toString())
                .replace("${dateUpdatedGmt}", transaction.getDateUpdatedGmt().toString())
                .replace("${transactionNumber}", transaction.getTransactionNumber())
                .replace("${orderNumber}", transaction.getOrderNumber())
                .replace("${paymentMethod}", transaction.getPaymentMethod())
                .replace("${totalAmount}", transaction.getTotalAmount().toString());
        MailSender.sendEmail(toEmail, SUBJECT, body);
    }
}
