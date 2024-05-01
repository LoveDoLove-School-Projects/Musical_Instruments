package controllers;

import common.Constants;
import entities.Customers;
import static entities.Role.CUSTOMER;
import entities.Session;
import entities.Staffs;
import exceptions.DatabaseException;
import features.SecurityLog;
import features.SessionChecker;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
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

@MultipartConfig
public class RemoveProfilePictureServlet extends HttpServlet {

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
        boolean isRemoved = false;
        switch (session.getRole()) {
            case CUSTOMER:
                Customers customer = new Customers(session.getUserId());
                isRemoved = removePicture(customer);
                break;
            case STAFF:
                Staffs staff = new Staffs(session.getUserId());
                isRemoved = removePicture(staff);
                break;
        }
        if (isRemoved) {
            SecurityLog.addSecurityLog(request, "Removed picture.");
            RedirectUtilities.setMessage(request, RedirectType.SUCCESS, "Picture removed successfully.");
        } else {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Error removing picture.");
        }
        RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
    }

    private boolean removePicture(Customers customer) {
        try {
            userTransaction.begin();
            Customers existingCustomer = entityManager.find(Customers.class, customer.getUserId());
            if (existingCustomer == null) {
                return false;
            }
            existingCustomer.setPicture(null);
            entityManager.merge(existingCustomer);
            userTransaction.commit();
            return true;
        } catch (RollbackException ex) {
            return false;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | SystemException | IllegalStateException | SecurityException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    private boolean removePicture(Staffs staff) {
        try {
            userTransaction.begin();
            Staffs existingStaff = entityManager.find(Staffs.class, staff.getUserId());
            if (existingStaff == null) {
                return false;
            }
            existingStaff.setPicture(null);
            entityManager.merge(existingStaff);
            userTransaction.commit();
            return true;
        } catch (RollbackException ex) {
            return false;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | SystemException | IllegalStateException | SecurityException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }
}
