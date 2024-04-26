package controllers.staffs;

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
import java.util.logging.Logger;
import services.StaffServices;
import utilities.RedirectUtilities;

public class ModifyCustomerServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(ModifyCustomerServlet.class.getName());

    private final StaffServices staffServices = new StaffServices();
    private static final String SHOW_CUSTOMER_DETAILS_URL = "/pages/staffs/manageCustomer";

    @PersistenceContext
    EntityManager entityManager;

    @Resource
    UserTransaction userTransaction;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userIdString = request.getParameter("userId");
        String username = request.getParameter("username");
        String gender = request.getParameter("gender");
        String address = request.getParameter("address");
        String phoneNumber = request.getParameter("phoneNumber");

        if (userIdString == null || userIdString.isEmpty()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Invalid detail, please reenter it.", "/pages/staffs/modifyCustomer.jsp");
            return;
        }
        int userId = Integer.parseInt(userIdString);
        Customers customer = new Customers(userId, username, address, phoneNumber, gender);

        boolean isUpdated = staffServices.modifyCustomerDetailsDB(customer);
        if (!isUpdated) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Invalid detail, please reenter it.", "/pages/staffs/modifyCustomer.jsp");
            return;
        }
        Customers customersList = staffServices.searchCustomerDetailsDB(userId);
        HttpSession session = request.getSession();
        session.setAttribute("customersList", customersList);
        LOG.info(customersList.toString());
        RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, "Update Successful!", SHOW_CUSTOMER_DETAILS_URL);
    }
}
