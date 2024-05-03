package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import common.Constants;
import entities.Carts;
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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import services.PaypalServices;
import utilities.RedirectUtilities;

@WebServlet(name = "PaypalServlet", urlPatterns = {"/payments/paypal"})
public class PaypalServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
    private static final Logger LOG = Logger.getLogger(PaypalServlet.class.getName());
    private final PaypalServices paypalServices = new PaypalServices();

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
        List<Carts> cartList = entityManager.createNamedQuery("Carts.findByCustomerId", Carts.class).setParameter("customerId", session.getUserId()).getResultList();
        if (cartList == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Cart is empty.", Constants.CART_URL);
            return;
        }
        try {
            String paymentResponse = paypalServices.createPayment(cartList);
            if (paymentResponse == null) {
                RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Failed to create payment.", Constants.CART_URL);
                return;
            }
            PaypalPayment payment = new Gson().fromJson(paymentResponse, PaypalPayment.class);
            if (saveTransactionToDB(session, payment)) {
                String approval_url = null;
                for (PaypalPayment.Link link : payment.getLinks()) {
                    if (link.getRel().equals("approval_url")) {
                        approval_url = link.getHref();
                    }
                }
                response.sendRedirect(approval_url);
                return;
            }
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Failed to save transaction.", Constants.CART_URL);
        } catch (JsonSyntaxException | IOException ex) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Failed to create payment.", Constants.CART_URL);
            LOG.severe(ex.getMessage());
        }
    }

    private boolean saveTransactionToDB(Session session, PaypalPayment paypalPayment) {
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
            return true;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | NumberFormatException | SecurityException ex) {
            LOG.severe(ex.getMessage());
            return false;
        }
    }
}
