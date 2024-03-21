package controllers;

import domain.common.Common;
import domain.common.Constants;
import features.SessionChecker;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import domain.models.Session;
import domain.request.AdminRequest;
import domain.response.AdminResponse;
import services.AdminServices;
import utilities.RedirectUtilities;
import utilities.StringUtilities;

public class AdminServlet extends HttpServlet {

    private final AdminServices adminServices = new AdminServices();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleTemplate(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleTemplate(request, response);
    }

    private void handleTemplate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        Session session = new SessionChecker().checkLoginStatus(request, response);
//        if (session.isResult()) {
//            if (session.getIsAdmin() == 0) {
//                RedirectUtilities.redirectWithError(request, response, "You are not admin", Constants.MAIN_URL);
//                return;
//            }
//            String path = request.getServletPath();
//            if ("POST".equalsIgnoreCase(request.getMethod())) {
//                switch (path) {
//                }
//            }
            initAdminProfile(request, response, 1);
//        }
    }

    //    Add your own method here and each method have their own link that added in web.xml under their own servlet
    private void initAdminProfile(HttpServletRequest request, HttpServletResponse response, int token) throws ServletException, IOException {
        AdminRequest adminRequest = new AdminRequest(token);
        AdminResponse adminResponse = adminServices.getAdminProfile(adminRequest);
        if (adminResponse == null || adminResponse.getStatus() == Common.Status.NOT_FOUND) { // Refer Common java class to see have what status then apply in services
            RedirectUtilities.redirectWithError(request, response, "Invalid Admin Profile!", Constants.MAIN_URL);
            return;
        }
        request.setAttribute("username", adminResponse.getUsername());
        request.getRequestDispatcher(Constants.ADMIN_JSP_URL).forward(request, response);
    }
}
