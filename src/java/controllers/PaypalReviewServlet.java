package controllers;

import common.Constants;
import entities.PaypalPayment;
import entities.Session;
import entities.Transactions;
import utilities.SessionUtilities;
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
import java.util.logging.Logger;
import services.PaypalServices;
import utilities.RedirectUtilities;

@WebServlet(name = "PaypalReviewServlet", urlPatterns = {"/payments/paypal/review"})
public class PaypalReviewServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
    private static final Logger LOG = Logger.getLogger(PaypalReviewServlet.class.getName());
    private static final String REVIEW_JSP_URL = "/payments/review.jsp";
    private final PaypalServices paypalServices = new PaypalServices();

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
        String paymentId = request.getParameter("paymentId");
        if (paymentId == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Payment ID is required.", Constants.CART_URL);
            return;
        }
        PaypalPayment paypalPayment = paypalServices.getPaymentDetails(paymentId);
        if (paypalPayment == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Payment not found.", Constants.CART_URL);
            return;
        }
        if (!updateTransactionToDB(paypalPayment)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Failed to update transaction.", Constants.CART_URL);
            return;
        }
        PaypalPayment.PayerInfo payerInfo = paypalPayment.getPayer().getPayer_info();
        PaypalPayment.Transaction transaction = paypalPayment.getTransactions().get(0);
        PaypalPayment.ShippingAddress shippingAddress = transaction.getItem_list().getShipping_address();
        request.setAttribute("payer", payerInfo);
        request.setAttribute("transaction", transaction);
        request.setAttribute("shippingAddress", shippingAddress);
        request.getRequestDispatcher(REVIEW_JSP_URL).forward(request, response);
    }

    private boolean updateTransactionToDB(PaypalPayment paypalPayment) {
        try {
            userTransaction.begin();
            Transactions transaction = entityManager.createNamedQuery("Transactions.findByTransactionNumber", Transactions.class).setParameter("transactionNumber", paypalPayment.getId()).getSingleResult();
            transaction.setTransactionStatus(paypalPayment.getState());
            transaction.setDateUpdatedGmt(new Date());
            entityManager.merge(transaction);
            userTransaction.commit();
            return true;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | NumberFormatException | SecurityException ex) {
            LOG.severe(ex.getMessage());
            return false;
        }
    }
}
