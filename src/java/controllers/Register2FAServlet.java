package controllers;

import common.Constants;
import dao.OtpDao;
import entities.Customers;
import entities.OtpStatus;
import features.AesProtector;
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
import utilities.RedirectUtilities;
import utilities.RedirectUtilities.RedirectType;
import utilities.StringUtilities;

public class Register2FAServlet extends HttpServlet {

    private static final String REGISTER_2FA_JSP_URL = "/sessions/register2fa.jsp";
    private static final Map<OtpStatus, String> STATUS_MESSAGES;

    static {
        STATUS_MESSAGES = new EnumMap<>(OtpStatus.class);
        STATUS_MESSAGES.put(OtpStatus.NOT_FOUND, "OTP not found!");
        STATUS_MESSAGES.put(OtpStatus.UNAUTHORIZED, "Too many attempts! Please click on 'Resend OTP' to try again.");
        STATUS_MESSAGES.put(OtpStatus.EXPIRED, "OTP expired! Please click on 'Resend OTP' to try again.");
        STATUS_MESSAGES.put(OtpStatus.FAILED, "Failed to verify OTP! Please try again.");
        STATUS_MESSAGES.put(OtpStatus.INVALID, "Invalid OTP!");
    }
    private final OtpDao otpDao = new OtpDao();
    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
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
        HttpSession session = request.getSession(false);
        Customers customer = getSessionAttributes(session);
        if (customer == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Invalid request!", Constants.CUSTOMER_LOGIN_URL);
            return;
        }
        String otp = request.getParameter("otp");
        if (StringUtilities.anyNullOrBlank(otp)) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Please fill in OTP!");
            setRegister2FAPage(request, response);
            return;
        }
        OtpStatus otpStatus = otpDao.verifyOtp(customer.getEmail(), otp);
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

    private void handleOtpStatus(OtpStatus otpStatus, HttpServletRequest request, HttpServletResponse response, HttpSession session, Customers customer) throws IOException, ServletException {
        if (otpStatus == OtpStatus.OK) {
            session.invalidate();
            boolean isRegistered = registerNewCustomer(customer);
            if (isRegistered) {
                RedirectUtilities.redirectWithMessage(request, response, RedirectType.SUCCESS, "Registered Successfully!", Constants.CUSTOMER_LOGIN_URL);
            } else {
                RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Failed to register! Please try again.", Constants.REGISTER_URL);
            }
        } else {
            String message = STATUS_MESSAGES.getOrDefault(otpStatus, "Failed to verify OTP!");
            RedirectUtilities.setMessage(request, RedirectType.DANGER, message);
            setRegister2FAPage(request, response);
        }
    }

    private boolean registerNewCustomer(Customers customer) {
        customer.setUsername(customer.getUsername());
        customer.setPassword(AesProtector.aes256EcbEncrypt(customer.getPassword()));
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
