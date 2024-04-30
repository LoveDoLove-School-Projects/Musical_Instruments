package controllers.staffs;

import common.Constants;
import entities.Customers;
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
import java.util.List;
import utilities.RedirectUtilities;

public class SearchCustomerServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    private final SessionChecker sessionChecker = new SessionChecker();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession httpSession = request.getSession(false);
        boolean isAdmin = sessionChecker.getIsAdminOrNot(request);
        Session session = sessionChecker.getLoginSession(httpSession);
        boolean isLoggedIn = session.isResult();
        if (!isAdmin && (!isLoggedIn || session.getRole() != Role.STAFF)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login as staff to view this page!", "/");
            return;
        }
        request.getRequestDispatcher("/pages/staffs/searchCustomer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String searchQuery = request.getParameter("searchQuery");
        if (searchQuery.isBlank() || searchQuery.trim().isEmpty()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please fill in the customer email!", Constants.ADMIN_SEARCH_CUSTOMER_JSP_URL);
            return;
        }
        List<Customers> customerList = entityManager.createNamedQuery("Customers.findByEmail", Customers.class).setParameter("email", searchQuery).getResultList();
        if (customerList == null || customerList.isEmpty()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Email address does not exist!", Constants.ADMIN_SEARCH_CUSTOMER_JSP_URL);
            return;
        }
        Customers customer = customerList.get(0);
        HttpSession session = request.getSession();
        session.setAttribute("customerDetails", customer);
        RedirectUtilities.sendRedirect(request, response, "/pages/staffs/manageCustomer");
    }
}
