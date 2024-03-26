package controllers;

import domain.common.Common;
import domain.common.Constants;
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

    private final SessionHandler sessionHandler = new SessionHandler();

    private final OtpServices otpServices = new OtpServices();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");

        HttpSession session = request.getSession(false);
        Integer login_id = (Integer) session.getAttribute("login_id_2fa");
        String email = (String) session.getAttribute("email");
        Common.Role role = (Common.Role) session.getAttribute("role");
        session.removeAttribute("login_id_2fa");
        session.removeAttribute("email");
        session.removeAttribute("role");

        String otp = (String) request.getParameter("otp");

        System.out.println(otp);

        if (StringUtilities.anyNullOrBlank(otp)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Please fill in OTP!", Constants.CUSTOMER_LOGIN_URL);
            return;
        }

        OtpResponse otpResponse = otpServices.verifyOtp(email, otp);

        if (otpResponse.getStatus() == Common.Status.INVALID) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Invalid OTP!", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        session.invalidate();
        session = request.getSession(true);
        sessionHandler.setLoginSession(session, login_id, role);

        String loginSession = "JSESSIONID=" + session.getId() + ";Path=/;Secure;HttpOnly;SameSite=Strict";
        response.setHeader("Set-Cookie", loginSession);
        RedirectUtilities.sendRedirect(request, response, Constants.MAIN_URL);
    }

}
