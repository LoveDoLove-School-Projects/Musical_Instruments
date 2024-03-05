package features;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import models.Customer;
import services.LoginServices;
import utilities.StringUtilities;

public class LoginHandler {

    private final LoginServices loginServices = new LoginServices();

    public int handle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int customerId = 0;

        response.setContentType("text/html;charset=UTF-8");

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
