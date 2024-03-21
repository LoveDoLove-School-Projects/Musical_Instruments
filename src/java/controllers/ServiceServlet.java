package controllers;

import utilities.JsonUtilities;
import utilities.StringUtilities;
import domain.common.Common;
import domain.common.Constants;
import domain.request.MailRequest;
import domain.response.MailResponse;
import domain.response.OtpResponse;
import services.MailServices;
import services.OtpServices;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServiceServlet extends HttpServlet {

    private static final OtpServices OTP_SERVICES = new OtpServices();
    private static final MailServices MAIL_SERVICES = new MailServices();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleService(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleService(request, response);
    }

    private void handleService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            String path = request.getServletPath();
            switch (path) {
                case Constants.SERVICE_SEND_EMAIL_URL:
                    sendEmail(request, response);
                    return;
                case Constants.SERVICE_SEND_OTP_URL:
                    sendOtp(request, response);
                    return;
            }
        }
        JsonUtilities.sendErrorResponse(response, "Invalid request!");
    }

    private void sendOtp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        if (StringUtilities.anyNullOrBlank(email)) {
            JsonUtilities.sendErrorResponse(response, "Please fill in the email field!");
            return;
        }
        OtpResponse otpResponse = OTP_SERVICES.sendOtp(email);
        if (otpResponse.getStatus() != Common.Status.OK) {
            JsonUtilities.sendErrorResponse(response, "Failed to send OTP!");
            return;
        }
        JsonUtilities.sendSuccessResponse(response, "OTP Sent Successfully!");
    }

    private void sendEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String subject = request.getParameter("subject");
        String body = request.getParameter("body");

        if (StringUtilities.anyNullOrBlank(email, subject, body)) {
            JsonUtilities.sendErrorResponse(response, "Email, subject and body must not be empty!");
            return;
        }
        MailRequest mailRequest = new MailRequest(email, subject, body);
        MailResponse mailResponse = MAIL_SERVICES.sendEmail(mailRequest);
        if (mailResponse.getStatus() != Common.Status.OK) {
            JsonUtilities.sendErrorResponse(response, "Failed to send email!");
            return;
        }
        JsonUtilities.sendSuccessResponse(response, "Email Sent Successfully!");
    }

}
