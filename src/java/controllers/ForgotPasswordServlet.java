package controllers;

import domain.common.Common;
import domain.common.Constants;
import entities.Customers;
import entities.Resetpassword;
import features.AesHandler;
import features.MailHandler;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import listeners.ServerListener;
import services.ResetPasswordServices;
import utilities.RedirectUtilities;
import utilities.RedirectUtilities.RedirectType;
import utilities.StringUtilities;
import utilities.ValidationUtilities;

public class ForgotPasswordServlet extends HttpServlet {

    private final MailHandler mailHandler = new MailHandler();
    private final ResetPasswordServices resetPasswordServices = new ResetPasswordServices();
    private static final String subject = "Reset Password";
    private static final String body = "Click the link to reset your password: ";
    @PersistenceContext
    EntityManager entityManager;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(Constants.FORGOT_PASSWORD_JSP_URL).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        request.setAttribute("email", email);
        if (StringUtilities.anyNullOrBlank(email)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, email, Constants.FORGOT_PASSWORD_URL);
            return;
        }
        if (!ValidationUtilities.isEmailValid(email)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Invalid email", Constants.FORGOT_PASSWORD_URL);
            return;
        }
        List<Customers> customers = (List<Customers>) entityManager.createNamedQuery("Customers.findByEmail").setParameter("email", email).getResultList();
        if (customers == null || customers.isEmpty()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Email not found", Constants.FORGOT_PASSWORD_URL);
            return;
        }
        String resetPasswordURL = generateResetPasswordURL(request, email);
        if (resetPasswordURL == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Error generating reset password URL", Constants.FORGOT_PASSWORD_URL);
            return;
        }
        String content = body + resetPasswordURL;
        String jsonPayload = String.format("{\"toEmail\":\"%s\",\"subject\":\"%s\",\"body\":\"%s\"}", email, subject, content);
        Common.Status status = mailHandler.sendEmail(jsonPayload);
        if (status == Common.Status.OK) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.SUCCESS, "Email sent successfully", Constants.FORGOT_PASSWORD_URL);
        } else {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Email not sent", Constants.FORGOT_PASSWORD_URL);
        }
    }

    private String generateResetPasswordURL(HttpServletRequest request, String email) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String makeToken = email + timestamp;
        String token = AesHandler.aes256EcbEncrypt(makeToken).replace("/", "").replace("+", "").replace("=", "");
        Resetpassword resetPassword = new Resetpassword(email, token);
        boolean isAdded = resetPasswordServices.addNewResetPassword(resetPassword);
        if (!isAdded) {
            return null;
        }
        return ServerListener.getServerBaseURL(request) + "/pages/resetPassword?token=" + token;
    }
}
