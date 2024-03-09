package contollers;

import common.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import models.Customer;
import services.LoginServices;
import utilities.RedirectUtilities;
import utilities.SessionUtilities;
import utilities.StringUtilities;

public class LoginServlet extends HttpServlet {

    private final LoginServices loginServices = new LoginServices();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            handleLogin(request, response);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            handleLogin(request, response);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        try {
            if ("POST".equalsIgnoreCase(request.getMethod())) {
                int customerId = loginCustomer(request);
                if (customerId == 0) {
                    String email = request.getParameter("email");
                    request.setAttribute("email", email);
                    RedirectUtilities.setMessage(request, response, "Incorrect Email or Password!");
                } else {
                    SessionUtilities.setSessionAttribute(request.getSession(), "login_id", customerId);
                    RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
                    return;
                }
            }
            request.getRequestDispatcher(Constants.LOGIN_JSP_URL).forward(request, response);
        } catch (ServletException | IOException ex) {
            throw new ServletException(ex);
        }
    }

    private int loginCustomer(HttpServletRequest request)
            throws ServletException, IOException {
        int customerId = 0;
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if (StringUtilities.anyNullOrBlank(email, password)) {
            return customerId;
        }
        Customer customer = new Customer(password, email);
        try {
            customerId = loginServices.loginCustomer(customer);
        } catch (Exception ex) {
            throw new ServletException("Error logging in customer", ex);
        }
        return customerId;
    }
}
