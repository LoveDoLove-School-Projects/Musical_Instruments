package controllers;

import common.Constants;
import entities.Customers;
import static entities.Role.CUSTOMER;
import static entities.Role.STAFF;
import entities.Session;
import entities.Staffs;
import exceptions.DatabaseException;
import features.SecurityLog;
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
import utilities.RedirectUtilities;
import utilities.RedirectUtilities.RedirectType;
import utilities.StringUtilities;

public class UpdateProfileServlet extends HttpServlet {

    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = SessionChecker.getLoginSession(request.getSession());
        if (session == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Please login to view this page.", "/");
            return;
        }
        String username = request.getParameter("username");
        String address = request.getParameter("address");
        String phoneNumber = request.getParameter("phone_number");
        String gender = request.getParameter("gender");
        boolean two_factor_auth = request.getParameter("two_factor_auth") != null;
        if (StringUtilities.anyNullOrBlank(username, address, phoneNumber, gender)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "All fields are required.", Constants.PROFILE_URL);
            return;
        }
        boolean isUpdated = false;
        switch (session.getRole()) {
            case CUSTOMER:
                Customers customer = new Customers(session.getUserId(), username, address, phoneNumber, gender, two_factor_auth);
                isUpdated = updateProfile(customer);
                break;
            case STAFF:
                Staffs staff = new Staffs(session.getUserId(), username, address, phoneNumber, gender, two_factor_auth);
                isUpdated = updateProfile(staff);
                break;
        }
        if (isUpdated) {
            SecurityLog.addSecurityLog(request, "Updated profile.");
            RedirectUtilities.setMessage(request, RedirectType.SUCCESS, "Profile updated successfully.");
        } else {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Error updating profile.");
        }
        RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
    }

    private boolean updateProfile(Customers customer) {
        try {
            userTransaction.begin();
            Customers existingCustomer = entityManager.find(Customers.class, customer.getUserId());
            if (existingCustomer == null) {
                return false;
            }
            existingCustomer.setUsername(customer.getUsername());
            existingCustomer.setAddress(customer.getAddress());
            existingCustomer.setPhoneNumber(customer.getPhoneNumber());
            existingCustomer.setGender(customer.getGender());
            existingCustomer.setTwoFactorAuth(customer.getTwoFactorAuth());
            entityManager.merge(existingCustomer);
            userTransaction.commit();
            return true;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | SecurityException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    private boolean updateProfile(Staffs staff) {
        try {
            userTransaction.begin();
            Staffs existingStaff = entityManager.find(Staffs.class, staff.getUserId());
            if (existingStaff == null) {
                return false;
            }
            existingStaff.setUsername(staff.getUsername());
            existingStaff.setAddress(staff.getAddress());
            existingStaff.setPhoneNumber(staff.getPhoneNumber());
            existingStaff.setGender(staff.getGender());
            existingStaff.setTwoFactorAuth(staff.getTwoFactorAuth());
            entityManager.merge(existingStaff);
            userTransaction.commit();
            return true;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | RollbackException | SystemException | IllegalStateException | SecurityException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }
}
