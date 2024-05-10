package controllers.admins;

import entities.Constants;
import entities.Staffs;
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
import java.util.logging.Logger;
import utilities.RedirectUtilities;
import utilities.SecurityLog;

public class DeleteStaffServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
    private static final Logger LOG = Logger.getLogger(DeleteStaffServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userID = request.getParameter("userId");
        try {
            userTransaction.begin();
            Staffs staff = entityManager.find(Staffs.class, Integer.valueOf(userID));
            entityManager.remove(staff);
            userTransaction.commit();
            SecurityLog.addInternalSecurityLog(request, "Staff " + staff.getUsername() + " deleted successfully.");
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, "Staff " + staff.getUsername() + " deleted successfully.", Constants.ADMIN_SEARCH_STAFF_URL);
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | NumberFormatException | SecurityException ex) {
            SecurityLog.addInternalSecurityLog(request, "Failed to delete staff: " + userID + ".");
            LOG.severe(ex.getMessage());
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Failed to delete staff: " + userID + ".", Constants.ADMIN_SEARCH_STAFF_URL);
        }
    }
}
