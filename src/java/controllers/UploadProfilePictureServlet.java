package controllers;

import common.Constants;
import entities.Customers;
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
import java.io.InputStream;
import utilities.RedirectUtilities;
import utilities.RedirectUtilities.RedirectType;

@MultipartConfig
public class UploadProfilePictureServlet extends HttpServlet {

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
        InputStream pictureStream = request.getPart("uploadPicture").getInputStream();
        if (pictureStream == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Error uploading picture.", Constants.PROFILE_URL);
            return;
        }
        byte[] pictureBytes = pictureStream.readAllBytes();
        boolean isUploaded;
        switch (session.getRole()) {
            case CUSTOMER:
                Customers customer = new Customers(session.getUserId(), pictureBytes);
                isUploaded = uploadPicture(customer);
                break;
            case STAFF:
                Staffs staff = new Staffs(session.getUserId(), pictureBytes);
                isUploaded = uploadPicture(staff);
                break;
            default:
                isUploaded = false;
                break;
        }
        if (!isUploaded) {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Error uploading picture.");
            return;
        }
        SecurityLog.addSecurityLog(request, "Uploaded picture.");
        RedirectUtilities.redirectWithMessage(request, response, RedirectType.SUCCESS, "Picture uploaded successfully.", Constants.PROFILE_URL);
    }

    private boolean uploadPicture(Customers customer) {
        try {
            userTransaction.begin();
            Customers existingCustomer = entityManager.find(Customers.class, customer.getUserId());
            if (existingCustomer == null) {
                return false;
            }
            existingCustomer.setPicture(customer.getPicture());
            entityManager.merge(existingCustomer);
            userTransaction.commit();
            return true;
        } catch (RollbackException ex) {
            return false;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | SystemException | IllegalStateException | SecurityException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    private boolean uploadPicture(Staffs staff) {
        try {
            userTransaction.begin();
            Staffs existingStaff = entityManager.find(Staffs.class, staff.getUserId());
            if (existingStaff == null) {
                return false;
            }
            existingStaff.setPicture(staff.getPicture());
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
