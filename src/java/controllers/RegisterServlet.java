package controllers;

import common.Constants;
import dao.OtpDao;
import entities.Customers;
import entities.Session;
import features.SessionChecker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import utilities.RedirectUtilities;
import utilities.RedirectUtilities.RedirectType;
import utilities.StringUtilities;
import utilities.ValidationUtilities;

public class RegisterServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    public static final String REGISTER_JSP_URL = "/pages/register.jsp";
    private static final String REGISTER_2FA_URL = "/sessions/register2fa";
    private final OtpDao otpDao = new OtpDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = SessionChecker.getLoginSession(request.getSession());
        if (session != null) {
            RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
            return;
        }
        setRegisterPage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Customers customer = addNewCustomer(request);
        if (customer == null) {
            setRegisterPage(request, response);
            return;
        }
        if (!otpDao.sendOtp(customer.getEmail())) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "There was an error from the server! Please try again later.");
            return;
        }
        HttpSession session = request.getSession();
        session.setAttribute("email", customer.getEmail());
        session.setAttribute("customer", customer);
        RedirectUtilities.sendRedirect(request, response, REGISTER_2FA_URL);
    }

    private void setRegisterPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(REGISTER_JSP_URL).forward(request, response);
    }

    private Customers addNewCustomer(HttpServletRequest request) throws ServletException, IOException {
        Customers customer = createCustomer(request);
        String confirm_password = request.getParameter("confirm_password");
        if (!validateCustomerDetails(customer)) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Please Fill In All The Fields!");
            return null;
        }
        if (!customer.getPassword().equals(confirm_password)) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Password and Confirm Password should be same!");
            return null;
        }
        if (doesEmailExist(customer.getEmail())) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Email Already Exists!");
            return null;
        }
        return customer;
    }

    private Customers createCustomer(HttpServletRequest request) throws ServletException, IOException {
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
        return new Customers(username, password, email, address, phone_number, gender);
    }

    private boolean doesEmailExist(String email) {
        try {
            List<Customers> customers = entityManager.createNamedQuery("Customers.findByEmail", Customers.class).setParameter("email", email).getResultList();
            return !customers.isEmpty();
        } catch (Exception ex) {
            return false;
        }
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
