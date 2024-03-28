package controllers;

import domain.common.Common;
import domain.common.Constants;
import domain.models.Session;
import domain.response.OtpResponse;
import features.SessionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import services.OtpServices;
import utilities.RedirectUtilities;
import utilities.StringUtilities;
import utilities.enums.RedirectType;

public class OtpServlet extends HttpServlet {

    private static final String LOGIN_ID_ATTRIBUTE = "login_id_2fa";
    private static final String EMAIL_ATTRIBUTE = "email";
    private static final String ROLE_ATTRIBUTE = "role";

    private static final SessionHandler sessionHandler = new SessionHandler();
    private static final OtpServices otpServices = new OtpServices();

    private void setOtpPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(Constants.VERIFY_OTP_JSP_URL).forward(request, response);
    }

    private Session getSessionAttributes(HttpSession session) {
        Integer loginId = (Integer) session.getAttribute(LOGIN_ID_ATTRIBUTE);
        String email = (String) session.getAttribute(EMAIL_ATTRIBUTE);
        Common.Role role = (Common.Role) session.getAttribute(ROLE_ATTRIBUTE);
        return new Session(loginId, email, role);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");

        HttpSession session = request.getSession(false);
        Session attributes = getSessionAttributes(session);

        String otp = request.getParameter("otp");

        if (StringUtilities.anyNullOrBlank(otp)) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Please fill in OTP!");
            setOtpPage(request, response);
            return;
        }

        OtpResponse otpResponse = otpServices.verifyOtp(attributes.getEmail(), otp);

        if (otpResponse.getStatus() == Common.Status.INVALID) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Invalid OTP!");
            setOtpPage(request, response);
            return;
        }

        session.invalidate();
        session = request.getSession(true);
        sessionHandler.setLoginSession(session, attributes.getId(), attributes.getRole());

        String loginSession = "JSESSIONID=" + session.getId() + ";Path=/;Secure;HttpOnly;SameSite=Strict";
        response.setHeader("Set-Cookie", loginSession);
        RedirectUtilities.sendRedirect(request, response, Constants.MAIN_URL);
    }

}
