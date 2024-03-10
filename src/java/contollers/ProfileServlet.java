package contollers;

import common.Constants;
import features.SessionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import models.Session;
import request.ProfileRequest;
import response.ProfileResponse;
import services.ProfileServices;
import utilities.RedirectUtilities;

public class ProfileServlet extends HttpServlet {

    private final ProfileServices profileServices = new ProfileServices();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            handleProfile(request, response);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            handleProfile(request, response);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void handleProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        Session session = new SessionHandler().checkLoginStatus(request, response);
        if (session.isResult()) {
            if ("POST".equalsIgnoreCase(request.getMethod())) {
            }
            initCustomerProfile(request, response, session.getLogin_id());
        }
    }

    private void initCustomerProfile(HttpServletRequest request, HttpServletResponse response, int login_id) throws ServletException, IOException, SQLException {
        ProfileRequest profileRequest = new ProfileRequest(login_id);
        ProfileResponse profileResponse = profileServices.getCustomerProfile(profileRequest);
        if (profileResponse == null) {
            RedirectUtilities.setErrorMessage(request, "Error fetching profile details.");
            RedirectUtilities.sendRedirect(request, response, "");
            return;
        }
        request.setAttribute("username", profileResponse.getCustomer().getUsername());
        request.setAttribute("email", profileResponse.getCustomer().getEmail());
        request.setAttribute("address", profileResponse.getCustomer().getAddress());
        request.setAttribute("phone_number", profileResponse.getCustomer().getPhoneNumber());
        request.setAttribute("gender", profileResponse.getCustomer().getGender());
        request.getRequestDispatcher(Constants.PROFILE_JSP_URL).forward(request, response);
    }
}
