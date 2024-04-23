package controllers;

import domain.common.Common;
import domain.common.Constants;
import features.AesHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import listeners.ServerListener;
import features.MailHandler;
import utilities.RedirectUtilities;
import utilities.RedirectUtilities.RedirectType;
import utilities.StringUtilities;

public class ForgotPasswordServlet extends HttpServlet {

    private final MailHandler mailServices = new MailHandler();
    private static final String subject = "Reset Password";
    private static final String body = "Click the link to reset your password: ";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setForgotPasswordPage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        if (StringUtilities.anyNullOrBlank(email)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, email, Constants.FORGOT_PASSWORD_URL);
            return;
        }
        String resetPasswordURL = generateResetPasswordURL(request, email);
        if (resetPasswordURL == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Error generating reset password URL", Constants.FORGOT_PASSWORD_URL);
            return;
        }
        String content = body + resetPasswordURL;
        String jsonPayload = String.format("{\"toEmail\":\"%s\",\"subject\":\"%s\",\"body\":\"%s\"}", email, subject, content);
        Common.Status status = mailServices.sendEmail(jsonPayload);
        if (status == Common.Status.OK) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.SUCCESS, "Email sent successfully", Constants.FORGOT_PASSWORD_URL);
        } else {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Email not sent", Constants.FORGOT_PASSWORD_URL);
        }
    }

    private String generateResetPasswordURL(HttpServletRequest request, String email) {
        String token = AesHandler.aes256EcbEncrypt(email);
        return ServerListener.getServerBaseURL(request) + "/pages/resetPassword?token=" + token;
    }

    private void setForgotPasswordPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(Constants.FORGOT_PASSWORD_JSP_URL).forward(request, response);
    }
}
