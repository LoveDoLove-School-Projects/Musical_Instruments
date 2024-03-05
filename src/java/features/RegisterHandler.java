package features;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import models.Customer;
import services.RegisterServices;
import utilities.StringUtilities;

public class RegisterHandler {

    private final RegisterServices registerServices = new RegisterServices();

    public int handle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int status = 0;

        response.setContentType("text/html;charset=UTF-8");

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
