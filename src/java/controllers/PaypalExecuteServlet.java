package controllers;

import common.Constants;
import entities.PaypalPayment;
import entities.Session;
import entities.Transactions;
import exceptions.DatabaseException;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import java.io.IOException;
import java.util.Date;
import services.PaypalServices;
import services.TransactionServices;
import utilities.RedirectUtilities;
import utilities.SecurityLog;
import utilities.SessionUtilities;

@WebServlet(name = "PaypalExecuteServlet", urlPatterns = {"/payments/paypal/execute"})
public class PaypalExecuteServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
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
        String paymentId = request.getParameter("paymentId");
        String payerId = request.getParameter("PayerID");
        PaypalPayment paypalPayment = paypalServices.executePayment(paymentId, payerId);
        if (paypalPayment == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Payment not found.", Constants.CART_URL);
            return;
        }
        Transactions transaction = updateTransactionToDB(session, paypalPayment);
        if (transaction == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Failed to update transaction.", Constants.CART_URL);
            return;
        }
        if ("approved".equals(paypalPayment.getState())) {
            SecurityLog.addSecurityLog(request, "Payment successful with paypal transaction number: " + paypalPayment.getId());
            transactionServices.sendPaymentReceipt(transaction, session.getEmail());
            String url = Constants.UPDATE_ORDER_URL + "?transaction_number=" + transaction.getTransactionNumber() + "&order_number=" + transaction.getOrderNumber() + "&txnStatus=" + transaction.getTransactionStatus();
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, "Payment successful.", url);
        } else {
            SecurityLog.addSecurityLog(request, "Payment failed with paypal transaction number: " + paypalPayment.getId());
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Payment failed.", "/");
        }
    }

    private Transactions updateTransactionToDB(Session session, PaypalPayment paypalPayment) {
        try {
            userTransaction.begin();
            Transactions dbTransaction = entityManager.createNamedQuery("Transactions.findByTransactionNumberAndUserId", Transactions.class).setParameter("transactionNumber", paypalPayment.getId()).setParameter("userId", session.getUserId()).getSingleResult();
            dbTransaction.setTransactionStatus(paypalPayment.getState());
            dbTransaction.setDateUpdatedGmt(new Date());
            entityManager.merge(dbTransaction);
            userTransaction.commit();
            return dbTransaction;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | NumberFormatException | SecurityException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }
}
