package controllers;

import domain.common.Common;
import domain.common.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import domain.request.RegisterRequest;
import domain.response.OtpResponse;
import domain.response.RegisterResponse;
import services.OtpServices;
import services.RegisterServices;
import utilities.RedirectUtilities;
import utilities.StringUtilities;

public class RegisterServlet extends HttpServlet {

    private final RegisterServices registerServices = new RegisterServices();

    private final OtpServices otpServices = new OtpServices();

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
                    if (!registerCustomer(request, response)) {
                        request.getRequestDispatcher(Constants.REGISTER_JSP_URL).forward(request, response);
                    }
                    return;
            }
        }
        request.getRequestDispatcher(Constants.REGISTER_JSP_URL).forward(request, response);
    }

    private boolean registerCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirm_password = request.getParameter("confirm_password");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String phone_number = request.getParameter("phone_number");
        String gender = request.getParameter("gender");
        String otp = request.getParameter("otp");

        request.setAttribute("username", username);
        request.setAttribute("email", email);
        request.setAttribute("address", address);
        request.setAttribute("phone_number", phone_number);

        RegisterRequest registerRequest = new RegisterRequest(username, password, confirm_password, email, address, phone_number, gender, otp);

        if (!validateRegisterRequest(registerRequest)) {
            RedirectUtilities.setErrorMessage(request, "Please Fill In All The Fields!");
            return false;
        }

        if (!password.equals(confirm_password)) {
            RedirectUtilities.setErrorMessage(request, "Password and Confirm Password should be same!");
            return false;
        }

        OtpResponse otpResponse = otpServices.verifyOtp(email, otp);

        if (otpResponse.getStatus() != Common.Status.OK) {
            RedirectUtilities.setErrorMessage(request, "Failed to verify OTP!");
            return false;
        }

        RegisterResponse registerResponse = registerServices.addNewCustomer(registerRequest);

        if (registerResponse.getStatus() == null) {
            RedirectUtilities.setErrorMessage(request, "Failed to Register!");
            return false;
        }

        if (registerResponse.getStatus() == Common.Status.EXISTS) {
            RedirectUtilities.setErrorMessage(request, "Email Already Exists!");
            return false;
        }

        if (registerResponse.getStatus() == Common.Status.OK) {
            RedirectUtilities.setSuccessMessage(request, "Registered Successfully!");
            otpServices.deleteOtp(email);
            RedirectUtilities.sendRedirect(request, response, Constants.CUSTOMER_LOGIN_URL);
            return true;
        }

        RedirectUtilities.setErrorMessage(request, "Failed to Register!");
        return false;
    }

    private boolean validateRegisterRequest(RegisterRequest registerRequest) {
        if (registerRequest == null) {
            return false;
        }
        if (StringUtilities.anyNullOrBlank(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getConfirm_password(), registerRequest.getEmail(), registerRequest.getAddress(), registerRequest.getPhoneNumber(), registerRequest.getGender(), registerRequest.getOtp())) {
            return false;
        }
        if (!registerRequest.getPassword().equals(registerRequest.getConfirm_password())) {
            return false;
        }
        if (registerRequest.getPassword().length() < 8) {
            return false;
        }
        if (!registerRequest.getEmail().contains("@") || !registerRequest.getEmail().contains(".")) {
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
