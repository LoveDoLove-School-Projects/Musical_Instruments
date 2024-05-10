package controllers;

import entities.Constants;
import entities.Role;
import entities.Session;
import entities.Staffs;
import exceptions.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import services.OtpServices;
import utilities.AesUtilities;
import utilities.RedirectUtilities;
import utilities.RedirectUtilities.RedirectType;
import utilities.SecurityLog;
import utilities.SessionUtilities;
import utilities.StringUtilities;

public class LoginStaffServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    private static final Logger LOG = Logger.getLogger(LoginStaffServlet.class.getName());
    private static final String STAFF_LOGIN_JSP_URL = "/pages/staffLogin.jsp";
    private static final String LOGIN_2FA_URL = "/sessions/login2fa";
    private final OtpServices otpServices = new OtpServices();

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
        Staffs staff = new Staffs(email, password);
        request.setAttribute("email", email);
        if (!validateLoginRequest(staff)) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Please Fill In All The Fields!");
            setLoginPage(request, response);
            return;
        }
        staff = tryStaffLogin(staff);
        if (staff == null) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Login Failed! Please Try Again!");
            setLoginPage(request, response);
            return;
        }
        checkNeedTwoFactorAuthOrNot(request, response, staff);
    }

    private void setLoginPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(STAFF_LOGIN_JSP_URL).forward(request, response);
    }

    private Staffs tryStaffLogin(Staffs staff) {
        try {
            String encryptedPassword = AesUtilities.aes256EcbEncrypt(staff.getPassword());
            List<Staffs> staffList = entityManager.createNamedQuery("Staffs.findByEmailAndPassword", Staffs.class)
                    .setParameter("email", staff.getEmail())
                    .setParameter("password", encryptedPassword)
                    .getResultList();
            return staffList.isEmpty() ? null : staffList.get(0);
        } catch (Exception ex) {
            LOG.severe(ex.getMessage());
            return null;
        }
    }

    private void checkNeedTwoFactorAuthOrNot(HttpServletRequest request, HttpServletResponse response, Staffs staff) throws ServletException, IOException {
        HttpSession httpSession = request.getSession(true);
        if (!staff.getTwoFactorAuth()) {
            Session session = new Session(staff.getUserId(), staff.getUsername(), staff.getEmail(), Role.STAFF);
            SessionUtilities.setLoginSession(httpSession, session);
            SecurityLog.addSecurityLog(request, "login successful.");
            RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
            return;
        }
        requiredTwoFactorAuth(request, response, staff, httpSession);
    }

    private void requiredTwoFactorAuth(HttpServletRequest request, HttpServletResponse response, Staffs staff, HttpSession session) throws ServletException, IOException {
        if (!otpServices.sendOtp(staff.getEmail())) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "There was an error from the server! Please try again later.");
            return;
        }
        session.setAttribute("login_id_2fa", staff.getUserId());
        session.setAttribute("username", staff.getUsername());
        session.setAttribute("email", staff.getEmail());
        session.setAttribute("role", Role.STAFF);
        RedirectUtilities.sendRedirect(request, response, LOGIN_2FA_URL);
    }

    private boolean validateLoginRequest(Staffs staff) {
        return staff != null
                && !StringUtilities.anyNullOrBlank(staff.getPassword(), staff.getEmail())
                && staff.getPassword().length() >= 8
                && staff.getEmail().contains("@")
                && staff.getEmail().contains(".");
    }
}
