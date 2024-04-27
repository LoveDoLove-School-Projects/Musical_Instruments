package controllers;

import common.Common;
import common.Constants;
import entities.Session;
import features.SessionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import dao.OtpDao;
import utilities.RedirectUtilities;
import utilities.RedirectUtilities.RedirectType;
import utilities.StringUtilities;

@WebServlet(name = "Login2FAServlet", urlPatterns = "/sessions/login2fa")
public class Login2FAServlet extends HttpServlet {

    private static final String LOGIN_2FA_JSP_URL = "/sessions/login2fa.jsp";
    private static final Map<Common.Status, String> STATUS_MESSAGES;

    static {
        STATUS_MESSAGES = new EnumMap<>(Common.Status.class);
        STATUS_MESSAGES.put(Common.Status.NOT_FOUND, "OTP not found!");
        STATUS_MESSAGES.put(Common.Status.UNAUTHORIZED, "Too many attempts! Please click on 'Resend OTP' to try again.");
        STATUS_MESSAGES.put(Common.Status.EXPIRED, "OTP expired! Please click on 'Resend OTP' to try again.");
        STATUS_MESSAGES.put(Common.Status.FAILED, "Failed to verify OTP! Please try again.");
        STATUS_MESSAGES.put(Common.Status.INVALID, "Invalid OTP!");
    }
    private final SessionHandler sessionHandler = new SessionHandler();
    private final OtpDao otpServices = new OtpDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
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
        HttpSession session = request.getSession(false);
        Session attributes = getSessionAttributes(session);
        if (attributes == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Invalid request!", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        String otp = request.getParameter("otp");
        if (StringUtilities.anyNullOrBlank(otp)) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Please fill in OTP!");
            setLogin2FAPage(request, response);
            return;
        }
        Common.Status otpStatus = otpServices.verifyOtp(attributes.getEmail(), otp);
        handleOtpStatus(otpStatus, request, response, session, attributes);
    }

    private void setLogin2FAPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(LOGIN_2FA_JSP_URL).forward(request, response);
    }

    private Session getSessionAttributes(HttpSession session) {
        if (session == null) {
            return null;
        }
        Integer loginId = (Integer) session.getAttribute("login_id_2fa");
        String email = (String) session.getAttribute("email");
        if (loginId == null || loginId == 0 || StringUtilities.anyNullOrBlank(email)) {
            return null;
        }
        return new Session(loginId, email);
    }

    private void handleOtpStatus(Common.Status otpStatus, HttpServletRequest request, HttpServletResponse response, HttpSession session, Session attributes) throws IOException, ServletException {
        if (otpStatus == Common.Status.OK) {
            session.invalidate();
            session = request.getSession(true);
            sessionHandler.setLoginSession(session, attributes.getUserId());
            RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
        } else {
            String message = STATUS_MESSAGES.getOrDefault(otpStatus, "Failed to verify OTP!");
            RedirectUtilities.setMessage(request, RedirectType.DANGER, message);
            setLogin2FAPage(request, response);
        }
    }
}
