package controllers.staffs;

import domain.common.Constants;
import entities.Customers;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.UserTransaction;
import java.io.IOException;
import java.util.List;
import utilities.RedirectUtilities;

public class SearchCustomerServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;

    @Resource
    UserTransaction userTransaction;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        // Get the search query parameter from the request
        String searchQuery = request.getParameter("searchQuery");

        if (searchQuery.isBlank() || searchQuery.trim().isEmpty()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please fill in the customer email!", Constants.ADMIN_MANAGE_CUSTOMER_JSP_URL);
            return;
        }
        // Create an instance of Customer to perform search
        List<Customers> customersList = entityManager.createNamedQuery("Customers.findByEmail").setParameter("email", searchQuery).getResultList();
        if (customersList == null || customersList.isEmpty()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Customer not found!", Constants.ADMIN_MANAGE_CUSTOMER_JSP_URL);
            return;
        }

        // When found data
        HttpSession session = request.getSession();
        session.setAttribute("customersList", customersList);
        RedirectUtilities.sendRedirect(request, response, "/pages/staffs/showCustomerDetails.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Search Customer Servlet";
    }
}
