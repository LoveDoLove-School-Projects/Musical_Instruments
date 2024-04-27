package controllers;

import common.Constants;
import entities.Session;
import entities.Customers;
import exceptions.DatabaseException;
import features.SecurityLogHandler;
import features.SessionHandler;
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
import java.util.Base64;
import utilities.RedirectUtilities;
import utilities.RedirectUtilities.RedirectType;
import utilities.StringUtilities;

@MultipartConfig
public class ProfileServlet extends HttpServlet {

    private final SecurityLogHandler securityLogHandler = new SecurityLogHandler();
    private final SessionHandler sessionHandler = new SessionHandler();
    @PersistenceContext
    EntityManager entityManager;
    @Resource
    UserTransaction userTransaction;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleProfile(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleProfile(request, response);
    }

    private void handleProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = sessionHandler.getLoginSession(request.getSession());
        if (!session.isResult()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Please login to view this page.", "/");
            return;
        }
        String path = request.getServletPath();
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            switch (path) {
                case Constants.PROFILE_UPLOAD_PICTURE_URL:
                    uploadPicture(request, response, session);
                    return;
                case Constants.PROFILE_REMOVE_PICTURE_URL:
                    removePicture(request, response, session);
                    return;
                case Constants.PROFILE_UPDATE_URL:
                    updateProfile(request, response, session);
                    return;
            }
        }
        initCustomerProfile(request, response, session);
    }

    private void initCustomerProfile(HttpServletRequest request, HttpServletResponse response, Session session) throws ServletException, IOException {
        Customers existingCustomer = entityManager.find(Customers.class, session.getUserId());
        if (existingCustomer == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Error fetching profile details.", "/");
            return;
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
        request.getRequestDispatcher(Constants.PROFILE_JSP_URL).forward(request, response);
    }

    private void uploadPicture(HttpServletRequest request, HttpServletResponse response, Session session) throws ServletException, IOException {
        InputStream pictureStream = request.getPart("uploadPicture").getInputStream();
        if (pictureStream == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Error uploading picture.", Constants.PROFILE_URL);
            return;
        }
        byte[] pictureBytes = pictureStream.readAllBytes();
        Customers customer = new Customers(session.getUserId(), pictureBytes);
        boolean isUploaded = uploadPicture(customer);
        if (isUploaded) {
            securityLogHandler.addSecurityLog(request, session, "Uploaded picture.");
            RedirectUtilities.setMessage(request, RedirectType.SUCCESS, "Picture uploaded successfully.");
        } else {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Error uploading picture.");
        }
        RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
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

    private void removePicture(HttpServletRequest request, HttpServletResponse response, Session session) throws ServletException, IOException {
        Customers customer = new Customers(session.getUserId());
        boolean isRemoved = removePicture(customer);
        if (isRemoved) {
            securityLogHandler.addSecurityLog(request, session, "Removed picture.");
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

    private void updateProfile(HttpServletRequest request, HttpServletResponse response, Session session) throws ServletException, IOException {
        String username = request.getParameter("username");
        String address = request.getParameter("address");
        String phoneNumber = request.getParameter("phone_number");
        String gender = request.getParameter("gender");
        boolean two_factor_auth = request.getParameter("two_factor_auth") != null;
        if (StringUtilities.anyNullOrBlank(username, address, phoneNumber, gender)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "All fields are required.", Constants.PROFILE_URL);
            return;
        }
        Customers customer = new Customers(session.getUserId(), username, address, phoneNumber, gender, two_factor_auth);
        boolean isUpdated = updateProfile(customer);
        if (isUpdated) {
            securityLogHandler.addSecurityLog(request, session, "Updated profile.");
            RedirectUtilities.setMessage(request, RedirectType.SUCCESS, "Profile updated successfully.");
        } else {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Error updating profile.");
        }
        RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
    }

    public boolean updateProfile(Customers customer) {
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
        } catch (RollbackException ex) {
            return false;
        } catch (HeuristicMixedException | HeuristicRollbackException | NotSupportedException | SystemException | IllegalStateException | SecurityException ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }
}
