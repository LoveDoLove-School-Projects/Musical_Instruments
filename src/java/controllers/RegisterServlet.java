package controllers;

import domain.common.Common;
import domain.common.Constants;
import domain.models.Session;
import domain.request.RegisterRequest;
import domain.response.RegisterResponse;
import features.SessionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import services.OtpServices;
import services.RegisterServices;
import utilities.RedirectUtilities;
import utilities.StringUtilities;
import utilities.enums.RedirectType;

public class RegisterServlet extends HttpServlet {

    private static final RegisterServices registerServices = new RegisterServices();
    private static final OtpServices otpServices = new OtpServices();
    private static final SessionHandler sessionHandler = new SessionHandler();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRegister(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRegister(request, response);
    }

    private void setRegisterPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(Constants.REGISTER_JSP_URL).forward(request, response);
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = sessionHandler.getLoginSession(request, response);
        if (session.isResult()) {
            RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
            return;
        }
        String path = request.getServletPath();
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            boolean isRegistered = false;
            switch (path) {
                case Constants.REGISTER_URL:
                    isRegistered = registerCustomer(request, response);
                    break;
            }
            if (isRegistered) {
                return;
            }
        }
        setRegisterPage(request, response);
    }

    private boolean registerCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirm_password = request.getParameter("confirm_password");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String phone_number = request.getParameter("phone_number");
        String gender = request.getParameter("gender");

        request.setAttribute("username", username);
        request.setAttribute("email", email);
        request.setAttribute("address", address);
        request.setAttribute("phone_number", phone_number);
        request.setAttribute("gender", gender);

        RegisterRequest registerRequest = new RegisterRequest(username, password, confirm_password, email, address, phone_number, gender);

        if (!validateRegisterRequest(registerRequest)) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Please Fill In All The Fields!");
            return false;
        }

        if (!password.equals(confirm_password)) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Password and Confirm Password should be same!");
            return false;
        }

        RegisterResponse registerResponse = registerServices.addNewCustomer(registerRequest);

        if (registerResponse.getStatus() == null) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Failed to Register!");
            return false;
        }

        if (registerResponse.getStatus() == Common.Status.EXISTS) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Email Already Exists!");
            return false;
        }

        if (registerResponse.getStatus() == Common.Status.OK) {
            otpServices.deleteOtp(email);
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.SUCCESS, "Registered Successfully!", Constants.CUSTOMER_LOGIN_URL);
            return true;
        }

        RedirectUtilities.setMessage(request, RedirectType.DANGER, "Failed to Register!");
        return false;
    }

    private boolean validateRegisterRequest(RegisterRequest registerRequest) {
        if (registerRequest == null) {
            return false;
        }
        if (StringUtilities.anyNullOrBlank(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getConfirm_password(), registerRequest.getEmail(), registerRequest.getAddress(), registerRequest.getPhoneNumber(), registerRequest.getGender())) {
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
