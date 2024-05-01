package controllers.staffs;

import common.Constants;
import entities.Customers;
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
import java.util.logging.Logger;
import utilities.RedirectUtilities;

public class ManageCustomerServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(ManageCustomerServlet.class.getName());
    @PersistenceContext
    EntityManager entityManager;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionChecker.checkIsStaffOrAdmin(request)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login as staff to view this page!", "/");
            return;
        }
        HttpSession session = request.getSession();
        response.setContentType("text/html;charset=UTF-8");
        Customers customer = (Customers) session.getAttribute("customerDetails");
        if (customer == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please fill in the customer email!", Constants.ADMIN_SEARCH_CUSTOMER_URL);
            return;
        }
        List<Customers> customerList = entityManager.createNamedQuery("Customers.findByEmail", Customers.class).setParameter("email", customer.getEmail()).getResultList();
        if (customerList == null || customerList.isEmpty()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Email address does not exist!", Constants.ADMIN_SEARCH_CUSTOMER_URL);
            return;
        }
        customer = customerList.get(0);
        session.removeAttribute("customerDetails");
        session.setAttribute("customerDetails", customer);
        request.getRequestDispatcher("/pages/staffs/manageCustomer.jsp").forward(request, response);
    }
}
