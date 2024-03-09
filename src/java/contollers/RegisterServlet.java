package contollers;

import common.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import models.Customer;
import services.RegisterServices;
import utilities.RedirectUtilities;
import utilities.StringUtilities;

public class RegisterServlet extends HttpServlet {

    private final RegisterServices registerServices = new RegisterServices();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            handleRegister(request, response);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            handleRegister(request, response);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        try {
            if ("POST".equalsIgnoreCase(request.getMethod())) {
                int status = addCustomer(request);
                if (status == 0) {
                    String email = request.getParameter("email");
                    request.setAttribute("email", email);
                    RedirectUtilities.setMessage(request, response, "Please Enter Valid Details to Register!");
                } else {
                    RedirectUtilities.sendRedirect(request, response, Constants.LOGIN_URL);
                    return;
                }
            }
            request.getRequestDispatcher(Constants.REGISTER_JSP_URL).forward(request, response);
        } catch (ServletException | IOException ex) {
            throw new ServletException(ex);
        }
    }

    private int addCustomer(HttpServletRequest request)
            throws SQLException, IOException, ServletException {
        int status = 0;
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String phone_number = request.getParameter("phone_number");
        String gender = request.getParameter("gender");

        if (StringUtilities.anyNullOrBlank(username, password, email, address, phone_number, gender)) {
            return status;
        }

        Customer customer = new Customer(username, password, email, address, phone_number, gender);
        try {
            status = registerServices.registerNewCustomer(customer);
        } catch (Exception ex) {
            throw new ServletException("Error registering new customer", ex);
        }
        return status;
    }
}
