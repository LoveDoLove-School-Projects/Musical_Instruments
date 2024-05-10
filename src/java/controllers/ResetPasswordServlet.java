package controllers;

import entities.Customers;
import entities.Resetpassword;
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
import utilities.StringUtilities;
import utilities.ValidationUtilities;

public class ResetPasswordServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
    private static final Logger LOG = Logger.getLogger(ResetPasswordServlet.class.getName());
    private static final String RESET_PASSWORD_JSP_URL = "/pages/resetPassword.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        String role = request.getParameter("role");
        if (token == null || role == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Invalid reset password link", "/");
            return;
        }
        if ((!role.equals("customer") && !role.equals("staff"))) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Invalid role", "/");
            return;
        }
        Resetpassword resetpassword = isTokenValid(token);
        if (resetpassword == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Invalid reset password link", "/");
            return;
        }
        request.setAttribute("token", token);
        request.setAttribute("email", resetpassword.getEmail());
        request.setAttribute("role", role);
        request.getRequestDispatcher(RESET_PASSWORD_JSP_URL).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        String token = request.getParameter("token");
        String role = request.getParameter("role");
        String newPassword = request.getParameter("newPassword");
        String confirmNewPassword = request.getParameter("confirmNewPassword");
        if (!validateResetPasswordRequest(token, role, newPassword, confirmNewPassword)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Invalid reset password request", "/");
            return;
        }
        Resetpassword resetPassword = isTokenValid(token);
        boolean isUpdated = false;
        switch (role) {
            case "customer":
                isUpdated = resetCustomerPassword(resetPassword.getEmail(), newPassword);
                break;
            case "staff":
                isUpdated = resetStaffPassword(resetPassword.getEmail(), newPassword);
                break;
        }
        if (!isUpdated) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Failed to reset password", "/");
            return;
        }
        removeResetPassword(resetPassword);
        RedirectUtilities.redirectWithMessage(request, response, RedirectType.SUCCESS, "Password reset successfully", "/");
    }

    private void removeResetPassword(Resetpassword resetPassword) {
        try {
            userTransaction.begin();
            Resetpassword managedResetPassword = entityManager.merge(resetPassword);
            entityManager.remove(managedResetPassword);
            userTransaction.commit();
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | SecurityException ex) {
            LOG.severe(ex.getMessage());
        }
    }

    private boolean resetCustomerPassword(String email, String newPassword) {
        try {
            userTransaction.begin();
            List<Customers> customers = (List<Customers>) entityManager.createNamedQuery("Customers.findByEmail").setParameter("email", email).getResultList();
            if (customers == null || customers.isEmpty()) {
                return false;
            }
            Customers customer = customers.get(0);
            customer.setPassword(AesUtilities.aes256EcbEncrypt(newPassword));
            entityManager.merge(customer);
            userTransaction.commit();
            return true;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | SecurityException ex) {
            LOG.severe(ex.getMessage());
            return false;
        }
    }

    private boolean resetStaffPassword(String email, String newPassword) {
        try {
            userTransaction.begin();
            List<Staffs> staffs = (List<Staffs>) entityManager.createNamedQuery("Staffs.findByEmail").setParameter("email", email).getResultList();
            if (staffs == null || staffs.isEmpty()) {
                return false;
            }
            Staffs staff = staffs.get(0);
            staff.setPassword(AesUtilities.aes256EcbEncrypt(newPassword));
            entityManager.merge(staff);
            userTransaction.commit();
            return true;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | SecurityException ex) {
            LOG.severe(ex.getMessage());
            return false;
        }
    }

    private boolean validateResetPasswordRequest(String token, String role, String newPassword, String confirmNewPassword) {
        if (StringUtilities.anyNullOrBlank(token, newPassword, confirmNewPassword)) {
            return false;
        }
        if (!ValidationUtilities.comparePasswords(newPassword, confirmNewPassword)) {
            return false;
        }
        if (role == null || (!role.equals("customer") && !role.equals("staff"))) {
            return false;
        }
        return newPassword.length() >= 8;
    }

    private Resetpassword isTokenValid(String token) {
        List<Resetpassword> resetpasswords = entityManager.createNamedQuery("Resetpassword.findByToken").setParameter("token", token).getResultList();
        if (resetpasswords == null || resetpasswords.isEmpty()) {
            return null;
        }
        return resetpasswords.get(0);
    }
}
