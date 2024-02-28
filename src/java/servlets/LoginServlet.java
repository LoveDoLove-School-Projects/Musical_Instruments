package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Customer;
import services.LoginServices;
import utilities.AesUtilities;
import utilities.StringUtilities;

public class LoginServlet extends HttpServlet {

    protected int processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int status = 0;

        response.setContentType("text/html;charset=UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (StringUtilities.anyNullOrBlank(email, password)) {
            return status;
        }

        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPassword(password);

        status = new LoginServices().loginCustomer(customer);
        return status;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        int status = processRequest(request, response);

        if (status == 0) {
            request.getRequestDispatcher("login.jsp").include(request, response);
            out.print("<center><p>Please Enter Valid Details to Login</p></center>");
            return;
        }

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Cookie userCookie = new Cookie("data", AesUtilities.aes256EcbEncrypt(email));
        userCookie.setMaxAge(3600);
        response.addCookie(userCookie);
        response.sendRedirect("index.jsp");
    }
}
