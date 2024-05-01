package controllers;

import common.Constants;
import entities.Customers;
import entities.Resetpassword;
import entities.Staffs;
import exceptions.DatabaseException;
import features.AesProtector;
import features.MailSender;
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

public class ForgotPasswordServlet extends HttpServlet {

    private static final String FORGOT_PASSWORD_JSP_URL = "/pages/forgotPassword.jsp";
    private static final String FORGOT_PASSWORD_URL = "/pages/forgotPassword";
    private static final String SUBJECT = "Reset Password";
    private static final String CONTENT = "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width,initial-scale=1'><title>Reset Password Design</title><style>body{font-family:Arial,sans-serif}.container{width:100%;max-width:600px;margin:0 auto}.card{border:1px solid #ddd;border-radius:5px;margin-top:50px;padding:20px;text-align:center}.card-title{font-size:24px;margin-bottom:20px}.card-text{font-size:18px;margin-bottom:20px}.btn{display:inline-block;color:#fff;background-color:#007bff;border-color:#007bff;padding:.375rem .75rem;font-size:1rem;line-height:1.5;border-radius:.25rem;text-decoration:none}.btn:hover{background-color:#0056b3}</style></head><body><div class='container'><div class='card'><h5 class='card-title'>Reset Your Password</h5><p class='card-text'>You requested to reset your password. Click the button below to continue.</p><a href='${resetPasswordLink}' class='btn'>Reset Password</a><p class='card-text'>If the button doesn't work, you can also use the following link to reset your password: <a href='${resetPasswordLink}'>${resetPasswordLink}</a></p></div></div></body></html>";
    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String role = request.getParameter("role");
        request.setAttribute("role", role);
        request.getRequestDispatcher(FORGOT_PASSWORD_JSP_URL).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        String email = request.getParameter("email");
        String role = request.getParameter("role");
        request.setAttribute("email", email);
        request.setAttribute("role", role);
        if (StringUtilities.anyNullOrBlank(email)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, email, FORGOT_PASSWORD_URL);
            return;
        }
        if (!ValidationUtilities.isEmailValid(email)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Invalid email", FORGOT_PASSWORD_URL);
            return;
        }
        switch (role) {
            case "customer":
                List<Customers> customers = entityManager.createNamedQuery("Customers.findByEmail", Customers.class).setParameter("email", email).getResultList();
                if (customers == null || customers.isEmpty()) {
                    RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Email not found", FORGOT_PASSWORD_URL);
                    return;
                }
                break;
            case "staff":
                List<Staffs> staffs = entityManager.createNamedQuery("Staffs.findByEmail", Staffs.class).setParameter("email", email).getResultList();
                if (staffs == null || staffs.isEmpty()) {
                    RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Email not found", FORGOT_PASSWORD_URL);
                    return;
                }
                break;
            default:
                RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Invalid role", "/");
                return;
        }
        String resetPasswordURL = generateResetPasswordURL(request, email, role);
        if (resetPasswordURL == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Error generating reset password URL", FORGOT_PASSWORD_URL);
            return;
        }
        boolean status = MailSender.sendEmail(email, SUBJECT, CONTENT.replace("${resetPasswordLink}", resetPasswordURL));
        if (status) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.SUCCESS, "Email sent successfully", FORGOT_PASSWORD_URL);
        } else {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Email not sent", FORGOT_PASSWORD_URL);
        }
    }

    private String generateResetPasswordURL(HttpServletRequest request, String email, String role) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String makeToken = email + timestamp;
        String token = AesProtector.aes256EcbEncrypt(makeToken).replace("/", "").replace("+", "").replace("=", "");
        Resetpassword resetPassword = new Resetpassword(email, token);
        boolean isAdded = addNewResetPassword(resetPassword);
        if (!isAdded) {
            return null;
        }
        return InitServlet.getServerBaseURL(request) + "/pages/resetPassword?token=" + token + "&role=" + role;
    }

    private boolean addNewResetPassword(Resetpassword resetPassword) {
        try {
            userTransaction.begin();
            Resetpassword existingResetPassword = entityManager.find(Resetpassword.class, resetPassword.getEmail());
            if (existingResetPassword == null) {
                entityManager.persist(resetPassword);
            } else {
                existingResetPassword.setToken(resetPassword.getToken());
                entityManager.merge(existingResetPassword);
            }
            userTransaction.commit();
            return true;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | SecurityException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }
}
