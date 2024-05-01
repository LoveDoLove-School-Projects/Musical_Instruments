package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import common.Constants;
import entities.Carts;
import entities.PaypalPayment;
import entities.Session;
import exceptions.PaymentException;
import features.SessionChecker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import services.PaypalServices;
import utilities.RedirectUtilities;

@WebServlet(name = "PaypalServlet", urlPatterns = {"/payments/paypal"})
public class PaypalServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    private final PaypalServices paypalServices = new PaypalServices();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        Session session = SessionChecker.getLoginSession(request.getSession());
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
            PaypalPayment payment = new Gson().fromJson(paymentResponse, PaypalPayment.class);
            String approval_url = null;
            for (PaypalPayment.Link link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    approval_url = link.getHref();
                }
            }
            response.sendRedirect(approval_url);
        } catch (JsonSyntaxException | IOException ex) {
            throw new PaymentException(ex.getMessage());
        }
    }

    private boolean insertNewTransactionToDb(PaypalPayment paypalPayment) {
        
    }
}
