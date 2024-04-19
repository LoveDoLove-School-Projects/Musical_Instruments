package controllers;

import domain.common.Common;
import static domain.common.Common.Role.ADMIN;
import static domain.common.Common.Role.CUSTOMER;
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
import java.util.Map;
import services.AdminLoginServices;
import services.CustomerLoginServices;
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
    private final AdminLoginServices adminLoginServices = new AdminLoginServices();
    private final CustomerLoginServices customerLoginServices = new CustomerLoginServices();
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
        String path = request.getServletPath();
        request.setAttribute("loginFormUrl", LOGIN_FORM_URLS.get(path));
        setLoginPage(request, response);
    }

    private void processPostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        Common.Role role = ROLE_MAP.get(path);
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        LoginRequest loginRequest = createLoginRequest(request);
        request.setAttribute(Constants.EMAIL_ATTRIBUTE, loginRequest.getEmail());
        if (!validateLoginRequest(loginRequest)) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Please Fill In All The Fields!");
            return;
        }
        Users users = null;
        switch (role) {
            case ADMIN:
                users = adminLoginServices.loginToAdmin(loginRequest);
                break;
            case CUSTOMER:
                users = customerLoginServices.loginToCustomer(loginRequest);
                break;
            default:
                break;
        }
        if (users == null) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Login Failed! Please Try Again!");
            setLoginPage(request, response);
            return;
        }
        checkNeedTwoFactorAuthOrNot(request, response, users, role);
    }

    private void setLoginPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(Constants.LOGIN_JSP_URL).forward(request, response);
    }

    private LoginRequest createLoginRequest(HttpServletRequest request) {
        String email = request.getParameter(Constants.EMAIL_ATTRIBUTE);
        String password = request.getParameter(Constants.PASSWORD_ATTRIBUTE);
        return new LoginRequest(email, password);
    }

    private void checkNeedTwoFactorAuthOrNot(HttpServletRequest request, HttpServletResponse response, Users users, Common.Role role) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        if (!users.getTwo_factor_auth()) {
            sessionHandler.setLoginSession(session, users.getId(), role);
            RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
            return;
        }
        requiredTwoFactorAuth(request, response, users, session, role);
    }

    private void requiredTwoFactorAuth(HttpServletRequest request, HttpServletResponse response, Users users, HttpSession session, Common.Role role) throws ServletException, IOException {
        Common.Status otpStatus = otpServices.sendOtp(users.getEmail());
        if (otpStatus != Common.Status.OK) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "There was an error from the server! Please try again later.");
            return;
        }
        session.setAttribute(Constants.LOGIN_ID_2FA_ATTRIBUTE, users.getId());
        session.setAttribute(Constants.ROLE_ATTRIBUTE, role);
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
