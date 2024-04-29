package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
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
    private final SessionChecker sessionChecker = new SessionChecker();
    private final PaypalServices paypalServices;

    public PaypalServlet() throws PayPalRESTException {
        this.paypalServices = new PaypalServices();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = sessionChecker.getLoginSession(request.getSession());
        if (!session.isResult()) {
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
            JsonArray linksArray = new Gson().fromJson(paymentResponse, JsonObject.class).getAsJsonArray("links");
            String approval_url = null;
            for (JsonElement link : linksArray) {
                if (link.getAsJsonObject().get("rel").getAsString().equals("approval_url")) {
                    approval_url = link.getAsJsonObject().get("href").getAsString();
                }
            }
            response.sendRedirect(approval_url);
        } catch (JsonSyntaxException | IOException ex) {
            throw new PaymentException(ex.getMessage());
        }
    }

    public String successPay(String paymentId, String payerId) {
        Payment payment = paypalServices.executePayment(paymentId, payerId);
        System.out.println(payment.toJSON());
        if (payment.getState().equals("approved")) {
            return "success";
        }
        return "redirect:/";
    }
}
