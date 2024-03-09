package contollers;

import common.Common;
import common.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import request.RegisterRequest;
import response.RegisterResponse;
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
                RegisterResponse registerResponse = addCustomer(request);
                if (registerResponse.getStatus().getCode() == Common.Status.OK.getCode()) {
                    RedirectUtilities.sendRedirect(request, response, Constants.LOGIN_URL);
                    return;
                }
                if (registerResponse.getStatus().getCode() == Common.Status.EXISTS.getCode()) {
                    RedirectUtilities.setMessage(request, response, "Email Already Exists!");
                } else if (registerResponse.getStatus().getCode() == Common.Status.INVALID.getCode()) {
                    RedirectUtilities.setMessage(request, response, "Please Fill All Fields Correctly!");
                } else if (registerResponse.getStatus().getCode() == Common.Status.INTERNAL_SERVER_ERROR.getCode()) {
                    RedirectUtilities.setMessage(request, response, "Internal Server Error!");
                } else {
                    RedirectUtilities.setMessage(request, response, "Error Registering New Customer!");
                }
            }
            request.getRequestDispatcher(Constants.REGISTER_JSP_URL).forward(request, response);
        } catch (ServletException | IOException ex) {
            throw new ServletException(ex);
        }
    }

    private RegisterResponse addCustomer(HttpServletRequest request)
            throws SQLException, IOException, ServletException {
        RegisterResponse registerResponse = new RegisterResponse();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String phone_number = request.getParameter("phone_number");
        String gender = request.getParameter("gender");

        request.setAttribute("username", username);
        request.setAttribute("email", email);
        request.setAttribute("address", address);
        request.setAttribute("phone_number", phone_number);
        request.setAttribute("gender", gender);

        RegisterRequest registerRequest = new RegisterRequest(username, password, email, address, phone_number, gender);

        if (!validateRegisterRequest(registerRequest)) {
            registerResponse.setStatus(Common.Status.INVALID);
            return registerResponse;
        }

        try {
            registerResponse = registerServices.registerNewCustomer(registerRequest);
            return registerResponse;
        } catch (SQLException ex) {
            throw new ServletException("Error registering new customer", ex);
        }
    }

    private boolean validateRegisterRequest(RegisterRequest registerRequest) {
        if (registerRequest == null) {
            return false;
        }
        if (StringUtilities.anyNullOrBlank(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail(), registerRequest.getAddress(), registerRequest.getPhoneNumber(), registerRequest.getGender())) {
            return false;
        }
        if (registerRequest.getPassword().length() < 8) {
            return false;
        }
        for (char ch : registerRequest.getPhoneNumber().toCharArray()) {
            if (!Character.isDigit(ch)) {
                return false;
            }
        }
        return true;
    }
}
