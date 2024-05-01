package controllers;

import com.google.gson.JsonSyntaxException;
import common.Constants;
import entities.Carts;
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
            String approval_link = paypalServices.createPayment(cartList);
            response.sendRedirect(approval_link);
        } catch (JsonSyntaxException | IOException ex) {
            throw new PaymentException(ex.getMessage());
        }
    }
}
