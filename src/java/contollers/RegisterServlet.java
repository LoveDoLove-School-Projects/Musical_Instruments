package contollers;

import common.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import request.RegisterRequest;
import response.RegisterResponse;
import services.MailServices;
import services.RegisterServices;
import utilities.JsonUtilities;
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
                    if (!registerCustomer(request, response)) {

                        request.getRequestDispatcher(Constants.REGISTER_JSP_URL).forward(request, response);

                    }
                    return;
                case Constants.REGISTER_SEND_OTP_URL:
                    sendOtp(request, response);
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

        RegisterResponse registerResponse = registerServices.getOtp(email);

        switch (registerResponse.getStatus()) {
            case INTERNAL_SERVER_ERROR:
                RedirectUtilities.setErrorMessage(request, "Internal Server Error!");
                return false;
            case NOT_FOUND:
                RedirectUtilities.setErrorMessage(request, "Email Not Found!");
                return false;
            case OK:
                if (!otp.equals(registerResponse.getOtp())) {
                    RedirectUtilities.setErrorMessage(request, "Invalid OTP!");
                    return false;
                }
                break;
        }

        registerResponse = registerServices.addNewCustomer(registerRequest);

        switch (registerResponse.getStatus()) {
            case INTERNAL_SERVER_ERROR:
                RedirectUtilities.setErrorMessage(request, "Internal Server Error!");
                return false;
            case EXISTS:
                RedirectUtilities.setErrorMessage(request, "Email Already Exists!");
                return false;
            case OK:
                RedirectUtilities.setSuccessMessage(request, "Registered Successfully!");
                sendWelcomeMessage(username, email);
                RedirectUtilities.sendRedirect(request, response, Constants.LOGIN_URL);
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

    private void sendOtp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        if (StringUtilities.anyNullOrBlank(email)) {
            JsonUtilities.sendErrorResponse(response, "Please fill in the email field!");
            return;
        }
        RegisterResponse registerResponse = registerServices.sendOtp(email);
        switch (registerResponse.getStatus()) {
            case INTERNAL_SERVER_ERROR:
                JsonUtilities.sendErrorResponse(response, "Internal Server Error!");
                return;
            case OK:
                JsonUtilities.sendSuccessResponse(response, "OTP Sent Successfully!");
                return;
        }
        JsonUtilities.sendErrorResponse(response, "Failed to send OTP!");
    }

    private void sendWelcomeMessage(String username, String email) {
        String subject = String.format("Welcome to %s %s", username, Constants.COMPANY_NAME);
        String body = String.format("Hey %s! Welcome to our store!\n"
                + "\n"
                + "Thank you for creating a %s account. We are more than happy to have you on board.\n"
                + "\n"
                + "Please make yourself enjoy shopping with us.\n"
                + "\n"
                + "But before you start, here’s a short tutorial we created for our new clients. It will help you find the best fit based on size, color, and body type.\n"
                + "\n"
                + "See you around,\n"
                + "%s Team", username, Constants.COMPANY_NAME, Constants.COMPANY_NAME);
        new MailServices().sendEmail(email, subject, body);
    }
}
