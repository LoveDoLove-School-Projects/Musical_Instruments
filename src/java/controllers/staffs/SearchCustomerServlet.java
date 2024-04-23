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
import services.StaffServices;
import utilities.RedirectUtilities;

public class SearchCustomerServlet extends HttpServlet {

    private final StaffServices staffServices = new StaffServices();
    @PersistenceContext
    EntityManager entityManager;

    @Resource
    UserTransaction userTransaction;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String searchQuery = request.getParameter("searchQuery");

        if (searchQuery.isBlank() || searchQuery.trim().isEmpty()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please fill in the customer email!", Constants.ADMIN_MANAGE_CUSTOMER_JSP_URL);
            return;
        }

        Customers customersList = staffServices.searchCustomerDetailsDB(searchQuery);

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
