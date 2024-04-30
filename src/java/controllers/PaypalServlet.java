package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
import java.util.logging.Logger;
import services.PaypalServices;
import utilities.RedirectUtilities;

@WebServlet(name = "PaypalServlet", urlPatterns = {"/payments/paypal", "/payments/paypal/execute"})
public class PaypalServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    private static final Logger LOG = Logger.getLogger(PaypalServlet.class.getName());
    private final SessionChecker sessionChecker = new SessionChecker();
    private final PaypalServices paypalServices = new PaypalServices();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = sessionChecker.getLoginSession(request.getSession(false));
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
            String paypalResponse = paypalServices.createPayment(cartList);
            LOG.info(paypalResponse);
            HttpSession httpSession = request.getSession(false);
            httpSession.setAttribute("paypalResponse", paypalResponse);
            JsonArray linksArray = new Gson().fromJson(paypalResponse, JsonObject.class).getAsJsonArray("links");
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
}
