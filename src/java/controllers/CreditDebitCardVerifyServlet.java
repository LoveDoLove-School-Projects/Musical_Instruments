package controllers;

import common.Constants;
import entities.Carts;
import entities.Customers;
import entities.OrderDetails;
import entities.Session;
import entities.Transactions;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.transaction.UserTransaction;
import java.io.IOException;
import java.util.List;
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
    private final TransactionServices transactionServices = new TransactionServices();
    private final OtpServices otpServices = new OtpServices();

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
        String otp = request.getParameter("otp");
        if (otp == null || otp.isEmpty()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please enter OTP.", CCDC_VERIFY_URL);
            RedirectUtilities.setMessage(request, RedirectUtilities.RedirectType.DANGER, "Please enter OTP.");
        }
        request.getRequestDispatcher(CCDC_REVIEW_JSP_URL).forward(request, response);
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
}
