package controllers;

import domain.common.Common;
import domain.common.Constants;
import domain.models.Session;
import domain.request.LoginRequest;
import domain.response.LoginResponse;
import features.SessionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import services.LoginServices;
import services.OtpServices;
import utilities.RedirectUtilities;
import utilities.RedirectUtilities.RedirectType;
import utilities.StringUtilities;

public class LoginServlet extends HttpServlet {

    private static final String LOGIN_2FA_URL = "/sessions/login2fa";
    private static final Map<String, Common.Role> ROLE_MAP = Map.of(
            Constants.CUSTOMER_LOGIN_URL, Common.Role.CUSTOMER,
            Constants.ADMIN_LOGIN_URL, Common.Role.ADMIN
    );
    private static final Map<String, String> LOGIN_FORM_URLS = Map.of(
            Constants.CUSTOMER_LOGIN_URL, "pages/login",
            Constants.ADMIN_LOGIN_URL, "pages/adminLogin"
    );
    private static final Map<Common.Status, String> STATUS_MESSAGES;
    private final LoginServices loginServices = new LoginServices();
    private final OtpServices otpServices = new OtpServices();
    private final SessionHandler sessionHandler = new SessionHandler();

    static {
        STATUS_MESSAGES = new EnumMap<>(Common.Status.class);
        STATUS_MESSAGES.put(Common.Status.NOT_FOUND, "Email not found!");
        STATUS_MESSAGES.put(Common.Status.UNAUTHORIZED, "Incorrect Email or Password!");
        STATUS_MESSAGES.put(Common.Status.FAILED, "Failed to login!");
    }

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
        String path = request.getServletPath();
        request.setAttribute("loginFormUrl", LOGIN_FORM_URLS.get(path));
        setLoginPage(request, response);
    }

    private void processPostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        handleLoginRequest(request, response, ROLE_MAP.get(path));
    }

    private void setLoginPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(Constants.LOGIN_JSP_URL).forward(request, response);
    }

    private LoginRequest createLoginRequest(HttpServletRequest request) {
        String email = request.getParameter(Constants.EMAIL_ATTRIBUTE);
        String password = request.getParameter("password");
        return new LoginRequest(email, password);
    }

    private void handleLoginRequest(HttpServletRequest request, HttpServletResponse response, Common.Role role) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        LoginRequest loginRequest = createLoginRequest(request);
        request.setAttribute(Constants.EMAIL_ATTRIBUTE, loginRequest.getEmail());
        if (!validateLoginRequest(loginRequest)) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Please Fill In All The Fields!");
            return;
        }
        LoginResponse loginResponse = loginServices.loginServices(loginRequest, role);
        if (loginResponse.getStatus() == Common.Status.OK) {
            checkNeedTwoFactorAuthOrNot(request, response, loginResponse, role);
        } else {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, STATUS_MESSAGES.get(loginResponse.getStatus()));
            setLoginPage(request, response);
        }
    }

    private void checkNeedTwoFactorAuthOrNot(HttpServletRequest request, HttpServletResponse response, LoginResponse loginResponse, Common.Role role) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        if (!loginResponse.isTwo_factor_auth()) {
            sessionHandler.setLoginSession(session, loginResponse.getLogin_id(), role);
            RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
            return;
        }
        requiredTwoFactorAuth(request, response, loginResponse, session, role);
    }

    private void requiredTwoFactorAuth(HttpServletRequest request, HttpServletResponse response, LoginResponse loginResponse, HttpSession session, Common.Role role) throws ServletException, IOException {
        Common.Status otpStatus = otpServices.sendOtp(loginResponse.getEmail());
        if (otpStatus != Common.Status.OK) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "There was an error from the server! Please try again later.");
            return;
        }
        session.setAttribute(Constants.LOGIN_ID_2FA_ATTRIBUTE, loginResponse.getLogin_id());
        session.setAttribute(Constants.ROLE_ATTRIBUTE, role);
        session.setAttribute(Constants.EMAIL_ATTRIBUTE, loginResponse.getEmail());
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
