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
import java.io.IOException;
import services.LoginServices;
import utilities.RedirectUtilities;
import utilities.StringUtilities;

public class LoginServlet extends HttpServlet {

    private final LoginServices loginServices = new LoginServices();
    private final SessionHandler sessionHandler = new SessionHandler();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleLogin(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleLogin(request, response);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            Common.Role role = null;
            switch (path) {
                case Constants.CUSTOMER_LOGIN_URL:
                    role = Common.Role.CUSTOMER;
                    break;
                case Constants.ADMIN_LOGIN_URL:
                    role = Common.Role.ADMIN;
                    break;
            }
            if (!handleLogin(request, response, role)) {
                setLoginPage(request, response);
            }
            return;
        }
        setLoginPage(request, response);
    }

    private void setLoginPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        request.getRequestDispatcher(Constants.LOGIN_JSP_URL).forward(request, response);
    }

    private boolean handleLogin(HttpServletRequest request, HttpServletResponse response, Common.Role role) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        request.setAttribute("email", email);

        if (StringUtilities.anyNullOrBlank(email, password)) {
            RedirectUtilities.setErrorMessage(request, "Email and Password are required!");
            return false;
        }
        LoginRequest loginRequest = new LoginRequest(email, password);
        LoginResponse loginResponse = loginServices.loginServices(loginRequest, role);

        if (loginResponse.getStatus() == null) {
            RedirectUtilities.setErrorMessage(request, "Failed to login!");
            return false;
        }

        if (loginResponse.getStatus() == Common.Status.NOT_FOUND) {
            RedirectUtilities.setErrorMessage(request, "Email not found!");
            return false;
        }

        if (loginResponse.getStatus() == Common.Status.UNAUTHORIZED) {
            RedirectUtilities.setErrorMessage(request, "Incorrect Email or Password!");
            return false;
        }

        if (loginResponse.getStatus() == Common.Status.OK) {
            sessionHandler.setLoginSession(request.getSession(), loginResponse.getLogin_id(), role);
            RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
            return true;
        }

        RedirectUtilities.setErrorMessage(request, "Failed to login!");
        return false;
    }
}
