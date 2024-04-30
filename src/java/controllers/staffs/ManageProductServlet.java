package controllers.staffs;

import entities.Products;
import entities.Role;
import entities.Session;
import features.SessionChecker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;
import utilities.RedirectUtilities;

public class ManageProductServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(ManageProductServlet.class.getName());
    @PersistenceContext
    EntityManager entityManager;
    private final SessionChecker sessionChecker = new SessionChecker();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        boolean isAdmin = sessionChecker.getIsAdminOrNot(request);
        Session session = sessionChecker.getLoginSession(httpSession);
        boolean isLoggedIn = session.isResult();
        if (!isAdmin && (!isLoggedIn || session.getRole() != Role.STAFF)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login as staff to view this page!", "/");
            return;
        }
        response.setContentType("text/html;charset=UTF-8");
        Products product = (Products) httpSession.getAttribute("productDetails");
        product = entityManager.find(Products.class, product.getProductId());
        httpSession.setAttribute("productDetails", product);
        request.getRequestDispatcher("/pages/staffs/manageProduct.jsp").forward(request, response);
    }
}
