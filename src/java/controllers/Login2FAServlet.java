package controllers;

import common.Constants;
import entities.OtpsType;
import entities.Role;
import entities.Session;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import services.OtpServices;
import utilities.RedirectUtilities;
import utilities.RedirectUtilities.RedirectType;
import utilities.SecurityLog;
import utilities.SessionUtilities;
import utilities.StringUtilities;

public class Login2FAServlet extends HttpServlet {

    private static final String LOGIN_2FA_JSP_URL = "/sessions/login2fa.jsp";
    private static final String LOGIN_2FA_URL = "/sessions/login2fa";
    private static final String RESEND_LOGIN_OTP_URL = "/sessions/resendLoginOtp";
    private static final Map<OtpsType, String> STATUS_MESSAGES;

    static {
        STATUS_MESSAGES = new EnumMap<>(OtpsType.class);
        STATUS_MESSAGES.put(OtpsType.NOT_FOUND, "OTP not found!");
        STATUS_MESSAGES.put(OtpsType.UNAUTHORIZED, "Too many attempts! Please click on 'Resend OTP' to try again.");
        STATUS_MESSAGES.put(OtpsType.EXPIRED, "OTP expired! Please click on 'Resend OTP' to try again.");
        STATUS_MESSAGES.put(OtpsType.FAILED, "Failed to verify OTP! Please try again.");
        STATUS_MESSAGES.put(OtpsType.INVALID, "Invalid OTP!");
    }
    private final OtpServices otpServices = new OtpServices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Session attributes = getSessionAttributes(session);
        if (attributes == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Invalid request!", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        setLogin2FAPage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        HttpSession session = request.getSession();
        Session attributes = getSessionAttributes(session);
        if (attributes == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Invalid request!", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        switch (request.getServletPath()) {
            case RESEND_LOGIN_OTP_URL:
                resendLoginOtp(request, response, attributes);
                break;
            default:
                verifyOtp(request, response, attributes);
                break;
        }
    }

    private void resendLoginOtp(HttpServletRequest request, HttpServletResponse response, Session attributes) throws IOException, ServletException {
        boolean otpStatus = otpServices.sendOtp(attributes.getEmail());
        if (otpStatus) {
            RedirectUtilities.setMessage(request, RedirectType.SUCCESS, "OTP sent successfully!");
        } else {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Failed to send OTP!");
        }
        RedirectUtilities.sendRedirect(request, response, LOGIN_2FA_URL);
    }

    private void verifyOtp(HttpServletRequest request, HttpServletResponse response, Session attributes) throws IOException, ServletException {
        String otp = request.getParameter("otp");
        if (StringUtilities.anyNullOrBlank(otp)) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Please fill in OTP!");
            setLogin2FAPage(request, response);
            return;
        }
        OtpsType otpStatus = otpServices.verifyOtp(attributes.getEmail(), otp);
        handleOtpStatus(request, response, otpStatus, attributes);
    }

    private void setLogin2FAPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(LOGIN_2FA_JSP_URL).forward(request, response);
    }

    private Session getSessionAttributes(HttpSession session) {
        if (session == null) {
            return null;
        }
        Integer loginId = (Integer) session.getAttribute("login_id_2fa");
        String username = (String) session.getAttribute("username");
        String email = (String) session.getAttribute("email");
        Role role = (Role) session.getAttribute("role");
        if (loginId == null || loginId == 0 || StringUtilities.anyNullOrBlank(email) || role == null) {
            return null;
        }
        return new Session(loginId, username, email, role);
    }

    private void handleOtpStatus(HttpServletRequest request, HttpServletResponse response, OtpsType otpStatus, Session session) throws IOException, ServletException {
        if (otpStatus == OtpsType.OK) {
            HttpSession httpSession = request.getSession();
            httpSession.invalidate();
            SessionUtilities.setLoginSession(httpSession, session);
            SecurityLog.addSecurityLog(request, "login successful with 2fa.");
            RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
        } else {
            String message = STATUS_MESSAGES.getOrDefault(otpStatus, "Failed to verify OTP!");
            RedirectUtilities.setMessage(request, RedirectType.DANGER, message);
            setLogin2FAPage(request, response);
        }
    }
}
