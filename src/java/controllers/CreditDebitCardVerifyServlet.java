package controllers;

import common.Constants;
import entities.Carts;
import entities.Customers;
import entities.OrderDetails;
import entities.OtpsType;
import entities.Session;
import entities.Transactions;
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
import java.util.logging.Logger;
import services.OtpServices;
import services.TransactionServices;
import utilities.RedirectUtilities;
import utilities.SessionUtilities;

@WebServlet(name = "CreditDebitCardVerifyServlet", urlPatterns = {"/payments/ccdc/verify"})
public class CreditDebitCardVerifyServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
    private static final Logger LOG = Logger.getLogger(CreditDebitCardVerifyServlet.class.getName());
    private static final String CCDC_REVIEW_JSP_URL = "/payments/ccdcVerify.jsp";
    private static final String CCDC_VERIFY_URL = "/payments/ccdc/verify";
    private static final Map<OtpsType, String> STATUS_MESSAGES;
    private static final String APPROVED = "approved";
    private static final String FAILED = "failed";
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
        if (!validateTransaction(request, response, session)) {
            return;
        }
        otpServices.sendOtp(session.getEmail());
        request.getRequestDispatcher(CCDC_REVIEW_JSP_URL).forward(request, response);
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
        if (!validateTransaction(request, response, session)) {
            return;
        }
        OtpsType otpStatus = validateOtp(request, response, session);
        if (otpStatus == null) {
            return;
        }
        String status = updateTransaction(request, otpStatus);
        switch (status) {
            case APPROVED:
                RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, "Payment successful!", "/");
                break;
            case FAILED:
                RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Payment failed!", "/");
                break;
            default:
                RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Invalid transaction.", "/");
                break;
        }
    }

    private String updateTransaction(HttpServletRequest request, OtpsType otpStatus) throws IOException, ServletException {
        HttpSession httpSession = request.getSession();
        Transactions transaction = (Transactions) httpSession.getAttribute("transaction");
        if (transaction == null) {
            return null;
        }
        if (updateTransactionToDB(transaction, otpStatus)) {
            return otpStatus == OtpsType.OK ? "approved" : "failed";
        }
        return null;
    }

    private OtpsType validateOtp(HttpServletRequest request, HttpServletResponse response, Session session) throws IOException, ServletException {
        String otp = request.getParameter("otp");
        if (otp == null || otp.isEmpty()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Payment failed!", "/");
            return null;
        }
        return otpServices.verifyOtp(session.getEmail(), otp);
    }

    private boolean validateTransaction(HttpServletRequest request, HttpServletResponse response, Session session) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        Transactions transaction = (Transactions) httpSession.getAttribute("transaction");
        if (transaction == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Invalid transaction.", "/");
            return false;
        }
        List<Carts> cartList = (List<Carts>) httpSession.getAttribute("cartList");
        if (cartList == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please add items to cart to view this page.", "/");
            return false;
        }
        Customers customer = entityManager.find(Customers.class, session.getUserId());
        if (customer == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Invalid customer.", "/");
            return false;
        }
        OrderDetails orderDetails = transactionServices.getOrderDetails(cartList);
        if (orderDetails == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Invalid order details.", "/");
            return false;
        }
        httpSession.setAttribute("customer", customer);
        httpSession.setAttribute("orderDetails", orderDetails);
        return true;
    }

    private boolean updateTransactionToDB(Transactions transaction, OtpsType otpStatus) {
        try {
            userTransaction.begin();
            Transactions dbTransaction = entityManager.createNamedQuery("Transactions.findByTransactionNumber", Transactions.class).setParameter("transactionNumber", transaction.getTransactionNumber()).getSingleResult();
            if (otpStatus == OtpsType.OK) {
                dbTransaction.setTransactionStatus(APPROVED);
                dbTransaction.setDateUpdatedGmt(new Date());
            } else {
                dbTransaction.setTransactionStatus(FAILED);
                dbTransaction.setDateUpdatedGmt(new Date());
            }
            entityManager.merge(transaction);
            userTransaction.commit();
            return true;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | NumberFormatException | SecurityException ex) {
            LOG.severe(ex.getMessage());
            return false;
        }
    }
}
