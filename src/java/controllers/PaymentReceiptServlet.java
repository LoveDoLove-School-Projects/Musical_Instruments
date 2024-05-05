package controllers;

import common.Constants;
import entities.Customers;
import entities.PaypalPayment;
import entities.Session;
import utilities.MailSender;
import utilities.SessionUtilities;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.logging.Logger;
import utilities.RedirectUtilities;

public class PaymentReceiptServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    private static final Logger LOG = Logger.getLogger(PaymentReceiptServlet.class.getName());
    private static final String RECEIPT_JSP_URL = "/payments/receipt.jsp";
    private static final String SUBJECT = "Payment Receipt";
    private static final String MESSAGE = "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width,initial-scale=1'><title>Payment Receipt</title><style>body{font-family:Arial,sans-serif;margin:0;padding:20px}.container{max-width:600px;margin:0 auto}h1{text-align:center}table{width:100%;border-collapse:collapse}table td,table th{border:1px solid #ddd;padding:8px;text-align:left}table th{background-color:#f2f2f2}</style></head><body><div class='container'><h1>Payment Successfully, Thank you for purchasing our TAR Music Product!</h1><br><h2>Receipt Details:</h2><table><tr><th>Merchant:</th><td>TAR Music</td></tr><tr><th>Payer:</th><td>${payer.firstName} ${payer.lastName}</td></tr><tr><th>Description:</th><td>${transaction.description}</td></tr><tr><th>Subtotal:</th><td>MYR ${transaction.amount.details.subtotal}</td></tr><tr><th>Shipping:</th><td>MYR ${transaction.amount.details.shipping}</td></tr><tr><th>Tax:</th><td>MYR ${transaction.amount.details.tax}</td></tr><tr><th>Total:</th><td>MYR ${transaction.amount.total}</td></tr></table></div></body></html>";

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
        HttpSession httpSession = request.getSession();
        PaypalPayment paypalPayment = (PaypalPayment) httpSession.getAttribute("paypalPayment");
        PaypalPayment.PayerInfo payerInfo = paypalPayment.getPayer().getPayer_info();
        PaypalPayment.Transaction transaction = paypalPayment.getTransactions().get(0);
        request.setAttribute("payer", payerInfo);
        request.setAttribute("transaction", transaction);
        String firstName = payerInfo.getFirst_name();
        String lastName = payerInfo.getLast_name();
        String description = transaction.getDescription();
        String subtotal = transaction.getAmount().getDetails().getSubtotal();
        String shipping = transaction.getAmount().getDetails().getShipping();
        String tax = transaction.getAmount().getDetails().getTax();
        String total = transaction.getAmount().getTotal();
        String body = MESSAGE.replace("${payer.firstName}", firstName)
                .replace("${payer.lastName}", lastName)
                .replace("${transaction.description}", description)
                .replace("${transaction.amount.details.subtotal}", subtotal)
                .replace("${transaction.amount.details.shipping}", shipping)
                .replace("${transaction.amount.details.tax}", tax)
                .replace("${transaction.amount.total}", total);
        String toEmail = getEmail(session);
        MailSender.sendEmail(toEmail, SUBJECT, body);
        request.getRequestDispatcher(RECEIPT_JSP_URL).forward(request, response);
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
