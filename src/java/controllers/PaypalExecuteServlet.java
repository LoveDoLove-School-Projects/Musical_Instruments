package controllers;

import common.Constants;
import entities.Customers;
import entities.PaypalPayment;
import entities.Session;
import entities.Transactions;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
import utilities.MailSender;
import utilities.RedirectUtilities;
import utilities.SessionUtilities;

@WebServlet(name = "PaypalExecuteServlet", urlPatterns = {"/payments/paypal/execute"})
public class PaypalExecuteServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
    private static final Logger LOG = Logger.getLogger(PaypalExecuteServlet.class.getName());
    private static final String RECEIPT_URL = "/payments/receipt";
    private static final String UPDATE_ORDER_URL = "/pages/orders/updateOrder";
    private final PaypalServices paypalServices = new PaypalServices();
    private static final String SUBJECT = "Payment Receipt";
    private static final String MESSAGE = "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width,initial-scale=1'><title>Payment Receipt</title><style>body{font-family:Arial,sans-serif;margin:0;padding:20px}.container{max-width:600px;margin:0 auto}h1{text-align:center}table{width:100%;border-collapse:collapse}table td,table th{border:1px solid #ddd;padding:8px;text-align:left}table th{background-color:#f2f2f2}</style></head><body><div class='container'><h1>Payment Successfully, Thank you for purchasing our TAR Music Product!</h1><br><h2>Receipt Details:</h2><table><tr><th scope='row'>Transaction Created Date:</th><td>${dateCreatedGmt}</td></tr><tr><th scope='row'>Transaction Updated Date:</th><td>${dateUpdatedGmt}</td></tr><tr><th>Merchant:</th><td>TAR Music</td></tr><tr><th scope='row'>Transaction Number:</th><td>${transactionNumber}</td></tr><tr><th scope='row'>Order Number:</th><td>${orderNumber}</td></tr><tr><th scope='row'>Payment Method:</th><td>${paymentMethod}</td></tr><tr><th scope='row'>Amount:</th><td>MYR ${totalAmount}</td></tr></table></div></body></html>";

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
        if (!updateTransactionToDB(paypalPayment)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Failed to update transaction.", Constants.CART_URL);
            return;
        }
        Transactions transaction = entityManager.createNamedQuery("Transactions.findByTransactionNumber", Transactions.class).setParameter("transactionNumber", paypalPayment.getId()).getSingleResult();
        if ("approved".equals(paypalPayment.getState())) {
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute("paypalPayment", paypalPayment);
            String toEmail = getEmail(session);
            String body = MESSAGE.replace("${dateCreatedGmt}", transaction.getDateCreatedGmt().toString())
                    .replace("${dateUpdatedGmt}", transaction.getDateUpdatedGmt().toString())
                    .replace("${transactionNumber}", transaction.getTransactionNumber())
                    .replace("${orderNumber}", transaction.getOrderNumber())
                    .replace("${paymentMethod}", transaction.getPaymentMethod())
                    .replace("${totalAmount}", transaction.getTotalAmount().toString());
            MailSender.sendEmail(toEmail, SUBJECT, body);
//            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, "Payment successful.", RECEIPT_URL + "?transactionNumber=" + transaction.getTransactionNumber());
            String url = UPDATE_ORDER_URL + "?transaction_number=" + transaction.getTransactionNumber() + "&order_number=" + transaction.getOrderNumber() + "&txnStatus=" + transaction.getTransactionStatus();
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, "Payment successful.", url);
        } else {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Payment failed.", "/");
        }
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

    private String getEmail(Session sesion) {
        try {
            return entityManager.createNamedQuery("Customers.findByUserId", Customers.class)
                    .setParameter("userId", sesion.getUserId())
                    .getSingleResult().getEmail();
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
            return null;
        }
    }
}
