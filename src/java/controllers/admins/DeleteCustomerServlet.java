package controllers.admins;

import entities.Constants;
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
import utilities.SecurityLog;

public class DeleteCustomerServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userID = request.getParameter("userId");
        try {
            userTransaction.begin();
            Customers customer = entityManager.find(Customers.class, Integer.valueOf(userID));
            entityManager.remove(customer);
            userTransaction.commit();
            SecurityLog.addInternalSecurityLog(request, "Customer: " + customer.getUsername() + " deleted successfully.");
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, customer.getUsername() + " deleted successfully.", Constants.ADMIN_SEARCH_CUSTOMER_URL);
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | NumberFormatException | SecurityException ex) {
            SecurityLog.addInternalSecurityLog(request, "Failed to delete customer: " + userID + ".");
            throw new DatabaseException(ex.getMessage());
        }
    }
}
