package servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Customer;
import models.Message;
import services.LoginServices;
import utilities.MessageUtilities;
import utilities.SessionUtilities;
import utilities.StringUtilities;

public class LoginServlet extends HttpServlet {

    private HttpSession session;

    protected int processRequest(HttpServletRequest request, HttpServletResponse response)
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int customer_id = processRequest(request, response);

        if (customer_id == 0) {
            Message message = new Message();
            message.setRequest(request);
            message.setResponse(response);
            message.setMessage("Incorrect Email or Password!");
            message.setJspPage("login.jsp");
            MessageUtilities.sendMessageToJsp(message);
            return;
        }

        session = request.getSession();
        SessionUtilities.setSessionAttribute(session, "customer_id", customer_id);

        response.sendRedirect("index.jsp");
    }
}
