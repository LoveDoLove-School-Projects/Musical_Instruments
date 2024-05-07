package controllers;

import entities.Constants;
import entities.Carts;
import entities.Customers;
import entities.OrderDetails;
import entities.OtpsType;
import entities.Session;
import entities.TransactionStatus;
import entities.Transactions;
import exceptions.DatabaseException;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import java.io.IOException;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import services.OtpServices;
import services.TransactionServices;
import utilities.RedirectUtilities;
import utilities.SecurityLog;
import utilities.SessionUtilities;

@WebServlet(name = "CreditDebitCardVerifyServlet", urlPatterns = {"/payments/ccdc/verify", "/api/payments/ccdc/resendOtp"})
public class CreditDebitCardVerifyServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
    private static final String CCDC_VERIFY_JSP_URL = "/payments/ccdcVerify.jsp";
    private static final String RECEIPT_URL = "/payments/receipt";
    private static final String RESEND_OTP_URL = "/api/payments/ccdc/resendOtp";
    private static final Map<OtpsType, String> STATUS_MESSAGES;
    private final TransactionServices transactionServices = new TransactionServices();
    private final OtpServices otpServices = new OtpServices();

    static {
        STATUS_MESSAGES = new EnumMap<>(OtpsType.class);
        STATUS_MESSAGES.put(OtpsType.NOT_FOUND, "OTP not found!");
        STATUS_MESSAGES.put(OtpsType.UNAUTHORIZED, "Too many attempts! Please click on 'Resend OTP' to try again.");
        STATUS_MESSAGES.put(OtpsType.EXPIRED, "OTP expired! Please click on 'Resend OTP' to try again.");
        STATUS_MESSAGES.put(OtpsType.FAILED, "Failed to verify OTP! Please try again.");
        STATUS_MESSAGES.put(OtpsType.INVALID, "Invalid OTP!");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        Session session = SessionUtilities.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        Transactions transaction = validateTransaction(request, response, session);
        if (transaction == null) {
            return;
        }
        otpServices.sendOtp(session.getEmail());
        request.getRequestDispatcher(CCDC_VERIFY_JSP_URL).forward(request, response);
    }

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
        String path = request.getServletPath();
        if (path.equals(RESEND_OTP_URL)) {
            resendOtp(response, session);
            return;
        }
        String otp = request.getParameter("otp");
        if (otp == null || otp.isEmpty()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Payment failed!", "/");
            return;
        }
        Transactions transaction = validateTransaction(request, response, session);
        if (transaction == null) {
            return;
        }
        OtpsType otpStatus = otpServices.verifyOtp(session.getEmail(), otp);
        if (otpStatus == null) {
            return;
        }
        String transactionStatus = otpStatus == OtpsType.OK ? TransactionStatus.APPROVED : TransactionStatus.FAILED;
        transaction = updateTransactionToDB(request, session, transaction, transactionStatus);
        if (transaction == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Failed to update transaction.", "/");
            return;
        }
        switch (transactionStatus) {
            case TransactionStatus.APPROVED:
                transactionServices.sendPaymentReceipt(transaction, session.getEmail());
                String url = Constants.UPDATE_ORDER_URL + "?transaction_number=" + transaction.getTransactionNumber() + "&order_number=" + transaction.getOrderNumber() + "&txnStatus=" + transaction.getTransactionStatus();
                RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, "Payment successful.", url);
                break;
            case TransactionStatus.FAILED:
                RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Payment failed!", RECEIPT_URL + "?transaction_number=" + transaction.getTransactionNumber());
                break;
            default:
                RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Invalid transaction.", "/");
        }
    }

    private void resendOtp(HttpServletResponse response, Session session) throws ServletException, IOException {
        // return json
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        boolean isSent = otpServices.sendOtp(session.getEmail());
        if (isSent) {
            response.getWriter().write("{\"status\":\"success\",\"message\":\"OTP sent successfully!\"}");
        } else {
            response.getWriter().write("{\"status\":\"danger\",\"message\":\"Failed to send OTP!\"}");
        }
    }

    private Transactions validateTransaction(HttpServletRequest request, HttpServletResponse response, Session session) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        Transactions transaction = (Transactions) httpSession.getAttribute("transaction");
        if (transaction == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Invalid transaction.", "/");
            return null;
        }
        List<Carts> cartList = (List<Carts>) httpSession.getAttribute("cartList");
        if (cartList == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please add items to cart to view this page.", "/");
            return null;
        }
        Customers customer = entityManager.find(Customers.class, session.getUserId());
        if (customer == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Invalid customer.", "/");
            return null;
        }
        OrderDetails orderDetails = transactionServices.getOrderDetails(cartList);
        if (orderDetails == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Invalid order details.", "/");
            return null;
        }
        httpSession.setAttribute("customer", customer);
        httpSession.setAttribute("orderDetails", orderDetails);
        return transaction;
    }

    private Transactions updateTransactionToDB(HttpServletRequest request, Session session, Transactions transaction, String transactionStatus) {
        try {
            userTransaction.begin();
            Transactions dbTransaction = entityManager.createNamedQuery("Transactions.findByTransactionNumberAndUserId", Transactions.class).setParameter("transactionNumber", transaction.getTransactionNumber()).setParameter("userId", session.getUserId()).getSingleResult();
            dbTransaction.setTransactionStatus(transactionStatus);
            dbTransaction.setDateUpdatedGmt(new Date());
            entityManager.merge(dbTransaction);
            userTransaction.commit();
            SecurityLog.addSecurityLog(request, "Transaction updated: " + dbTransaction.getTransactionNumber());
            return dbTransaction;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | NumberFormatException | SecurityException ex) {
            SecurityLog.addSecurityLog(request, "Failed to update transaction: " + transaction.getTransactionNumber());
            throw new DatabaseException(ex.getMessage());
        }
    }
}
