package services;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import entities.AccessToken;
import entities.Carts;
import entities.Customers;
import entities.Environment;
import entities.MemoryCache;
import entities.OrderDetails;
import entities.PaypalPayment;
import exceptions.PaymentException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import utilities.AesUtilities;
import utilities.HttpUtilities;

public class PaypalServices {

    private static final Logger LOG = Logger.getLogger(PaypalServices.class.getName());
    private static final String CLIENT_ID = "AZrjdHeC-KD9nsZVH8HR54O-3ZgvAshjqYq4hiPgXGL7ZKcps159a3mTW-YqLlvLQzBNveUjdpSELOuX";
    private static final String CLIENT_SECRET = "EGwkzMhtunT9dpkeIGuanST6nkKzdwRVFWHofvCYv8HFHy-RMk_A65bfFVw_p08ZCQaIMEtZKXVOswOY";
    private static final String RETURN_URL = "http://localhost:8080/Musical_Instruments/payments/paypal/review";
    private static final String CANCEL_URL = "http://localhost:8080/Musical_Instruments/payments/cancel";
    private static final String ACCESS_TOKEN_API = AesUtilities.aes256EcbDecrypt(Environment.ACCESS_TOKEN_API);
    private static final String CREATE_PAYMENT_API = AesUtilities.aes256EcbDecrypt(Environment.CREATE_PAYMENT_API);
    private static final String GET_PAYMENT_API = AesUtilities.aes256EcbDecrypt(Environment.GET_PAYMENT_API);
    private static final String EXECUTE_PAYMENT_API = AesUtilities.aes256EcbDecrypt(Environment.EXECUTE_PAYMENT_API);
    private static final String CURRENCY = "MYR";
    private static final MemoryCache<String, String> MEMORY_CACHE = new MemoryCache<>(3);
    private static final MemoryCache<String, Long> TIME_CACHE = new MemoryCache<>(3);
    private final TransactionServices transactionServices = new TransactionServices();

    public String createPayment(List<Carts> cartList, Customers customer) {
        try {
            String accessToken = getAccessToken();
            String paymentJsonPayload = constructPayload(cartList, customer);
            return createPayment(accessToken, paymentJsonPayload);
        } catch (IOException ex) {
            throw new PaymentException(ex.getMessage());
        }
    }

    private String constructPayload(List<Carts> cartList, Customers customer) {
        List<Transaction> transactions = getTransactionInformation(cartList);
        Payer payer = getPayerInformation(customer);
        Payment payment = getPaymentInformation(payer, transactions);
        RedirectUrls redirectUrls = getRedirectURLs();
        payment.setRedirectUrls(redirectUrls);
        return payment.toJSON();
    }

    private Payer getPayerInformation(Customers customer) {
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setFirstName(customer.getFirstName())
                .setLastName(customer.getLastName())
                .setEmail(customer.getEmail());
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
        transaction.setDescription("Pay To TAR Music");
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

    private String getAccessToken() throws IOException {
        String accessToken = MEMORY_CACHE.get("access_token");
        Long tokenIssueTimeMillis = TIME_CACHE.get("token_issue_time");
        Long expiresIn = TIME_CACHE.get("expires_in");
        if (accessToken == null || expiresIn == null || tokenIssueTimeMillis == null || isTokenExpired(tokenIssueTimeMillis, expiresIn)) {
            AccessToken accessTokenObj = requestAccessToken();
            accessToken = accessTokenObj.getAccess_token();
            expiresIn = TimeUnit.SECONDS.toMillis(accessTokenObj.getExpires_in());
            MEMORY_CACHE.put("access_token", accessToken);
            TIME_CACHE.put("token_issue_time", System.currentTimeMillis());
            TIME_CACHE.put("expires_in", expiresIn);
        }
        return accessToken;
    }

    private boolean isTokenExpired(Long tokenIssueTimeMillis, Long expiresIn) {
        return (tokenIssueTimeMillis + expiresIn) < System.currentTimeMillis();
    }

    private AccessToken requestAccessToken() throws IOException {
        String jsonPayload = "{\"client_id\":\"" + CLIENT_ID + "\",\"client_secret\":\"" + CLIENT_SECRET + "\"}";
        String response = HttpUtilities.sendHttpJsonRequest(ACCESS_TOKEN_API, jsonPayload);
        AccessToken accessTokenObj = new Gson().fromJson(response, AccessToken.class);
        return accessTokenObj;
    }

    private String createPayment(String accessToken, String paymentJsonPayload) {
        try {
            String jsonPayload = "{\"access_token\":\"" + accessToken + "\",\"payment_body\":" + paymentJsonPayload + "}";
            return HttpUtilities.sendHttpJsonRequest(CREATE_PAYMENT_API, jsonPayload);
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
            return null;
        }
    }

    public PaypalPayment getPaymentDetails(String paymentId) {
        try {
            String accessToken = getAccessToken();
            String jsonPayload = "{\"access_token\":\"" + accessToken + "\",\"payment_id\":\"" + paymentId + "\"}";
            String response = HttpUtilities.sendHttpJsonRequest(GET_PAYMENT_API, jsonPayload);
            return new Gson().fromJson(response, PaypalPayment.class);
        } catch (JsonSyntaxException | IOException ex) {
            throw new PaymentException(ex.getMessage());
        }
    }

    public PaypalPayment executePayment(String payment_id, String payer_id) {
        try {
            String accessToken = getAccessToken();
            String jsonPayload = "{\"access_token\":\"" + accessToken + "\",\"payment_id\":\"" + payment_id + "\",\"payer_id\":\"" + payer_id + "\"}";
            String response = HttpUtilities.sendHttpJsonRequest(EXECUTE_PAYMENT_API, jsonPayload);
            return new Gson().fromJson(response, PaypalPayment.class);
        } catch (JsonSyntaxException | IOException ex) {
            throw new PaymentException(ex.getMessage());
        }
    }
}
