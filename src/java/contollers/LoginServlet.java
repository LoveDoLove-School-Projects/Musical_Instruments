package contollers;

import common.Common;
import common.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import request.LoginRequest;
import response.LoginResponse;
import services.LoginServices;
import utilities.RedirectUtilities;
import utilities.SessionUtilities;
import utilities.StringUtilities;

public class LoginServlet extends HttpServlet {

    private final LoginServices loginServices = new LoginServices();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleLogin(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleLogin(request, response);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            String path = request.getServletPath();
            switch (path) {
                case Constants.LOGIN_URL:
                    loginCustomer(request, response);
                    return;
            }
        }
        request.getRequestDispatcher(Constants.LOGIN_JSP_URL).forward(request, response);
    }

    private void loginCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        request.setAttribute("email", email);
        if (StringUtilities.anyNullOrBlank(email, password)) {
            RedirectUtilities.redirectWithError(request, response, "Email and Password are required!", Constants.LOGIN_URL);
            return;
        }
        LoginRequest loginRequest = new LoginRequest(email, password);
        LoginResponse loginResponse = loginServices.loginCustomer(loginRequest);
        if (loginResponse == null || loginResponse.getStatus() == Common.Status.UNAUTHORIZED) {
            RedirectUtilities.setErrorMessage(request, "Incorrect Email or Password!");
        } else if (loginResponse.getStatus() == Common.Status.OK) {
            SessionUtilities.setSessionAttribute(request.getSession(), "login_id", loginResponse.getLogin_id());
            RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
            return;
        }
        RedirectUtilities.sendRedirect(request, response, Constants.LOGIN_URL);
    }
}
