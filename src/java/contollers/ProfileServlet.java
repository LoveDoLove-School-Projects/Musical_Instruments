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
        Session session = new SessionHandler().checkLoginStatus(request, response);
        if (session.isResult()) {
            String path = request.getServletPath();
            if ("POST".equalsIgnoreCase(request.getMethod())) {
                switch (path) {
                    case Constants.PROFILE_UPLOAD_PICTURE_URL:
                        uploadPicture(request, response, session.getLogin_id());
                        return;
                    case Constants.PROFILE_REMOVE_PICTURE_URL:
                        removePicture(request, response, session.getLogin_id());
                        return;
                }
            }
            initCustomerProfile(request, response, session.getLogin_id());
        }
    }

    private void initCustomerProfile(HttpServletRequest request, HttpServletResponse response, int login_id) throws ServletException, IOException {
        ProfileRequest profileRequest = new ProfileRequest(login_id);
        ProfileResponse profileResponse = profileServices.getCustomerProfile(profileRequest);
        if (profileResponse == null || profileResponse.getStatus() == Common.Status.INTERNAL_SERVER_ERROR || profileResponse.getStatus() == Common.Status.NOT_FOUND) {
            RedirectUtilities.redirectWithError(request, response, "Error fetching profile details.", Constants.MAIN_URL);
            return;
        }
        request.setAttribute("username", profileResponse.getCustomer().getUsername());
        request.setAttribute("email", profileResponse.getCustomer().getEmail());
        request.setAttribute("address", profileResponse.getCustomer().getAddress());
        request.setAttribute("phone_number", profileResponse.getCustomer().getPhoneNumber());
        request.setAttribute("gender", profileResponse.getCustomer().getGender());
        byte[] picture = profileResponse.getCustomer().getPicture();
        if (picture != null) {
            String pictureBase64 = Base64.getEncoder().encodeToString(picture);
            request.setAttribute("pictureBase64", pictureBase64);
        }
        request.getRequestDispatcher(Constants.PROFILE_JSP_URL).forward(request, response);
    }

    private void uploadPicture(HttpServletRequest request, HttpServletResponse response, int login_id) throws ServletException, IOException {
        InputStream pictureStream = request.getPart("picture").getInputStream();
        if (pictureStream == null) {
            RedirectUtilities.redirectWithError(request, response, "Error uploading picture.", Constants.PROFILE_URL);
            return;
        }
        ProfileRequest profileRequest = new ProfileRequest(login_id);
        profileRequest.setPicture(pictureStream);
        ProfileResponse profileResponse = profileServices.uploadPicture(profileRequest);
        if (profileResponse == null || profileResponse.getStatus() == Common.Status.INTERNAL_SERVER_ERROR) {
            RedirectUtilities.setErrorMessage(request, "Error uploading picture.");
        } else if (profileResponse.getStatus() == Common.Status.OK) {
            RedirectUtilities.setSuccessMessage(request, "Picture uploaded successfully.");
        }
        RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
    }

    private void removePicture(HttpServletRequest request, HttpServletResponse response, int login_id) throws ServletException, IOException {
        ProfileRequest profileRequest = new ProfileRequest(login_id);
        ProfileResponse profileResponse = profileServices.removePicture(profileRequest);
        if (profileResponse == null || profileResponse.getStatus() == Common.Status.INTERNAL_SERVER_ERROR) {
            RedirectUtilities.setErrorMessage(request, "Error removing picture.");
        } else if (profileResponse.getStatus() == Common.Status.OK) {
            RedirectUtilities.setSuccessMessage(request, "Picture removed successfully.");
        }
        RedirectUtilities.sendRedirect(request, response, Constants.PROFILE_URL);
    }
}
