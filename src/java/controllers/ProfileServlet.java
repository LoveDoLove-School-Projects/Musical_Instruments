package controllers;

import domain.common.Common;
import domain.common.Constants;
import domain.models.Session;
import domain.request.ProfileRequest;
import domain.response.ProfileResponse;
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
import utilities.StringUtilities;
import utilities.enums.RedirectType;

@MultipartConfig
public class ProfileServlet extends HttpServlet {

    private final ProfileServices profileServices;
    private final SessionHandler sessionHandler;

    public ProfileServlet() {
        this.profileServices = new ProfileServices();
        this.sessionHandler = new SessionHandler();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleProfile(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleProfile(request, response);
    }

    private void handleProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = sessionHandler.getLoginSession(request, response);
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
        ProfileRequest profileRequest = new ProfileRequest(session.getId());
        ProfileResponse profileResponse = profileServices.getProfile(profileRequest, session.getRole());
        if (profileResponse == null || profileResponse.getStatus() == Common.Status.INTERNAL_SERVER_ERROR || profileResponse.getStatus() == Common.Status.NOT_FOUND) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Error fetching profile details.", Constants.MAIN_URL);
            return;
        }
        request.setAttribute("username", profileResponse.getUsername());
        request.setAttribute("email", profileResponse.getEmail());
        request.setAttribute("address", profileResponse.getAddress());
        request.setAttribute("phone_number", profileResponse.getPhoneNumber());
        request.setAttribute("gender", profileResponse.getGender());
        request.setAttribute("two_factor_auth", profileResponse.getTwo_factor_auth());
        byte[] picture = profileResponse.getPicture();
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
        ProfileRequest profileRequest = new ProfileRequest(session.getId(), pictureBytes);
        Common.Status profileStatus = profileServices.uploadPicture(profileRequest, session.getRole());
        if (profileStatus == Common.Status.OK) {
            RedirectUtilities.setMessage(request, RedirectType.SUCCESS, "Picture uploaded successfully.");
        } else {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Error uploading picture.");
        }
        RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
    }

    private void removePicture(HttpServletRequest request, HttpServletResponse response, Session session) throws ServletException, IOException {
        ProfileRequest profileRequest = new ProfileRequest(session.getId());
        Common.Status profileStatus = profileServices.removePicture(profileRequest, session.getRole());
        if (profileStatus == Common.Status.OK) {
            RedirectUtilities.setMessage(request, RedirectType.SUCCESS, "Picture removed successfully.");
        } else {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Error removing picture.");
        }
        RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
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
        ProfileRequest profileRequest = new ProfileRequest(session.getId(), username, address, phoneNumber, gender, two_factor_auth);
        ProfileResponse profileResponse = profileServices.updateProfile(profileRequest, session.getRole());
        if (profileResponse.getStatus() == Common.Status.OK) {
            RedirectUtilities.setMessage(request, RedirectType.SUCCESS, "Profile updated successfully.");
        } else {
            RedirectUtilities.setMessage(request, RedirectType.DANGER, "Error updating profile.");
        }
        RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
    }
}
