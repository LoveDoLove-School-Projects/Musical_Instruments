package controllers.carts;

import common.Constants;
import entities.Carts;
import entities.Session;
import features.SessionChecker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import utilities.RedirectUtilities;

public class CartServlet extends HttpServlet {

    private static final SessionChecker sessionHandler = new SessionChecker();
    @PersistenceContext
    EntityManager entityManager;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Session session = sessionHandler.getLoginSession(request.getSession());
        if (!session.isResult()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        List<Carts> carts = entityManager.createNamedQuery("Carts.findByCustomerId").setParameter("customerId", session.getUserId()).getResultList();
        if (carts == null || carts.isEmpty()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Product Not Found!", Constants.PRODUCT_URL);
            return;
        } else {
            request.setAttribute("cartDetails", carts);
            request.getRequestDispatcher(Constants.CART_JSP_URL).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
