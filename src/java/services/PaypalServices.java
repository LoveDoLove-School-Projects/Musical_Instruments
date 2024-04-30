package services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import entities.Carts;
import entities.OrderDetails;
import environments.Enviroment;
import exceptions.PaymentException;
import features.AesProtector;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import utilities.HttpUtilities;

public class PaypalServices {

    private final TransactionServices transactionServices = new TransactionServices();
    private static final String CLIENT_ID = "AZrjdHeC-KD9nsZVH8HR54O-3ZgvAshjqYq4hiPgXGL7ZKcps159a3mTW-YqLlvLQzBNveUjdpSELOuX";
    private static final String CLIENT_SECRET = "EGwkzMhtunT9dpkeIGuanST6nkKzdwRVFWHofvCYv8HFHy-RMk_A65bfFVw_p08ZCQaIMEtZKXVOswOY";
    private static final String RETURN_URL = "http://localhost:8080/Musical_Instruments/payments/paypal/review";
    private static final String CANCEL_URL = "http://localhost:8080/Musical_Instruments/payments/cancel.html";
    private static final String ACCESS_TOKEN_API = AesProtector.aes256EcbDecrypt(Enviroment.ACCESS_TOKEN_API);
    private static final String CREATE_PAYMENT_API = AesProtector.aes256EcbDecrypt(Enviroment.CREATE_PAYMENT_API);
    private static final String CURRENCY = "MYR";

    public String createPayment(List<Carts> cartList) {
        try {
            String accessToken = getAccessToken();
            String paymentJsonPayload = constructPayload(cartList);
            return createPayment(accessToken, paymentJsonPayload);
        } catch (IOException ex) {
            throw new PaymentException(ex.getMessage());
        }
    }

    private String constructPayload(List<Carts> cartList) {
        List<Transaction> transactions = getTransactionInformation(cartList);
        Payer payer = getPayerInformation();
        Payment payment = getPaymentInformation(payer, transactions);
        RedirectUrls redirectUrls = getRedirectURLs();
        payment.setRedirectUrls(redirectUrls);
        return payment.toJSON();
    }

    private String getAccessToken() throws IOException {
        String jsonPayload = "{\"client_id\":\"" + CLIENT_ID + "\",\"client_secret\":\"" + CLIENT_SECRET + "\"}";
        String response = HttpUtilities.sendHttpJsonRequest(ACCESS_TOKEN_API, jsonPayload);
        JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
        return jsonObject.get("access_token").getAsString();
    }

    private Payer getPayerInformation() {
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setFirstName("John")
                .setLastName("Doe")
                .setEmail("sb-kfbrj14644364@business.example.com");
        payer.setPayerInfo(payerInfo);
        return payer;
    }

    private Payment getPaymentInformation(Payer payer, List<Transaction> transactions) {
        Payment payment = new Payment();
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        payment.setIntent("sale");
        return payment;
    }

    private RedirectUrls getRedirectURLs() {
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setReturnUrl(RETURN_URL);
        redirectUrls.setCancelUrl(CANCEL_URL);
        return redirectUrls;
    }

    private List<Transaction> getTransactionInformation(List<Carts> cartList) {
        Amount amount = getAmountDetails(cartList);
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("Demo Transaction");
        ItemList itemList = getItemList(cartList);
        transaction.setItemList(itemList);
        List<Transaction> listTransaction = new ArrayList<>();
        listTransaction.add(transaction);
        return listTransaction;
    }

    private Amount getAmountDetails(List<Carts> cartList) {
        OrderDetails orderDetail = transactionServices.getOrderDetails(cartList);
        Details details = new Details();
        details.setShipping(orderDetail.getShipping());
        details.setSubtotal(orderDetail.getSubtotal());
        details.setTax(orderDetail.getTax());
        Amount amount = new Amount();
        amount.setCurrency(CURRENCY);
        amount.setTotal(orderDetail.getTotal());
        amount.setDetails(details);
        return amount;
    }

    private ItemList getItemList(List<Carts> cartList) {
        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<>();
        for (Carts cart : cartList) {
            Item item = new Item();
            item.setCurrency(CURRENCY);
            item.setName(cart.getProductName());
            item.setPrice(String.valueOf(cart.getProductPrice()));
            item.setTax("0");
            item.setQuantity(String.valueOf(cart.getProductQuantity()));
            items.add(item);
        }
        itemList.setItems(items);
        return itemList;
    }

    private String createPayment(String accessToken, String paymentJsonPayload) {
        try {
            String jsonPayload = "{\"access_token\":\"" + accessToken + "\",\"payment_body\":" + paymentJsonPayload + "}";
            return HttpUtilities.sendHttpJsonRequest(CREATE_PAYMENT_API, jsonPayload);
        } catch (Exception ex) {
            throw new PaymentException(ex.getMessage());
        }
    }
//    public Payment getPaymentDetails(String paymentId) throws PayPalRESTException {
//        return Payment.get(apiContext, paymentId);
//    }
//
//    public Payment executePayment(String paymentId, String payerId) {
//        try {
//            Payment payment = new Payment();
//            payment.setId(paymentId);
//            PaymentExecution paymentExecute = new PaymentExecution();
//            paymentExecute.setPayerId(payerId);
//            return payment.execute(apiContext, paymentExecute);
//        } catch (PayPalRESTException ex) {
//            throw new PaymentException(ex.getMessage());
//        }
//    }
}
