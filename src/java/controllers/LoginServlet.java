package controllers;

import domain.common.Common;
import domain.common.Constants;
import domain.models.Session;
import domain.request.LoginRequest;
import domain.response.LoginResponse;
import domain.response.OtpResponse;
import features.SessionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import services.LoginServices;
import services.OtpServices;
import utilities.RedirectUtilities;
import utilities.StringUtilities;
import utilities.enums.RedirectType;

public class LoginServlet extends HttpServlet {

    private static final LoginServices loginServices = new LoginServices();
    private static final OtpServices otpServices = new OtpServices();
    private static final SessionHandler sessionHandler = new SessionHandler();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = sessionHandler.getLoginSession(request, response);
        if (session.isResult()) {
            RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
            return;
        }
        String path = request.getServletPath();
        switch (path) {
            case Constants.CUSTOMER_LOGIN_URL:
                request.setAttribute("loginFormUrl", "pages/login");
                break;
            case Constants.ADMIN_LOGIN_URL:
                request.setAttribute("loginFormUrl", "pages/adminLogin");
                break;
        }
        setLoginPage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        Common.Role role = null;
        boolean isLoggedIn = false;
        switch (path) {
            case Constants.CUSTOMER_LOGIN_URL:
                role = Common.Role.CUSTOMER;
                break;
            case Constants.ADMIN_LOGIN_URL:
                role = Common.Role.ADMIN;
                break;
        }
        isLoggedIn = handleLogin(request, response, role);
        if (isLoggedIn) {
            return;
        }
        setLoginPage(request, response);
    }

    private void setLoginPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(Constants.LOGIN_JSP_URL).forward(request, response);
    }

    private boolean handleLogin(HttpServletRequest request, HttpServletResponse response, Common.Role role) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        request.setAttribute("email", email);

        if (StringUtilities.anyNullOrBlank(email, password)) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Email and Password are required!");
            return false;
        }
        LoginRequest loginRequest = new LoginRequest(email, password);
        LoginResponse loginResponse = loginServices.loginServices(loginRequest, role);

        if (loginResponse.getStatus() == null) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Failed to login!");
            return false;
        }

        if (loginResponse.getStatus() == Common.Status.NOT_FOUND) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Email not found!");
            return false;
        }

        if (loginResponse.getStatus() == Common.Status.UNAUTHORIZED) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Incorrect Email or Password!");
            return false;
        }

        if (loginResponse.getStatus() != Common.Status.OK) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Failed to login!");
            return false;
        }

        HttpSession session = request.getSession(true);
        if (loginResponse.isTwo_factor_auth()) {
            OtpResponse otpResponse = otpServices.sendOtp(loginResponse.getEmail());
            if (otpResponse.getStatus() != Common.Status.OK) {
                RedirectUtilities.setMessage(request, RedirectType.DANGER, "There was an error from the server! Please try again later.");
                return false;
            }
            session.setAttribute("login_id_2fa", loginResponse.getLogin_id());
            session.setAttribute("role", role);
            session.setAttribute("email", loginResponse.getEmail());
            String loginSession = "JSESSIONID=" + session.getId() + ";Path=/;Secure;HttpOnly;SameSite=Strict";
            response.setHeader("Set-Cookie", loginSession);
            request.getRequestDispatcher(Constants.VERIFY_OTP_JSP_URL).forward(request, response);
        } else {
            sessionHandler.setLoginSession(session, loginResponse.getLogin_id(), role);
            RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
        }
        return true;
    }
}
