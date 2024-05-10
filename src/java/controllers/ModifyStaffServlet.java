package controllers;

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
import utilities.SessionUtilities;

public class ModifyStaffServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
    private static final Logger LOG = Logger.getLogger(ModifyStaffServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SessionUtilities.checkIsStaffOrAdmin(request)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login as staff to view this page!", "/");
            return;
        }
        request.getRequestDispatcher("/pages/superAdmin/modifyStaff.jsp").forward(request, response);
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
        Staffs staff = new Staffs(userId, username, address, phoneNumber, gender);
        boolean isUpdated = updateStaffDetails(request, staff);
        if (!isUpdated) {
            showError(request, response);
            return;
        }
        RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.SUCCESS, "Update Successful!", Constants.ADMIN_MANAGE_STAFF_URL);
    }

    private boolean updateStaffDetails(HttpServletRequest request, Staffs staff) {
        try {
            userTransaction.begin();
            Staffs staffFromDB = entityManager.find(Staffs.class, staff.getUserId());
            if (staffFromDB == null) {
                return false;
            }
            staffFromDB.setAddress(staff.getAddress());
            staffFromDB.setGender(staff.getGender());
            staffFromDB.setPhoneNumber(staff.getPhoneNumber());
            staffFromDB.setUsername(staff.getUsername());
            entityManager.merge(staffFromDB);
            userTransaction.commit();
            SecurityLog.addInternalSecurityLog(request, "Staff: " + staff.getUsername() + " updated successfully.");
            return true;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | SecurityException ex) {
            SecurityLog.addInternalSecurityLog(request, "Failed to update staff: " + staff.getUserId() + ".");
            LOG.severe(ex.getMessage());
            return false;
        }
    }

    private void showError(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Invalid details, please re-enter it.", Constants.ADMIN_MODIFY_STAFF_URL);
    }
}
