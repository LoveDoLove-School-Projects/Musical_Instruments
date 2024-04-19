package controllers;

import domain.common.Common;
import domain.common.Constants;
import domain.models.Session;
import domain.request.RegisterRequest;
import features.SessionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import services.RegisterServices;
import utilities.RedirectUtilities;
import utilities.RedirectUtilities.RedirectType;
import utilities.StringUtilities;

public class RegisterServlet extends HttpServlet {

    private final RegisterServices registerServices = new RegisterServices();
    private final SessionHandler sessionHandler = new SessionHandler();

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
        Session session = sessionHandler.getLoginSession(request.getSession());
        if (session.isResult()) {
            RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
            return;
        }
        String path = request.getServletPath();
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            switch (path) {
                case Constants.REGISTER_URL:
                    Common.Status registerStatus = addNewCustomer(request);
                    handleRegisterStatus(registerStatus, request, response);
                    return;
            }
        }
        setRegisterPage(request, response);
    }

    private Common.Status addNewCustomer(HttpServletRequest request) throws ServletException, IOException {
        RegisterRequest registerRequest = createRegisterRequest(request);
        if (!validateRegisterRequest(registerRequest)) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Please Fill In All The Fields!");
            return Common.Status.INVALID;
        }
        if (!registerRequest.getPassword().equals(registerRequest.getConfirm_password())) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Password and Confirm Password should be same!");
            return Common.Status.INVALID;
        }
        return registerServices.addNewCustomer(registerRequest);
    }

    private void handleRegisterStatus(Common.Status registerStatus, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        switch (registerStatus) {
            case OK:
                RedirectUtilities.redirectWithMessage(request, response, RedirectType.SUCCESS, "Registered Successfully!", Constants.CUSTOMER_LOGIN_URL);
                return;
            case EXISTS:
                RedirectUtilities.setMessage(request, RedirectType.DANGER, "Email Already Exists!");
                break;
            default:
                RedirectUtilities.setMessage(request, RedirectType.DANGER, "Failed to Register!");
                break;
        }
        setRegisterPage(request, response);
    }

    private RegisterRequest createRegisterRequest(HttpServletRequest request) {
        String username = request.getParameter(Constants.USERNAME_ATTRIBUTE);
        String password = request.getParameter(Constants.PASSWORD_ATTRIBUTE);
        String confirm_password = request.getParameter("confirm_password");
        String email = request.getParameter(Constants.EMAIL_ATTRIBUTE);
        String address = request.getParameter(Constants.ADDRESS_ATTRIBUTE);
        String phone_number = request.getParameter(Constants.PHONE_ATTRIBUTE);
        String gender = request.getParameter(Constants.GENDER_ATTRIBUTE);
        request.setAttribute(Constants.USERNAME_ATTRIBUTE, username);
        request.setAttribute(Constants.EMAIL_ATTRIBUTE, email);
        request.setAttribute(Constants.ADDRESS_ATTRIBUTE, address);
        request.setAttribute(Constants.PHONE_ATTRIBUTE, phone_number);
        request.setAttribute(Constants.GENDER_ATTRIBUTE, gender);
        return new RegisterRequest(username, password, confirm_password, email, address, phone_number, gender);
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
