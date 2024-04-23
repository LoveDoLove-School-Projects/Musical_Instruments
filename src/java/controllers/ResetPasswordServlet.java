package controllers;

import domain.common.Constants;
import entities.Customers;
import entities.Resetpassword;
import features.AesHandler;
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
import utilities.RedirectUtilities;
import utilities.RedirectUtilities.RedirectType;
import utilities.StringUtilities;
import utilities.ValidationUtilities;

public class ResetPasswordServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        System.out.println(token);
        if (token == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Invalid reset password link", Constants.MAIN_URL);
            return;
        }
        Resetpassword resetpassword = isTokenValid(token);
        if (resetpassword == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Invalid reset password link", Constants.MAIN_URL);
            return;
        }
        request.setAttribute("token", token);
        request.setAttribute("email", resetpassword.getEmail());
        request.getRequestDispatcher(Constants.RESET_PASSWORD_JSP_URL).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        String newPassword = request.getParameter("newPassword");
        String confirmNewPassword = request.getParameter("confirmNewPassword");
        if (!validateResetPasswordRequest(token, newPassword, confirmNewPassword)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Invalid reset password request", Constants.MAIN_URL);
            return;
        }
        List<Resetpassword> resetPasswords = (List<Resetpassword>) entityManager.createNamedQuery("Resetpassword.findByToken").setParameter("token", token).getResultList();
        if (resetPasswords == null || resetPasswords.isEmpty()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Invalid reset password link", Constants.MAIN_URL);
            return;
        }
        Resetpassword resetPassword = resetPasswords.get(0);
        List<Customers> customers = (List<Customers>) entityManager.createNamedQuery("Customers.findByEmail").setParameter("email", resetPassword.getEmail()).getResultList();
        if (customers == null || customers.isEmpty()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Email not found", Constants.MAIN_URL);
            return;
        }
        Customers customer = customers.get(0);
        customer.setPassword(AesHandler.aes256EcbEncrypt(newPassword));
        try {
            userTransaction.begin();
            entityManager.merge(customer);
            entityManager.remove(resetPassword);
            userTransaction.commit();
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | SecurityException ex) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "An error occurred while resetting password", Constants.MAIN_URL);
            return;
        }
        RedirectUtilities.redirectWithMessage(request, response, RedirectType.SUCCESS, "Password reset successfully", Constants.CUSTOMER_LOGIN_URL);
    }

    private boolean validateResetPasswordRequest(String token, String newPassword, String confirmNewPassword) {
        if (StringUtilities.anyNullOrBlank(token, newPassword, confirmNewPassword)) {
            return false;
        }
        if (!ValidationUtilities.comparePasswords(newPassword, confirmNewPassword)) {
            return false;
        }
        if (newPassword.length() < 8) {
            return false;
        }
        return true;
    }

    private Resetpassword isTokenValid(String token) {
        try {
            List<Resetpassword> resetpasswords = (List<Resetpassword>) entityManager.createNamedQuery("Resetpassword.findByToken").setParameter("token", token).getResultList();
            Resetpassword resetpassword = resetpasswords.get(0);
            return resetpassword;
        } catch (Exception ex) {
            return null;
        }
    }
}
