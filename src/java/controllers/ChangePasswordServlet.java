package controllers;

import entities.Customers;
import entities.Session;
import entities.Staffs;
import features.AesProtector;
import features.SecurityLog;
import features.SessionChecker;
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
import utilities.RedirectUtilities;
import utilities.RedirectUtilities.RedirectType;
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
        Session session = SessionChecker.getLoginSession(request.getSession());
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
        Session session = SessionChecker.getLoginSession(request.getSession());
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
        if (!checkCurrentPassword(session, curentPassword)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Password incorrect", "/");
            return;
        }
        try {
            userTransaction.begin();
            if (!updateNewPassword(session, newPassword)) {
                RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "An error occurred while resetting password", "/");
                return;
            }
            userTransaction.commit();
            SecurityLog.addSecurityLog(request, "Password changed successfully");
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.SUCCESS, "Password reset successfully", "/");
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IOException | IllegalStateException | SecurityException ex) {
            LOG.severe(ex.getMessage());
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "An error occurred while resetting password", "/");
        }
    }

    private boolean updateNewPassword(Session session, String newPassword) {
        try {
            switch (session.getRole()) {
                case CUSTOMER:
                    List<Customers> customers = (List<Customers>) entityManager.createNamedQuery("Customers.findByUserId", Customers.class).setParameter("userId", session.getUserId()).getResultList();
                    if (customers == null || customers.isEmpty()) {
                        return false;
                    }
                    Customers customer = customers.get(0);
                    customer.setPassword(AesProtector.aes256EcbEncrypt(newPassword));
                    entityManager.merge(customer);
                    return true;
                case STAFF:
                    List<Staffs> staffs = (List<Staffs>) entityManager.createNamedQuery("Staffs.findByUserId", Staffs.class).setParameter("userId", session.getUserId()).getResultList();
                    if (staffs == null || staffs.isEmpty()) {
                        return false;
                    }
                    Staffs staff = staffs.get(0);
                    staff.setPassword(AesProtector.aes256EcbEncrypt(newPassword));
                    entityManager.merge(staff);
                    return true;
                default:
                    return false;
            }
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
            return false;
        }
    }

    private boolean checkCurrentPassword(Session session, String currentPassword) {
        try {
            switch (session.getRole()) {
                case CUSTOMER:
                    List<Customers> customers = (List<Customers>) entityManager.createNamedQuery("Customers.findByUserId", Customers.class).setParameter("userId", session.getUserId()).getResultList();
                    if (customers == null || customers.isEmpty()) {
                        return false;
                    }
                    Customers customer = customers.get(0);
                    return AesProtector.aes256EcbDecrypt(customer.getPassword()).equals(currentPassword);
                case STAFF:
                    List<Staffs> staffs = (List<Staffs>) entityManager.createNamedQuery("Staffs.findByUserId", Staffs.class).setParameter("userId", session.getUserId()).getResultList();
                    if (staffs == null || staffs.isEmpty()) {
                        return false;
                    }
                    Staffs staff = staffs.get(0);
                    return AesProtector.aes256EcbDecrypt(staff.getPassword()).equals(currentPassword);
                default:
                    return false;
            }
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
            return false;
        }
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
