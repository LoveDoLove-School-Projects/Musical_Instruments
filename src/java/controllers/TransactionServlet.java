package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import entities.Constants;
import entities.Cards;
import entities.Carts;
import entities.Customers;
import entities.OrderDetails;
import entities.PaypalPayment;
import entities.Session;
import entities.Transactions;
import exceptions.DatabaseException;
import exceptions.PaymentException;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import services.PaypalServices;
import services.TransactionServices;
import utilities.RedirectUtilities;
import utilities.SecurityLog;
import utilities.SessionUtilities;
import utilities.StringUtilities;

public class TransactionServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
    private static final String PAYPAL = "Paypal";
    private static final String CREDIT_OR_DEBIT_CARD = "CreditOrDebitCard";
    private static final String CCDC_VERIFY_URL = "/payments/ccdc/verify";
    private static final String BILLING_DETAILS_URL = "/pages/billingDetails";
    private final PaypalServices paypalServices = new PaypalServices();
    private final TransactionServices transactionServices = new TransactionServices();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        Session session = SessionUtilities.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        if (!validateBillingDetails(session)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please complete your billing details.", BILLING_DETAILS_URL);
            return;
        }
        List<Carts> cartList = entityManager.createNamedQuery("Carts.findByCustomerId", Carts.class).setParameter("customerId", session.getUserId()).getResultList();
        if (cartList == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Cart is empty.", Constants.CART_URL);
            return;
        }
        String paymentMethod = request.getParameter("paymentMethod");
        if (StringUtilities.anyNullOrBlank(paymentMethod)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Invalid payment method.", Constants.CART_URL);
            return;
        }
        Customers customer = entityManager.find(Customers.class, session.getUserId());
        if (customer == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Invalid customer.", Constants.CART_URL);
            return;
        }
        switch (paymentMethod) {
            case PAYPAL:
                processPaypalPayment(request, response, session, cartList, customer);
                break;
            case CREDIT_OR_DEBIT_CARD:
                processCreditOrDebitCardPayment(request, response, session, cartList);
                break;
            default:
                RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Invalid payment method.", Constants.CART_URL);
        }
    }

    private boolean validateBillingDetails(Session session) {
        Customers customer = entityManager.find(Customers.class, session.getUserId());
        if (customer == null) {
            return false;
        }
        return !StringUtilities.anyNullOrBlank(customer.getEmail(), customer.getCountry(), customer.getFirstName(), customer.getLastName(), customer.getAddress(), customer.getCity(), customer.getState(), customer.getZipCode(), customer.getPhoneNumber());
    }

    private void processPaypalPayment(HttpServletRequest request, HttpServletResponse response, Session session, List<Carts> cartList, Customers customer) throws IOException {
        try {
            String paymentResponse = paypalServices.createPayment(cartList, customer);
            if (paymentResponse == null) {
                RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Failed to create payment.", Constants.CART_URL);
                return;
            }
            PaypalPayment payment = new Gson().fromJson(paymentResponse, PaypalPayment.class);
            if (savePaypalTransactionToDB(request, session, payment)) {
                String approval_url = null;
                for (PaypalPayment.Link link : payment.getLinks()) {
                    if (link.getRel().equals("approval_url")) {
                        approval_url = link.getHref();
                    }
                }
                HttpSession httpSession = request.getSession();
                httpSession.setAttribute("transaction_number", payment.getId());
                response.sendRedirect(approval_url);
                return;
            }
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Failed to create transaction.", Constants.CART_URL);
        } catch (JsonSyntaxException | IOException ex) {
            throw new PaymentException(ex.getMessage());
        }
    }

    private void processCreditOrDebitCardPayment(HttpServletRequest request, HttpServletResponse response, Session session, List<Carts> cartList) throws IOException {
        String paymentMethod = request.getParameter("paymentMethod");
        String cardHolderName = request.getParameter("cardHolderName");
        String card1 = request.getParameter("card1");
        String card2 = request.getParameter("card2");
        String card3 = request.getParameter("card3");
        String card4 = request.getParameter("card4");
        String cvv = request.getParameter("cvv");
        String expYear = request.getParameter("expYear");
        String expMonth = request.getParameter("expMonth");
        if (!validateCardDetails(cardHolderName, card1, card2, card3, card4, cvv, expYear, expMonth)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Invalid card details.", Constants.CART_URL);
            return;
        }
        String cardNumber = card1 + card2 + card3 + card4;
        Cards card = new Cards(cardHolderName, cardNumber, expYear, expMonth, cvv);
        if (!checkCardDetails(card)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Invalid card details.", Constants.CART_URL);
            return;
        }
        OrderDetails orderDetails = transactionServices.getOrderDetails(cartList);
        Transactions transaction = saveCCDCTransactionToDB(request, session, orderDetails, paymentMethod);
        if (transaction == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Failed to create transaction.", Constants.CART_URL);
            return;
        }
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("transaction", transaction);
        httpSession.setAttribute("transaction_number", transaction.getTransactionNumber());
        httpSession.setAttribute("cartList", cartList);
        RedirectUtilities.sendRedirect(request, response, CCDC_VERIFY_URL);
    }

    private boolean validateCardDetails(String cardHolderName, String card1, String card2, String card3, String card4, String cvv, String expYear, String expMonth) {
        if (StringUtilities.anyNullOrBlank(cardHolderName, card1, card2, card3, card4, cvv, expYear, expMonth)) {
            return false;
        }
        if (expYear.length() != 4 || expMonth.length() != 2) {
            return false;
        }
        if (cvv.length() != 3) {
            return false;
        }
        if (!StringUtilities.isNumeric(card1) || !StringUtilities.isNumeric(card2) || !StringUtilities.isNumeric(card3) || !StringUtilities.isNumeric(card4) || !StringUtilities.isNumeric(cvv) || !StringUtilities.isNumeric(expYear) || !StringUtilities.isNumeric(expMonth)) {
            return false;
        }
        if (Integer.parseInt(expYear) < LocalDate.now().getYear()) {
            return false;
        }
        String cardNumber = card1 + card2 + card3 + card4;
        if (cardNumber.length() != 16) {
            return false;
        }
        String cardExpiry = expYear + "-" + expMonth;
        return cardExpiry.length() == 7;
    }

    private boolean checkCardDetails(Cards card) {
        try {
            List<Cards> cardList = entityManager.createNamedQuery("Cards.findByCardNumber", Cards.class).setParameter("cardNumber", card.getCardNumber()).getResultList();
            if (cardList == null || cardList.isEmpty()) {
                return false;
            }
            Cards dbCard = cardList.get(0);
            if (!dbCard.getCardHolderName().equals(card.getCardHolderName())) {
                return false;
            }
            if (!dbCard.getCardNumber().equals(card.getCardNumber())) {
                return false;
            }
            if (!dbCard.getExpYear().equals(card.getExpYear()) || !dbCard.getExpMonth().equals(card.getExpMonth())) {
                return false;
            }
            return dbCard.getCvv().equals(card.getCvv());
        } catch (Exception ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    private Transactions saveCCDCTransactionToDB(HttpServletRequest request, Session session, OrderDetails orderDetails, String paymentMethod) {
        try {
            String transactionNumber = "TXN" + System.currentTimeMillis() + session.getUserId();
            String orderNumber = "ORD" + System.currentTimeMillis() + session.getUserId();
            Date date = new Date();
            Transactions transaction = new Transactions();
            transaction.setUserId(session.getUserId());
            transaction.setTransactionNumber(transactionNumber);
            transaction.setOrderNumber(orderNumber);
            transaction.setTransactionStatus("created");
            transaction.setPaymentMethod(paymentMethod);
            transaction.setCurrency("MYR");
            transaction.setTotalAmount(BigDecimal.valueOf(Double.parseDouble(orderDetails.getTotal())));
            transaction.setDateCreatedGmt(date);
            transaction.setDateUpdatedGmt(date);
            userTransaction.begin();
            entityManager.persist(transaction);
            userTransaction.commit();
            SecurityLog.addSecurityLog(request, "create credit or debit card transaction successful.");
            return transaction;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | NumberFormatException | SecurityException ex) {
            SecurityLog.addSecurityLog(request, "create credit or debit card transaction failed.");
            throw new DatabaseException(ex.getMessage());
        }
    }

    private boolean savePaypalTransactionToDB(HttpServletRequest request, Session session, PaypalPayment paypalPayment) {
        try {
            String orderNumber = "ORD" + System.currentTimeMillis() + session.getUserId();
            Date date = new Date();
            Transactions transaction = new Transactions();
            transaction.setUserId(session.getUserId());
            transaction.setTransactionNumber(paypalPayment.getId());
            transaction.setOrderNumber(orderNumber);
            transaction.setTransactionStatus(paypalPayment.getState());
            transaction.setPaymentMethod(paypalPayment.getPayer().getPayment_method());
            transaction.setCurrency(paypalPayment.getTransactions().get(0).getAmount().getCurrency());
            transaction.setTotalAmount(BigDecimal.valueOf(Double.parseDouble(paypalPayment.getTransactions().get(0).getAmount().getTotal())));
            transaction.setDateCreatedGmt(date);
            transaction.setDateUpdatedGmt(date);
            userTransaction.begin();
            entityManager.persist(transaction);
            userTransaction.commit();
            SecurityLog.addSecurityLog(request, "create paypal transaction successful.");
            return true;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | NumberFormatException | SecurityException ex) {
            SecurityLog.addSecurityLog(request, "create paypal transaction failed.");
            throw new DatabaseException(ex.getMessage());
        }
    }
}
