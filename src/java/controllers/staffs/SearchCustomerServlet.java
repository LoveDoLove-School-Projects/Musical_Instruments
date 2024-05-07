package controllers.staffs;

import entities.Constants;
import entities.Customers;
import utilities.SessionUtilities;
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtilities.checkIsStaffOrAdmin(request)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login as staff to view this page!", "/");
            return;
        }
        List<Customers> customerList = entityManager.createNamedQuery("Customers.findAll", Customers.class).getResultList();
        request.setAttribute("customerCount", customerList.size());
        request.getRequestDispatcher("/pages/staffs/searchCustomer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String searchQuery = request.getParameter("searchQuery");
        if (searchQuery.isBlank() || searchQuery.trim().isEmpty()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please fill in the customer email!", Constants.ADMIN_SEARCH_CUSTOMER_URL);
            return;
        }
        List<Customers> customerList = entityManager.createNamedQuery("Customers.findByEmail", Customers.class).setParameter("email", searchQuery).getResultList();
        if (customerList == null || customerList.isEmpty()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Email address does not exist!", Constants.ADMIN_SEARCH_CUSTOMER_URL);
            return;
        }
        Customers customer = customerList.get(0);
        HttpSession session = request.getSession();
        session.setAttribute("customerDetails", customer);
        RedirectUtilities.sendRedirect(request, response, "/pages/staffs/manageCustomer");
    }
}
