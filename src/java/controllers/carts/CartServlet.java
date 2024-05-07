package controllers.carts;

import entities.Constants;
import entities.Carts;
import entities.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import utilities.RedirectUtilities;
import utilities.SessionUtilities;

public class CartServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = SessionUtilities.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        if (SessionUtilities.checkIsStaffOrAdmin(request)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Admin or staff are not allowed to view this page because it only for customer.", "/");
            return;
        }
        List<Carts> carts = entityManager.createNamedQuery("Carts.findByCustomerId").setParameter("customerId", session.getUserId()).getResultList();
        if (carts == null || carts.isEmpty()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Your card is empty !", Constants.PRODUCT_URL);
        } else {
            request.setAttribute("cartDetails", carts);
            request.getRequestDispatcher(Constants.CART_JSP_URL).forward(request, response);
        }
    }
}
