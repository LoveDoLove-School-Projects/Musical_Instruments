package contollers;

import common.Common;
import common.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
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
        try {
            handleLogin(request, response);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            handleLogin(request, response);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            LoginResponse loginResponse = loginCustomer(request);
            if (loginResponse.getStatus().getCode() == Common.Status.OK.getCode()) {
                SessionUtilities.setSessionAttribute(request.getSession(), "login_id", loginResponse.getLogin_id());
                RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
                return;
            }
            if (loginResponse.getStatus().getCode() == Common.Status.INVALID.getCode()) {
                RedirectUtilities.setErrorMessage(request, "Incorrect Email or Password!");
            } else {
                RedirectUtilities.setErrorMessage(request, "Error Logging In!");
            }
        }
        request.getRequestDispatcher(Constants.LOGIN_JSP_URL).forward(request, response);
    }

    private LoginResponse loginCustomer(HttpServletRequest request) throws SQLException {
        LoginResponse loginResponse = new LoginResponse();
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        request.setAttribute("email", email);
        if (StringUtilities.anyNullOrBlank(email, password)) {
            loginResponse.setStatus(Common.Status.INVALID);
            return loginResponse;
        }
        LoginRequest loginRequest = new LoginRequest(email, password);
        loginResponse = loginServices.loginCustomer(loginRequest);
        return loginResponse;
    }
}
