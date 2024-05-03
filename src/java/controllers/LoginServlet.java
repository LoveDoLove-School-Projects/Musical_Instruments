package controllers;

import common.Constants;
import dao.OtpDao;
import entities.Customers;
import entities.Role;
import entities.Session;
import exceptions.DatabaseException;
import utilities.AesUtilities;
import utilities.SecurityLog;
import utilities.SessionUtilities;
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

public class LoginServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    private static final String LOGIN_JSP_URL = "/pages/login.jsp";
    private static final String LOGIN_2FA_URL = "/sessions/login2fa";
    private final OtpDao otpDao = new OtpDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = SessionUtilities.getLoginSession(request.getSession());
        if (session != null) {
            RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
            return;
        }
        switch (request.getMethod()) {
            case "GET":
                setLoginPage(request, response);
                break;
            case "POST":
                processPostRequest(request, response);
                break;
        }
    }

    private void processPostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Customers customer = new Customers(email, password);
        request.setAttribute("email", email);
        if (!validateLoginRequest(customer)) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Please Fill In All The Fields!");
            setLoginPage(request, response);
            return;
        }
        customer = tryCustomerLogin(customer);
        if (customer == null) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Login Failed! Please Try Again!");
            setLoginPage(request, response);
            return;
        }
        checkNeedTwoFactorAuthOrNot(request, response, customer);
    }

    private void setLoginPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(LOGIN_JSP_URL).forward(request, response);
    }

    private Customers tryCustomerLogin(Customers customer) throws DatabaseException {
        try {
            String encryptedPassword = AesUtilities.aes256EcbEncrypt(customer.getPassword());
            List<Customers> customerList = entityManager.createNamedQuery("Customers.findByEmailAndPassword", Customers.class)
                    .setParameter("email", customer.getEmail())
                    .setParameter("password", encryptedPassword)
                    .getResultList();
            return customerList.isEmpty() ? null : customerList.get(0);
        } catch (Exception ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    private void checkNeedTwoFactorAuthOrNot(HttpServletRequest request, HttpServletResponse response, Customers customer) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(true);
        if (!customer.getTwoFactorAuth()) {
            Session session = new Session(customer.getUserId(), customer.getUsername(), customer.getEmail(), Role.CUSTOMER);
            SessionUtilities.setLoginSession(httpSession, session);
            SecurityLog.addSecurityLog(request, "login successful.");
            RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
            return;
        }
        requiredTwoFactorAuth(request, response, customer, httpSession);
    }

    private void requiredTwoFactorAuth(HttpServletRequest request, HttpServletResponse response, Customers customer, HttpSession session) throws ServletException, IOException {
        if (!otpDao.sendOtp(customer.getEmail())) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "There was an error from the server! Please try again later.");
            return;
        }
        session.setAttribute("login_id_2fa", customer.getUserId());
        session.setAttribute("username", customer.getUsername());
        session.setAttribute("email", customer.getEmail());
        session.setAttribute("role", Role.CUSTOMER);
        RedirectUtilities.sendRedirect(request, response, LOGIN_2FA_URL);
    }

    private boolean validateLoginRequest(Customers customer) {
        return customer != null
                && !StringUtilities.anyNullOrBlank(customer.getPassword(), customer.getEmail())
                && customer.getPassword().length() >= 8
                && customer.getEmail().contains("@")
                && customer.getEmail().contains(".");
    }
}
