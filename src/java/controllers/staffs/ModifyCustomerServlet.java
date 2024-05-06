package controllers.staffs;

import common.Constants;
import entities.Customers;
import exceptions.DatabaseException;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import java.io.IOException;
import utilities.RedirectUtilities;
import utilities.SessionUtilities;

public class ModifyCustomerServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtilities.checkIsStaffOrAdmin(request)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login as staff to view this page!", "/");
            return;
        }
        request.getRequestDispatcher("/pages/admins/modifyCustomer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userIdString = request.getParameter("userId");
        String username = request.getParameter("username");
        String gender = request.getParameter("gender");
        String address = request.getParameter("address");
        String phoneNumber = request.getParameter("phoneNumber");
        if (userIdString == null || userIdString.isEmpty()) {
            showError(request, response);
            return;
        }
        int userId = Integer.parseInt(userIdString);
        Customers customer = new Customers(userId, username, address, phoneNumber, gender);
        boolean isUpdated = updateCustomerDetails(customer);
        if (!isUpdated) {
            showError(request, response);
            return;
        }
        RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, "Update Successful!", Constants.ADMIN_MANAGE_CUSTOMER_URL);
    }

    private boolean updateCustomerDetails(Customers customer) {
        try {
            userTransaction.begin();
            Customers customerFromDB = entityManager.find(Customers.class, customer.getUserId());
            if (customerFromDB == null) {
                return false;
            }
            customerFromDB.setAddress(customer.getAddress());
            customerFromDB.setGender(customer.getGender());
            customerFromDB.setPhoneNumber(customer.getPhoneNumber());
            customerFromDB.setUsername(customer.getUsername());
            entityManager.merge(customerFromDB);
            userTransaction.commit();
            return true;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | SecurityException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    private void showError(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Invalid details, please re-enter it.", "/pages/admins/modifyCustomer.jsp");
    }
}
