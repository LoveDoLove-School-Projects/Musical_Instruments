package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import models.Customer;
import services.RegisterServices;
import utilities.StringUtilities;

public class RegisterServlet extends HttpServlet {

    protected int processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int status = 0;

        response.setContentType("text/html;charset=UTF-8");

        String first_name = request.getParameter("first_name");
        String last_name = request.getParameter("last_name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (StringUtilities.anyNullOrBlank(first_name, last_name, email, password)) {
            return status;
        }

        Customer customer = new Customer();
        customer.setFirstName(first_name);
        customer.setLastName(last_name);
        customer.setEmail(email);
        customer.setPassword(password);

        RegisterServices registerServices = new RegisterServices();
        status = registerServices.insertCustomer(customer);
        return status;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter printWriter = response.getWriter();
        int status = processRequest(request, response);

        if (status == 1) {
            printWriter.print("<center><h3>Registration Successful.</h3>" + "<p><a href='login.jsp'> Login</a></p></center>");
            return;
        }

        request.getRequestDispatcher("register.jsp").include(request, response);
        printWriter.print("<center><p>Please Enter Valid Details to Register</p></center>");
    }
}
