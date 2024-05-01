package controllers;

import common.Constants;
import entities.PaypalPayment;
import entities.Session;
import features.SessionChecker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import services.PaypalServices;
import utilities.RedirectUtilities;

@WebServlet(name = "PaypalExecuteServlet", urlPatterns = {"/payments/paypal/execute"})
public class PaypalExecuteServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    private static final String RECEIPT_URL = "/payments/receipt";
    private final PaypalServices paypalServices = new PaypalServices();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = SessionChecker.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        String paymentId = request.getParameter("paymentId");
        String payerId = request.getParameter("PayerID");
        PaypalPayment paypalPayment = paypalServices.executePayment(paymentId, payerId);
        if ("approved".equals(paypalPayment.getState())) {
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute("paypalPayment", paypalPayment);
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, "Payment successful.", RECEIPT_URL);
        } else {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Payment failed.", "/");
        }
    }
}
