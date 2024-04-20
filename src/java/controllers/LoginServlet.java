package controllers;

import domain.common.Common;
import domain.common.Constants;
import domain.models.Session;
import domain.models.Users;
import domain.request.LoginRequest;
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
import utilities.RedirectUtilities.RedirectType;
import utilities.StringUtilities;

public class LoginServlet extends HttpServlet {

    private static final String LOGIN_2FA_URL = "/sessions/login2fa";
    private final LoginServices customerLoginServices = new LoginServices();
    private final OtpServices otpServices = new OtpServices();
    private final SessionHandler sessionHandler = new SessionHandler();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = sessionHandler.getLoginSession(request.getSession());
        if (session.isResult()) {
            RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
            return;
        }
        switch (request.getMethod()) {
            case "GET":
                processGetRequest(request, response);
                break;
            case "POST":
                processPostRequest(request, response);
                break;
        }
    }

    private void processGetRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setLoginPage(request, response);
    }

    private void processPostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        LoginRequest loginRequest = createLoginRequest(request);
        request.setAttribute(Constants.EMAIL_ATTRIBUTE, loginRequest.getEmail());
        if (!validateLoginRequest(loginRequest)) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Please Fill In All The Fields!");
            return;
        }
        Users users = customerLoginServices.loginToCustomer(loginRequest);
        if (users == null) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Login Failed! Please Try Again!");
            setLoginPage(request, response);
            return;
        }
        checkNeedTwoFactorAuthOrNot(request, response, users);
    }

    private void setLoginPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(Constants.LOGIN_JSP_URL).forward(request, response);
    }

    private LoginRequest createLoginRequest(HttpServletRequest request) {
        String email = request.getParameter(Constants.EMAIL_ATTRIBUTE);
        String password = request.getParameter(Constants.PASSWORD_ATTRIBUTE);
        return new LoginRequest(email, password);
    }

    private void checkNeedTwoFactorAuthOrNot(HttpServletRequest request, HttpServletResponse response, Users users) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        if (!users.getTwo_factor_auth()) {
            sessionHandler.setLoginSession(session, users.getId());
            RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
            return;
        }
        requiredTwoFactorAuth(request, response, users, session);
    }

    private void requiredTwoFactorAuth(HttpServletRequest request, HttpServletResponse response, Users users, HttpSession session) throws ServletException, IOException {
        Common.Status otpStatus = otpServices.sendOtp(users.getEmail());
        if (otpStatus != Common.Status.OK) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "There was an error from the server! Please try again later.");
            return;
        }
        session.setAttribute(Constants.LOGIN_ID_2FA_ATTRIBUTE, users.getId());
        session.setAttribute(Constants.EMAIL_ATTRIBUTE, users.getEmail());
        RedirectUtilities.sendRedirect(request, response, LOGIN_2FA_URL);
    }

    private boolean validateLoginRequest(LoginRequest loginRequest) {
        if (loginRequest == null) {
            return false;
        }
        if (StringUtilities.anyNullOrBlank(loginRequest.getPassword(), loginRequest.getEmail())) {
            return false;
        }
        if (loginRequest.getPassword().length() < 8) {
            return false;
        }
        return !(!loginRequest.getEmail().contains("@") || !loginRequest.getEmail().contains("."));
    }
}
