package controllers;

import domain.common.Constants;
import domain.models.Session;
import domain.models.Users;
import domain.request.ProfileRequest;
import features.SecurityLogHandler;
import features.SessionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import services.ProfileServices;
import utilities.RedirectUtilities;
import utilities.RedirectUtilities.RedirectType;
import utilities.StringUtilities;

@MultipartConfig
public class ProfileServlet extends HttpServlet {

    private static final SecurityLogHandler SECURITY_LOG_HANDLER = new SecurityLogHandler();
    private static final ProfileServices profileServices = new ProfileServices();
    private static final SessionHandler sessionHandler = new SessionHandler();

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
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Please login to view this page.", Constants.MAIN_URL);
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
        ProfileRequest profileRequest = new ProfileRequest(session.getUserId());
        Users users = profileServices.getProfile(profileRequest, session.getRole());
        if (users == null) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Error fetching profile details.", Constants.MAIN_URL);
            return;
        }
        request.setAttribute(Constants.USERNAME_ATTRIBUTE, users.getUsername());
        request.setAttribute(Constants.EMAIL_ATTRIBUTE, users.getEmail());
        request.setAttribute(Constants.ADDRESS_ATTRIBUTE, users.getAddress());
        request.setAttribute(Constants.PHONE_ATTRIBUTE, users.getPhoneNumber());
        request.setAttribute(Constants.GENDER_ATTRIBUTE, users.getGender());
        request.setAttribute(Constants.TWO_FACTOR_AUTH_ATTRIBUTE, users.getTwo_factor_auth());
        byte[] picture = users.getPicture();
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
        ProfileRequest profileRequest = new ProfileRequest(session.getUserId(), pictureBytes);
        boolean isUploaded = profileServices.uploadPicture(profileRequest, session.getRole());
        if (isUploaded) {
            SECURITY_LOG_HANDLER.addSecurityLog(request, session, "Uploaded picture.");
            RedirectUtilities.setMessage(request, RedirectType.SUCCESS, "Picture uploaded successfully.");
        } else {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Error uploading picture.");
        }
        RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
    }

    private void removePicture(HttpServletRequest request, HttpServletResponse response, Session session) throws ServletException, IOException {
        ProfileRequest profileRequest = new ProfileRequest(session.getUserId());
        boolean isRemoved = profileServices.removePicture(profileRequest, session.getRole());
        if (isRemoved) {
            SECURITY_LOG_HANDLER.addSecurityLog(request, session, "Removed picture.");
            RedirectUtilities.setMessage(request, RedirectType.SUCCESS, "Picture removed successfully.");
        } else {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Error removing picture.");
        }
        RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
    }

    private void updateProfile(HttpServletRequest request, HttpServletResponse response, Session session) throws ServletException, IOException {
        String username = request.getParameter(Constants.USERNAME_ATTRIBUTE);
        String address = request.getParameter(Constants.ADDRESS_ATTRIBUTE);
        String phoneNumber = request.getParameter(Constants.PHONE_ATTRIBUTE);
        String gender = request.getParameter(Constants.GENDER_ATTRIBUTE);
        boolean two_factor_auth = request.getParameter(Constants.TWO_FACTOR_AUTH_ATTRIBUTE) != null;
        if (StringUtilities.anyNullOrBlank(username, address, phoneNumber, gender)) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "All fields are required.", Constants.PROFILE_URL);
            return;
        }
        ProfileRequest profileRequest = new ProfileRequest(session.getUserId(), username, address, phoneNumber, gender, two_factor_auth);
        boolean isUpdated = profileServices.updateProfile(profileRequest, session.getRole());
        if (isUpdated) {
            SECURITY_LOG_HANDLER.addSecurityLog(request, session, "Updated profile.");
            RedirectUtilities.setMessage(request, RedirectType.SUCCESS, "Profile updated successfully.");
        } else {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Error updating profile.");
        }
        RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
    }
}
