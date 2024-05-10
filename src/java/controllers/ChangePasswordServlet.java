package controllers;

import entities.Customers;
import entities.Session;
import entities.Staffs;
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
import java.util.List;
import java.util.logging.Logger;
import utilities.AesUtilities;
import utilities.RedirectUtilities;
import utilities.RedirectUtilities.RedirectType;
import utilities.SecurityLog;
import utilities.SessionUtilities;
import utilities.StringUtilities;
import utilities.ValidationUtilities;

public class ChangePasswordServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
    private static final Logger LOG = Logger.getLogger(ChangePasswordServlet.class.getName());
    private static final String CHANGE_PASSWORD_JSP_URL = "/pages/changePassword.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Session session = SessionUtilities.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Please login to view this page.", "/");
            return;
        }
        request.getRequestDispatcher(CHANGE_PASSWORD_JSP_URL).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        Session session = SessionUtilities.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectUtilities.RedirectType.DANGER, "Please login to view this page.", "/");
            return;
        }
        String curentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmNewPassword = request.getParameter("confirmNewPassword");
        if (!validateChangePasswordRequest(curentPassword, newPassword, confirmNewPassword)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Invalid input", "/");
            return;
        }
        boolean isCorrectCurrentPassword = false;
        switch (session.getRole()) {
            case CUSTOMER:
                isCorrectCurrentPassword = checkCustomerCurrentPassword(session.getUserId(), curentPassword);
                break;
            case STAFF:
                isCorrectCurrentPassword = checkStaffCurrentPassword(session.getUserId(), curentPassword);
                break;
            default:
                RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Invalid role", "/");
                break;
        }
        if (!isCorrectCurrentPassword) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Current password is incorrect", "/");
            return;
        }
        boolean isPasswordChanged = false;
        switch (session.getRole()) {
            case CUSTOMER:
                isPasswordChanged = updateCustomerNewPassword(request, session, newPassword);
                break;
            case STAFF:
                isPasswordChanged = updateStaffNewPassword(request, session, newPassword);
                break;
            default:
                RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Invalid role", "/");
                break;
        }
        if (!isPasswordChanged) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Failed to change password", "/");
            return;
        }
        RedirectUtilities.redirectWithMessage(request, response, RedirectType.SUCCESS, "Password reset successfully", "/");
    }

    private boolean updateCustomerNewPassword(HttpServletRequest request, Session session, String newPassword) {
        try {
            userTransaction.begin();
            List<Customers> customers = (List<Customers>) entityManager.createNamedQuery("Customers.findByUserId", Customers.class).setParameter("userId", session.getUserId()).getResultList();
            if (customers == null || customers.isEmpty()) {
                return false;
            }
            Customers customer = customers.get(0);
            customer.setPassword(AesUtilities.aes256EcbEncrypt(newPassword));
            entityManager.merge(customer);
            userTransaction.commit();
            SecurityLog.addSecurityLog(request, "Customer: " + customer.getUsername() + " has reset password.");
            return true;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | SecurityException ex) {
            SecurityLog.addSecurityLog(request, "Failed to change password for customer: " + session.getUserId() + ".");
            LOG.severe(ex.getMessage());
            return false;
        }
    }

    private boolean updateStaffNewPassword(HttpServletRequest request, Session session, String newPassword) {
        try {
            userTransaction.begin();
            List<Staffs> staffs = (List<Staffs>) entityManager.createNamedQuery("Staffs.findByUserId", Staffs.class).setParameter("userId", session.getUserId()).getResultList();
            if (staffs == null || staffs.isEmpty()) {
                return false;
            }
            Staffs staff = staffs.get(0);
            staff.setPassword(AesUtilities.aes256EcbEncrypt(newPassword));
            entityManager.merge(staff);
            userTransaction.commit();
            SecurityLog.addSecurityLog(request, "Staff: " + staff.getUsername() + " has reset password.");
            return true;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | SecurityException ex) {
            SecurityLog.addSecurityLog(request, "Failed to change password for staff: " + session.getUserId() + ".");
            LOG.severe(ex.getMessage());
            return false;
        }
    }

    private boolean checkCustomerCurrentPassword(int userId, String currentPassword) {
        List<Customers> customers = (List<Customers>) entityManager.createNamedQuery("Customers.findByUserId", Customers.class).setParameter("userId", userId).getResultList();
        if (customers == null || customers.isEmpty()) {
            return false;
        }
        Customers customer = customers.get(0);
        return AesUtilities.aes256EcbDecrypt(customer.getPassword()).equals(currentPassword);
    }

    private boolean checkStaffCurrentPassword(int userId, String currentPassword) {
        List<Staffs> staffs = (List<Staffs>) entityManager.createNamedQuery("Staffs.findByUserId", Staffs.class).setParameter("userId", userId).getResultList();
        if (staffs == null || staffs.isEmpty()) {
            return false;
        }
        Staffs staff = staffs.get(0);
        return AesUtilities.aes256EcbDecrypt(staff.getPassword()).equals(currentPassword);
    }

    private boolean validateChangePasswordRequest(String currentPassword, String newPassword, String confirmNewPassword) {
        if (StringUtilities.anyNullOrBlank(currentPassword, newPassword, confirmNewPassword)) {
            return false;
        }
        if (!ValidationUtilities.comparePasswords(newPassword, confirmNewPassword)) {
            return false;
        }
        return newPassword.length() >= 8;
    }
}
