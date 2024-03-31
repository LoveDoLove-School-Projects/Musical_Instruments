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
import java.util.Map;
import services.LoginServices;
import services.OtpServices;
import utilities.RedirectUtilities;
import utilities.StringUtilities;
import utilities.enums.RedirectType;

public class LoginServlet extends HttpServlet {

    private static final Map<String, Common.Role> ROLE_MAP = Map.of(
            Constants.CUSTOMER_LOGIN_URL, Common.Role.CUSTOMER,
            Constants.ADMIN_LOGIN_URL, Common.Role.ADMIN
    );
    private LoginServices loginServices;
    private OtpServices otpServices;
    private SessionHandler sessionHandler;

    @Override
    public void init() throws ServletException {
        this.loginServices = new LoginServices();
        this.otpServices = new OtpServices();
        this.sessionHandler = new SessionHandler();
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
        Session session = sessionHandler.getLoginSession(request, response);
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

    private void processPostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        Common.Role role = ROLE_MAP.get(path);
        if (role == null) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Invalid URL!");
            setLoginPage(request, response);
            return;
        }
        boolean isLoggedIn = handleLogin(request, response, role);
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
        LoginRequest loginRequest = createLoginRequest(request);
        request.setAttribute("email", loginRequest.getEmail());
        if (!validateLoginRequest(loginRequest)) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Please Fill In All The Fields!");
            return false;
        }
        LoginResponse loginResponse = loginServices.loginServices(loginRequest, role);
        switch (loginResponse.getStatus()) {
            case OK:
                handleSuccessfulLogin(request, response, loginResponse, role);
                return true;
            case NOT_FOUND:
                RedirectUtilities.setMessage(request, RedirectType.DANGER, "Email not found!");
                break;
            case UNAUTHORIZED:
                RedirectUtilities.setMessage(request, RedirectType.DANGER, "Incorrect Email or Password!");
                break;
            default:
                RedirectUtilities.setMessage(request, RedirectType.DANGER, "Failed to login!");
                break;
        }
        return false;
    }

    private void handleSuccessfulLogin(HttpServletRequest request, HttpServletResponse response, LoginResponse loginResponse, Common.Role role) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        if (loginResponse.isTwo_factor_auth()) {
            handleTwoFactorAuth(request, response, loginResponse, session, role);
        } else {
            sessionHandler.setLoginSession(session, loginResponse.getLogin_id(), role);
            RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
        }
    }

    private void handleTwoFactorAuth(HttpServletRequest request, HttpServletResponse response, LoginResponse loginResponse, HttpSession session, Common.Role role) throws ServletException, IOException {
        Common.Status otpStatus = otpServices.sendOtp(loginResponse.getEmail());
        if (otpStatus != Common.Status.OK) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "There was an error from the server! Please try again later.");
            return;
        }
        session.setAttribute("login_id_2fa", loginResponse.getLogin_id());
        session.setAttribute("role", role);
        session.setAttribute("email", loginResponse.getEmail());
        String loginSession = "JSESSIONID=" + session.getId() + ";Path=/;Secure;HttpOnly;SameSite=Strict";
        response.setHeader("Set-Cookie", loginSession);
        request.getRequestDispatcher(Constants.VERIFY_OTP_JSP_URL).forward(request, response);
    }

    private LoginRequest createLoginRequest(HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        return new LoginRequest(email, password);
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
