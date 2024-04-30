package controllers;

import common.Constants;
import entities.PaypalPayment;
import entities.Session;
import features.MailSender;
import features.SessionChecker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import utilities.RedirectUtilities;

public class PaymentReceiptServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    private final SessionChecker sessionChecker = new SessionChecker();
    private final MailSender mailSender = new MailSender();
    private static final String RECEIPT_JSP_URL = "/payments/receipt.jsp";
    private static final String SUBJECT = "Payment Receipt";
    private static final String MESSAGE = "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width,initial-scale=1'><title>Payment Receipt</title><style>body{font-family:Arial,sans-serif;margin:0;padding:20px}.container{max-width:600px;margin:0 auto}h1{text-align:center}table{width:100%;border-collapse:collapse}table td,table th{border:1px solid #ddd;padding:8px;text-align:left}table th{background-color:#f2f2f2}</style></head><body><div class='container'><h1>Payment Done. Thank you for purchasing our products</h1><br><h2>Receipt Details:</h2><table><tr><th>Merchant:</th><td>Musical Instruments</td></tr><tr><th>Payer:</th><td>${payer.firstName} ${payer.lastName}</td><!-- Replace with actual payer name --></tr><tr><th>Description:</th><td>${transaction.description}</td><!-- Replace with actual description --></tr><tr><th>Subtotal:</th><td>MYR ${transaction.amount.details.subtotal}</td><!-- Replace with actual subtotal --></tr><tr><th>Shipping:</th><td>MYR ${transaction.amount.details.shipping}</td><!-- Replace with actual shipping cost --></tr><tr><th>Tax:</th><td>MYR ${transaction.amount.details.tax}</td><!-- Replace with actual tax amount --></tr><tr><th>Total:</th><td>MYR ${transaction.amount.total}</td><!-- Replace with actual total amount --></tr></table></div></body></html>";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = sessionChecker.getLoginSession(request.getSession());
        if (!session.isResult()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        HttpSession httpSession = request.getSession();
        PaypalPayment paypalPayment = (PaypalPayment) httpSession.getAttribute("paypalPayment");
        PaypalPayment.PayerInfo payerInfo = paypalPayment.getPayer().getPayer_info();
        PaypalPayment.Transaction transaction = paypalPayment.getTransactions().get(0);
        request.setAttribute("payer", payerInfo);
        request.setAttribute("transaction", transaction);
        String toEmail = paypalPayment.getPayer().getPayer_info().getEmail();
        String body = MESSAGE.replace("${payer.firstName}", paypalPayment.getPayer().getPayer_info().getFirst_name())
                .replace("${payer.lastName}", paypalPayment.getPayer().getPayer_info().getLast_name())
                .replace("${transaction.description}", paypalPayment.getTransactions().get(0).getDescription())
                .replace("${transaction.amount.details.subtotal}", paypalPayment.getTransactions().get(0).getAmount().getDetails().getSubtotal())
                .replace("${transaction.amount.details.shipping}", paypalPayment.getTransactions().get(0).getAmount().getDetails().getShipping())
                .replace("${transaction.amount.details.tax}", paypalPayment.getTransactions().get(0).getAmount().getDetails().getTax())
                .replace("${transaction.amount.total}", paypalPayment.getTransactions().get(0).getAmount().getTotal());
        mailSender.sendEmail(toEmail, SUBJECT, body);
        request.getRequestDispatcher(RECEIPT_JSP_URL).forward(request, response);
    }
}
