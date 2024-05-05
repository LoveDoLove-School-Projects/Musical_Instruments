package controllers;

import common.Constants;
import entities.Customers;
import entities.OtpsType;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import java.io.IOException;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;
import services.OtpServices;
import utilities.AesUtilities;
import utilities.RedirectUtilities;
import utilities.RedirectUtilities.RedirectType;
import utilities.StringUtilities;

public class Register2FAServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;
    private static final String REGISTER_2FA_JSP_URL = "/sessions/register2fa.jsp";
    private static final String REGISTER_URL = "/pages/register";
    private static final String REGISTER_2FA_URL = "/sessions/register2fa";
    private static final String RESEND_REGISTER_OTP_URL = "/sessions/resendRegisterOtp";
    private static final Map<OtpsType, String> STATUS_MESSAGES;

    static {
        STATUS_MESSAGES = new EnumMap<>(OtpsType.class);
        STATUS_MESSAGES.put(OtpsType.NOT_FOUND, "OTP not found!");
        STATUS_MESSAGES.put(OtpsType.UNAUTHORIZED, "Too many attempts! Please click on 'Resend OTP' to try again.");
        STATUS_MESSAGES.put(OtpsType.EXPIRED, "OTP expired! Please click on 'Resend OTP' to try again.");
        STATUS_MESSAGES.put(OtpsType.FAILED, "Failed to verify OTP! Please try again.");
        STATUS_MESSAGES.put(OtpsType.INVALID, "Invalid OTP!");
    }
    private final OtpServices otpServices = new OtpServices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Customers customer = getSessionAttributes(session);
        if (customer == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Invalid request!", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        setRegister2FAPage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        HttpSession session = request.getSession();
        Customers customer = getSessionAttributes(session);
        if (customer == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Invalid request!", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        switch (request.getServletPath()) {
            case RESEND_REGISTER_OTP_URL:
                resendLoginOtp(request, response, customer);
                break;
            default:
                verifyLoginOtp(request, response, session, customer);
                break;
        }
    }

    private void resendLoginOtp(HttpServletRequest request, HttpServletResponse response, Customers attributes) throws IOException, ServletException {
        boolean otpStatus = otpServices.sendOtp(attributes.getEmail());
        if (otpStatus) {
            RedirectUtilities.setMessage(request, RedirectType.SUCCESS, "OTP sent successfully!");
        } else {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Failed to send OTP!");
        }
        RedirectUtilities.sendRedirect(request, response, REGISTER_2FA_URL);
    }

    private void verifyLoginOtp(HttpServletRequest request, HttpServletResponse response, HttpSession session, Customers customer) throws IOException, ServletException {
        String otp = request.getParameter("otp");
        if (StringUtilities.anyNullOrBlank(otp)) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Please fill in OTP!");
            setRegister2FAPage(request, response);
            return;
        }
        OtpsType otpStatus = otpServices.verifyOtp(customer.getEmail(), otp);
        handleOtpStatus(otpStatus, request, response, session, customer);
    }

    private void setRegister2FAPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(REGISTER_2FA_JSP_URL).forward(request, response);
    }

    private Customers getSessionAttributes(HttpSession session) {
        if (session == null) {
            return null;
        }
        return (Customers) session.getAttribute("customer");
    }

    private void handleOtpStatus(OtpsType otpStatus, HttpServletRequest request, HttpServletResponse response, HttpSession session, Customers customer) throws IOException, ServletException {
        if (otpStatus == OtpsType.OK) {
            session.invalidate();
            boolean isRegistered = registerNewCustomer(customer);
            if (isRegistered) {
                RedirectUtilities.redirectWithMessage(request, response, RedirectType.SUCCESS, "Registered Successfully!", Constants.CUSTOMER_LOGIN_URL);
            } else {
                RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Failed to register! Please try again.", REGISTER_URL);
            }
        } else {
            String message = STATUS_MESSAGES.getOrDefault(otpStatus, "Failed to verify OTP!");
            RedirectUtilities.setMessage(request, RedirectType.DANGER, message);
            setRegister2FAPage(request, response);
        }
    }

    private boolean registerNewCustomer(Customers customer) {
        customer.setUsername(customer.getUsername());
        customer.setPassword(AesUtilities.aes256EcbEncrypt(customer.getPassword()));
        customer.setEmail(customer.getEmail());
        customer.setAddress(customer.getAddress());
        customer.setPhoneNumber(customer.getPhoneNumber());
        customer.setGender(customer.getGender());
        customer.setTwoFactorAuth(false);
        customer.setAccountCreationDate(new Date());
        try {
            userTransaction.begin();
            entityManager.persist(customer);
            userTransaction.commit();
            return true;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | SecurityException ex) {
            try {
                userTransaction.rollback();
            } catch (SystemException | IllegalStateException | SecurityException e) {
            }
            return false;
        }
    }
}
