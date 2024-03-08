package contollers;

import features.RegisterHandler;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import utilities.RedirectUtilities;

public class RegisterServlet extends HttpServlet {

    private final RegisterHandler registerHandler = new RegisterHandler();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
//        int status = registerHandler.handle(request, response);
//
//        if (status == 0) {
//            RedirectUtilities.redirectWithMessage(request, response, "Please Enter Valid Details to Register!", "/pages/register.jsp");
//            return;
//        }
//
//        response.sendRedirect("/pages/login.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        try {
            switch (action) {
                case "/pages/register":
                    showRegisterForm(request, response);
                    break;
                case "/pages/register/add":
                    addNewCustomer(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void showRegisterForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String phone_number = request.getParameter("phone_number");
        String gender = request.getParameter("gender");

        request.setAttribute("username", username);
        request.setAttribute("password", password);
        request.setAttribute("email", email);
        request.setAttribute("address", address);
        request.setAttribute("phone_number", phone_number);
        request.setAttribute("gender", gender);

        // This one is for the ui to let user interact
        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/register.jsp");
        dispatcher.forward(request, response);
    }

    private void addNewCustomer(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int status = registerHandler.handle(request, response);

        if (status != 0) {
            RedirectUtilities.setMessage(request, response, "Please Enter Valid Details to Register!");
            showRegisterForm(request, response);
            return;
        }

        response.sendRedirect("/pages/login");
//        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/register.jsp");
//        dispatcher.forward(request, response);
    }
}
