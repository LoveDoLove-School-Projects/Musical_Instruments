package contollers;

import common.Common;
import common.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import request.RegisterRequest;
import response.RegisterResponse;
import services.RegisterServices;
import utilities.RedirectUtilities;
import utilities.StringUtilities;

public class RegisterServlet extends HttpServlet {

    private final RegisterServices registerServices = new RegisterServices();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRegister(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRegister(request, response);
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            String path = request.getServletPath();
            switch (path) {
                case Constants.REGISTER_URL:
                    registerCustomer(request, response);
                    return;
            }
        }
        request.getRequestDispatcher(Constants.REGISTER_JSP_URL).forward(request, response);
    }

    private void registerCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            RedirectUtilities.redirectWithError(request, response, "Please Fill All Fields Correctly!", Constants.REGISTER_URL);
            return;
        }

        RegisterResponse registerResponse = registerServices.addNewCustomer(registerRequest);

        if (registerResponse == null || registerResponse.getStatus() == Common.Status.INTERNAL_SERVER_ERROR) {
            RedirectUtilities.setErrorMessage(request, "Internal Server Error!");
        } else if (registerResponse.getStatus() == Common.Status.EXISTS) {
            RedirectUtilities.setErrorMessage(request, "Email Already Exists!");
        } else if (registerResponse.getStatus() == Common.Status.OK) {
            RedirectUtilities.setSuccessMessage(request, "Registered Successfully!");
            RedirectUtilities.sendRedirect(request, response, Constants.LOGIN_URL);
            return;
        }
        RedirectUtilities.sendRedirect(request, response, Constants.REGISTER_URL);
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
