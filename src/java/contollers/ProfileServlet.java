package contollers;

import common.Common;
import common.Constants;
import features.SessionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import models.Session;
import request.ProfileRequest;
import response.ProfileResponse;
import services.ProfileServices;
import utilities.RedirectUtilities;

@MultipartConfig
public class ProfileServlet extends HttpServlet {

    private final ProfileServices profileServices = new ProfileServices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleProfile(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleProfile(request, response);
    }

    private void handleProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = new SessionHandler().getLoginSession(request, response);
        if (session.isResult()) {
            String path = request.getServletPath();
            if ("POST".equalsIgnoreCase(request.getMethod())) {
                switch (path) {
                    case Constants.PROFILE_UPLOAD_PICTURE_URL:
                        uploadPicture(request, response, session);
                        return;
                    case Constants.PROFILE_REMOVE_PICTURE_URL:
                        removePicture(request, response, session);
                        return;
                }
            }
            initCustomerProfile(request, response, session);
        }
    }

    private void initCustomerProfile(HttpServletRequest request, HttpServletResponse response, Session session) throws ServletException, IOException {
        ProfileRequest profileRequest = new ProfileRequest(session.getId());
        ProfileResponse profileResponse = profileServices.getProfile(profileRequest, session.getRole());
        if (profileResponse == null || profileResponse.getStatus() == Common.Status.INTERNAL_SERVER_ERROR || profileResponse.getStatus() == Common.Status.NOT_FOUND) {
            RedirectUtilities.redirectWithError(request, response, "Error fetching profile details.", Constants.MAIN_URL);
            return;
        }
        request.setAttribute("username", profileResponse.getProfile().getUsername());
        request.setAttribute("email", profileResponse.getProfile().getEmail());
        request.setAttribute("address", profileResponse.getProfile().getAddress());
        request.setAttribute("phone_number", profileResponse.getProfile().getPhoneNumber());
        request.setAttribute("gender", profileResponse.getProfile().getGender());
        byte[] picture = profileResponse.getProfile().getPicture();
        if (picture != null) {
            String pictureBase64 = Base64.getEncoder().encodeToString(picture);
            request.setAttribute("pictureBase64", pictureBase64);
        }
        request.getRequestDispatcher(Constants.PROFILE_JSP_URL).forward(request, response);
    }

    private void uploadPicture(HttpServletRequest request, HttpServletResponse response, Session session) throws ServletException, IOException {
        InputStream pictureStream = request.getPart("uploadPicture").getInputStream();
        if (pictureStream == null) {
            RedirectUtilities.redirectWithError(request, response, "Error uploading picture.", Constants.PROFILE_URL);
            return;
        }
        ProfileRequest profileRequest = new ProfileRequest(session.getId());
        profileRequest.setPicture(pictureStream);
        ProfileResponse profileResponse = profileServices.uploadPicture(profileRequest, session.getRole());
        if (profileResponse == null || profileResponse.getStatus() == Common.Status.INTERNAL_SERVER_ERROR) {
            RedirectUtilities.setErrorMessage(request, "Error uploading picture.");
        } else if (profileResponse.getStatus() == Common.Status.OK) {
            RedirectUtilities.setSuccessMessage(request, "Picture uploaded successfully.");
        }
        RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
    }

    private void removePicture(HttpServletRequest request, HttpServletResponse response, Session session) throws ServletException, IOException {
        ProfileRequest profileRequest = new ProfileRequest(session.getId());
        ProfileResponse profileResponse = profileServices.removePicture(profileRequest, session.getRole());
        if (profileResponse == null || profileResponse.getStatus() == Common.Status.INTERNAL_SERVER_ERROR) {
            RedirectUtilities.setErrorMessage(request, "Error removing picture.");
        } else if (profileResponse.getStatus() == Common.Status.OK) {
            RedirectUtilities.setSuccessMessage(request, "Picture removed successfully.");
        }
        RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
    }
}
