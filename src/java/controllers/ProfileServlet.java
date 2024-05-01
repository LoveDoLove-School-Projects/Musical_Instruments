package controllers;

import common.Constants;
import entities.Customers;
import entities.Session;
import entities.Staffs;
import features.SessionChecker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import utilities.RedirectUtilities;
import utilities.RedirectUtilities.RedirectType;

@MultipartConfig
public class ProfileServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    private static final String PROFILE_JSP_URL = "/pages/profile.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = SessionChecker.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Please login to view this page.", "/");
            return;
        }
        boolean isInit;
        switch (session.getRole()) {
            case CUSTOMER:
                isInit = initCustomerProfile(request, session);
                break;
            case STAFF:
                isInit = initStaffProfile(request, session);
                break;
            default:
                RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Invalid role.", "/");
                return;
        }
        if (!isInit) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Error fetching profile details.", "/");
            return;
        }
        request.getRequestDispatcher(PROFILE_JSP_URL).forward(request, response);
    }

    private boolean initCustomerProfile(HttpServletRequest request, Session session) {
        Customers existingCustomer = entityManager.find(Customers.class, session.getUserId());
        if (existingCustomer == null) {
            return false;
        }
        request.setAttribute("username", existingCustomer.getUsername());
        request.setAttribute("email", existingCustomer.getEmail());
        request.setAttribute("address", existingCustomer.getAddress());
        request.setAttribute("phone_number", existingCustomer.getPhoneNumber());
        request.setAttribute("gender", existingCustomer.getGender());
        request.setAttribute("two_factor_auth", existingCustomer.getTwoFactorAuth());
        byte[] picture = existingCustomer.getPicture();
        if (picture != null) {
            String pictureBase64 = Base64.getEncoder().encodeToString(picture);
            request.setAttribute("pictureBase64", pictureBase64);
        }
        return true;
    }

    private boolean initStaffProfile(HttpServletRequest request, Session session) {
        Staffs existingStaff = entityManager.find(Staffs.class, session.getUserId());
        if (existingStaff == null) {
            return false;
        }
        request.setAttribute("username", existingStaff.getUsername());
        request.setAttribute("email", existingStaff.getEmail());
        request.setAttribute("address", existingStaff.getAddress());
        request.setAttribute("phone_number", existingStaff.getPhoneNumber());
        request.setAttribute("gender", existingStaff.getGender());
        request.setAttribute("two_factor_auth", existingStaff.getTwoFactorAuth());
        byte[] picture = existingStaff.getPicture();
        if (picture != null) {
            String pictureBase64 = Base64.getEncoder().encodeToString(picture);
            request.setAttribute("pictureBase64", pictureBase64);
        }
        return true;
    }
}
