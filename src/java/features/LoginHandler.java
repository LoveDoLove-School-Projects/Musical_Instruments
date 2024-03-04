package features;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import models.Customer;
import services.LoginServices;
import utilities.StringUtilities;

public class LoginHandler {

    public int handle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int customer_id = 0;

        response.setContentType("text/html;charset=UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (StringUtilities.anyNullOrBlank(email, password)) {
            return customer_id;
        }

        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPassword(password);

        LoginServices loginServices = new LoginServices();
        customer_id = loginServices.loginCustomer(customer);
        return customer_id;
    }
}
