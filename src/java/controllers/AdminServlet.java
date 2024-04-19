package controllers;

import domain.common.Common;
import domain.common.Constants;
import domain.models.Session;
import domain.request.AdminRequest;
import domain.response.AdminResponse;
import features.SessionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import services.AdminServices;
import utilities.RedirectUtilities;
import utilities.RedirectUtilities.RedirectType;

public class AdminServlet extends HttpServlet {

    private static final AdminServices ADMIN_SERVICES = new AdminServices();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Session session = new SessionHandler().getLoginSession(request.getSession());
        if (!session.isResult()) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Please login as admin!", Constants.ADMIN_LOGIN_URL);
            return;
        }
        if (session.getRole() != Common.Role.ADMIN) {
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "You are not admin", Constants.MAIN_URL);
            return;
        }
        String path = request.getServletPath();
        switch (path) {
            case Constants.ADMIN_URL:
                viewAdminMainPage(request, response, session.getId());
                return;
            case Constants.ADMIN_CONTROL_PANEL_URL:
                viewControlPanelPage(request, response, session.getId());
                return;
            case Constants.ADMIN_MANAGE_CUSTOMER_URL:
                viewManageCustomerPage(request, response, session.getId());
                return;
            case Constants.ADMIN_MANAGE_STAFF_URL:
                viewManageStaffPage(request, response, session.getId());
                return;
            case Constants.ADMIN_MANAGE_STOCK_URL:
                viewManageStockPage(request, response, session.getId());
                return;
            case Constants.ADMIN_SALES_URL:
                viewSalesPage(request, response, session.getId());
                return;
            case Constants.ADMIN_VIEW_TRANSACTION_URL:
                viewTransactionPage(request, response, session.getId());
                return;
        }
    }

    private void viewAdminMainPage(HttpServletRequest request, HttpServletResponse response, int token) throws ServletException, IOException {
        AdminRequest adminRequest = new AdminRequest(token);
        AdminResponse adminResponse = ADMIN_SERVICES.getAdminProfile(adminRequest);
        if (adminResponse == null || adminResponse.getStatus() == Common.Status.NOT_FOUND) { // Refer Common java class to see have what status then apply in services
            RedirectUtilities.redirectWithMessage(request, response, RedirectType.DANGER, "Invalid Admin Profile!", Constants.MAIN_URL);
            return;
        }
        request.setAttribute(Constants.USERNAME_ATTRIBUTE, adminResponse.getUsername());
        request.getRequestDispatcher(Constants.ADMIN_JSP_URL).forward(request, response);
    }

    private void viewControlPanelPage(HttpServletRequest request, HttpServletResponse response, int token) throws ServletException, IOException {
        request.getRequestDispatcher(Constants.ADMIN_CONTROL_PANEL_JSP_URL).forward(request, response);
    }

    private void viewManageCustomerPage(HttpServletRequest request, HttpServletResponse response, int token) throws ServletException, IOException {
        request.getRequestDispatcher(Constants.ADMIN_MANAGE_CUSTOMER_JSP_URL).forward(request, response);
    }

    private void viewManageStaffPage(HttpServletRequest request, HttpServletResponse response, int token) throws ServletException, IOException {
        request.getRequestDispatcher(Constants.ADMIN_MANAGE_STAFF_JSP_URL).forward(request, response);
    }

    private void viewManageStockPage(HttpServletRequest request, HttpServletResponse response, int token) throws ServletException, IOException {
        request.getRequestDispatcher(Constants.ADMIN_MANAGE_STOCK_JSP_URL).forward(request, response);
    }

    private void viewSalesPage(HttpServletRequest request, HttpServletResponse response, int token) throws ServletException, IOException {
        request.getRequestDispatcher(Constants.ADMIN_SALES_JSP_URL).forward(request, response);
    }

    private void viewTransactionPage(HttpServletRequest request, HttpServletResponse response, int token) throws ServletException, IOException {
        request.getRequestDispatcher(Constants.ADMIN_VIEW_TRANSACTION_JSP_URL).forward(request, response);
    }
}
