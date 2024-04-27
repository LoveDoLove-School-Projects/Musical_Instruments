package controllers;

import common.Common;
import common.Constants;
import entities.Customers;
import entities.Session;
import features.AesProtector;
import features.SessionChecker;
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
import java.util.Date;
import java.util.List;
import utilities.RedirectUtilities;
import utilities.RedirectUtilities.RedirectType;
import utilities.StringUtilities;
import utilities.ValidationUtilities;

public class RegisterServlet extends HttpServlet {

    private final SessionChecker sessionHandler = new SessionChecker();
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
        Customers customer = new Customers(username, password, confirm_password, email, address, phone_number, gender);
        if (!validateCustomerDetails(customer)) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Please Fill In All The Fields!");
            return Common.Status.INVALID;
        }
        if (!customer.getPassword().equals(confirm_password)) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Password and Confirm Password should be same!");
            return Common.Status.INVALID;
        }
        return registerNewCustomer(customer);
    }

    private Common.Status registerNewCustomer(Customers customer) {
        customer.setUsername(customer.getUsername());
        customer.setPassword(AesProtector.aes256EcbEncrypt(customer.getPassword()));
        customer.setEmail(customer.getEmail());
        customer.setAddress(customer.getAddress());
        customer.setPhoneNumber(customer.getPhoneNumber());
        customer.setGender(customer.getGender());
        customer.setTwoFactorAuth(false);
        customer.setAccountCreationDate(new Date());
        if (doesEmailExist(customer.getEmail())) {
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

    private boolean validateCustomerDetails(Customers customer) {
        if (customer == null) {
            return false;
        }
        if (StringUtilities.anyNullOrBlank(customer.getUsername(), customer.getPassword(), customer.getEmail(), customer.getAddress(), customer.getPhoneNumber(), customer.getGender())) {
            return false;
        }
        if (customer.getPassword().length() < 8) {
            return false;
        }
        if (!ValidationUtilities.isEmailValid(customer.getEmail())) {
            return false;
        }
        return ValidationUtilities.isPhoneNumberValid(customer.getPhoneNumber());
    }
}
