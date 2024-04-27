package controllers;

import common.Common;
import common.Constants;
import entities.Session;
import entities.Customers;
import exceptions.DatabaseException;
import features.AesProtector;
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
import dao.OtpDao;
import utilities.RedirectUtilities;
import utilities.RedirectUtilities.RedirectType;
import utilities.StringUtilities;

public class LoginServlet extends HttpServlet {

    private static final String LOGIN_2FA_URL = "/sessions/login2fa";
    private final OtpDao otpDao = new OtpDao();
    private final SessionChecker sessionHandler = new SessionChecker();
    @PersistenceContext
    EntityManager entityManager;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = sessionHandler.getLoginSession(request.getSession());
        if (session.isResult()) {
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
        request.getRequestDispatcher(Constants.LOGIN_JSP_URL).forward(request, response);
    }

    private Customers tryCustomerLogin(Customers customer) throws DatabaseException {
        try {
            String encryptedPassword = AesProtector.aes256EcbEncrypt(customer.getPassword());
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
        HttpSession session = request.getSession(true);
        if (!customer.getTwoFactorAuth()) {
            sessionHandler.setLoginSession(session, customer.getUserId());
            RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
        } else {
            requiredTwoFactorAuth(request, response, customer, session);
        }
    }

    private void requiredTwoFactorAuth(HttpServletRequest request, HttpServletResponse response, Customers customer, HttpSession session) throws ServletException, IOException {
        if (otpDao.sendOtp(customer.getEmail()) != Common.Status.OK) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "There was an error from the server! Please try again later.");
            return;
        }
        session.setAttribute("login_id_2fa", customer.getUserId());
        session.setAttribute("email", customer.getEmail());
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
