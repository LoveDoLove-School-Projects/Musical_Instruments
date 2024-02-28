package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import models.Customer;
import models.Message;
import services.RegisterServices;
import utilities.MessageUtilities;
import utilities.StringUtilities;

public class RegisterServlet extends HttpServlet {

    protected int processRequest(HttpServletRequest request, HttpServletResponse response)
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

        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setPassword(password);
        customer.setEmail(email);
        customer.setAddress(address);
        customer.setPhoneNumber(phone_number);
        customer.setGender(gender);

        RegisterServices registerServices = new RegisterServices();
        status = registerServices.registerNewCustomer(customer);
        return status;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int status = processRequest(request, response);

        if (status != 1) {
            Message message = new Message();
            message.setRequest(request);
            message.setResponse(response);
            message.setMessage("Please Enter Valid Details to Register!");
            message.setJspPage("register.jsp");
            MessageUtilities.sendMessageToJsp(message);
            return;
        }

        response.sendRedirect("login.jsp");
    }
}
