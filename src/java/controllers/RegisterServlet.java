package controllers;

import domain.common.Common;
import domain.common.Constants;
import domain.models.Session;
import domain.request.RegisterRequest;
import entities.Customers;
import features.AesHandler;
import features.SessionHandler;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import java.io.IOException;
import java.util.List;
import utilities.RedirectUtilities;
import utilities.RedirectUtilities.RedirectType;
import utilities.StringUtilities;
import utilities.ValidationUtilities;

public class RegisterServlet extends HttpServlet {

    private final SessionHandler sessionHandler = new SessionHandler();
    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;

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
        return registerNewCustomer(registerRequest);
    }

    private Common.Status registerNewCustomer(RegisterRequest registerRequest) {
        Customers customer = new Customers();
        customer.setUsername(registerRequest.getUsername());
        customer.setPassword(AesHandler.aes256EcbEncrypt(registerRequest.getPassword()));
        customer.setEmail(registerRequest.getEmail());
        customer.setAddress(registerRequest.getAddress());
        customer.setPhoneNumber(registerRequest.getPhoneNumber());
        customer.setGender(registerRequest.getGender());
        customer.setTwoFactorAuth(false);
        if (doesEmailExist(registerRequest.getEmail())) {
            return Common.Status.EXISTS;
        }
        try {
            userTransaction.begin();
            entityManager.persist(customer);
            userTransaction.commit();
            return Common.Status.OK;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | SecurityException ex) {
            try {
                userTransaction.rollback();
            } catch (SystemException | IllegalStateException | SecurityException e) {
            }
            return Common.Status.INTERNAL_SERVER_ERROR;
        }
    }

    private boolean doesEmailExist(String email) {
        try {
            List<Customers> customers = entityManager.createNamedQuery("Customers.findByEmail", Customers.class).setParameter("email", email).getResultList();
            return !customers.isEmpty();
        } catch (Exception ex) {
            return false;
        }
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
        return new RegisterRequest(username, password, confirm_password, email, address, phone_number, gender);
    }

    private boolean validateRegisterRequest(RegisterRequest registerRequest) {
        if (registerRequest == null) {
            return false;
        }
        if (StringUtilities.anyNullOrBlank(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getConfirm_password(), registerRequest.getEmail(), registerRequest.getAddress(), registerRequest.getPhoneNumber(), registerRequest.getGender())) {
            return false;
        }
        if (!ValidationUtilities.comparePasswords(registerRequest.getPassword(), registerRequest.getConfirm_password())) {
            return false;
        }
        if (registerRequest.getPassword().length() < 8) {
            return false;
        }
        if (!ValidationUtilities.isEmailValid(registerRequest.getEmail())) {
            return false;
        }
        return ValidationUtilities.isPhoneNumberValid(registerRequest.getPhoneNumber());
    }
}
